package de.csmp.jeiscp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.csmp.jeiscp.EiscpConnector.ConnectionState;

/** Connector and reader thread against a receiver simulated on a loopback TCP socket. */
public class EiscpConnectorTest {

	ServerSocket server;
	Socket receiverSide;
	EiscpConnector conn;
	final BlockingQueue<String> received = new LinkedBlockingQueue<>();
	final List<ConnectionState> states = new CopyOnWriteArrayList<>();

	@Before
	public void connect() throws Exception {
		server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
		conn = new EiscpConnector("127.0.0.1", server.getLocalPort());
		conn.setRetryDelays(Duration.ofMillis(50), Duration.ofMillis(200));
		conn.addStateListener(states::add);
		conn.connect();
		receiverSide = server.accept();
	}

	@After
	public void disconnect() throws Exception {
		conn.close();
		receiverSide.close();
		server.close();
	}

	@Test
	public void reconnectsAfterTheReceiverDroppedTheConnection() throws Exception {
		conn.addListener(received::add);

		receiverSide.close();
		server.setSoTimeout(3000);
		receiverSide = server.accept();
		awaitState(ConnectionState.CONNECTED);
		send(frame("!1PWR01\r\n"));

		assertEquals("PWR01", next());
		assertEquals(List.of(ConnectionState.CONNECTING, ConnectionState.CONNECTED,
				ConnectionState.LOST, ConnectionState.CONNECTING, ConnectionState.CONNECTED), states);
	}

	@Test
	public void sendingWhileOfflineFails() throws Exception {
		server.close();	// nothing to reconnect to
		receiverSide.close();
		awaitState(ConnectionState.LOST);

		assertThrows(IOException.class, () -> conn.sendIscpCommand("PWRQSTN"));
	}

	@Test
	public void closeEndsTheConnectionForGood() throws Exception {
		conn.close();

		assertEquals(-1, receiverSide.getInputStream().read());
		server.setSoTimeout(500);
		assertThrows(SocketTimeoutException.class, () -> server.accept());
		assertEquals(ConnectionState.CLOSED, conn.getState());
	}

	@Test
	public void firstConnectFailureIsThrown() throws Exception {
		int unused;
		try (ServerSocket probe = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
			unused = probe.getLocalPort();
		}
		EiscpConnector offline = new EiscpConnector("127.0.0.1", unused);

		assertThrows(IOException.class, offline::connect);
		assertEquals(ConnectionState.DISCONNECTED, offline.getState());
	}

	private void awaitState(ConnectionState expected) throws InterruptedException {
		long deadline = System.currentTimeMillis() + 3000;
		while (conn.getState() != expected && System.currentTimeMillis() < deadline) {
			Thread.sleep(20);
		}
		assertEquals(expected, conn.getState());
	}

	@Test
	public void reportsReceiverAddress() {
		assertEquals("127.0.0.1", conn.getAddress());
	}

	@Test
	public void sendsCommandAsEiscpFrame() throws Exception {
		conn.sendIscpCommand("PWRQSTN");

		assertEquals("!1PWRQSTN\r\n", readFrame());
	}

	@Test
	public void deliversEachFrameWithoutTerminators() throws Exception {
		conn.addListener(received::add);

		send(frame("!1PWR01\u001a\r\n"), frame("!1MVL1A\u001a\r\n"));

		assertEquals("PWR01", next());
		assertEquals("MVL1A", next());
	}

	@Test
	public void skipsCorruptDataUpToTerminator() throws Exception {
		conn.addListener(received::add);

		send("garbage\r".getBytes(StandardCharsets.US_ASCII), frame("!1AMT01\u001a\r\n"));

		assertEquals("AMT01", next());
	}

	@Test
	public void garbageEndingInCrLfDoesNotSwallowTheNextFrame() throws Exception {
		conn.addListener(received::add);

		send("garbage\r\n".getBytes(StandardCharsets.US_ASCII), frame("!1AMT01\u001a\r\n"));

		assertEquals("AMT01", next());
	}

	@Test
	public void resyncsAfterPartialMarkers() throws Exception {
		conn.addListener(received::add);

		send("ISCISIS".getBytes(StandardCharsets.US_ASCII), frame("!1PWR01\r\n"));

		assertEquals("PWR01", next());
	}

	@Test
	public void skipsFramesWithImplausibleHeaders() throws Exception {
		conn.addListener(received::add);

		send(header(16, Integer.MAX_VALUE, 1),	// would allocate 2 GB
				header(16, 9, 2), "!1PWR00\r\n".getBytes(StandardCharsets.US_ASCII),	// unknown version
				header(20, 9, 1),	// unknown header size
				frame("!1MVL1A\r\n"));

		assertEquals("MVL1A", next());
		assertNull(received.poll(200, TimeUnit.MILLISECONDS));
		assertEquals(ConnectionState.CONNECTED, conn.getState());
	}

	@Test
	public void skipsMessagesWithoutStartCharactersOrContent() throws Exception {
		conn.addListener(received::add);

		send(frame("XXPWR00\r\n"), frame("!1\r\n"), frame("!1PWR01\r\n"));

		assertEquals("PWR01", next());
		assertEquals(ConnectionState.CONNECTED, conn.getState());
	}

	@Test
	public void failingListenerDoesNotStopDelivery() throws Exception {
		conn.addListener(message -> {
			throw new IllegalStateException("broken listener");
		});
		conn.addListener(received::add);

		send(frame("!1PWR01\r\n"), frame("!1PWR00\r\n"));

		assertEquals("PWR01", next());
		assertEquals("PWR00", next());
	}

	@Test
	public void removedListenerIsNotCalled() throws Exception {
		BlockingQueue<String> removed = new LinkedBlockingQueue<>();
		EiscpListener listener = removed::add;
		conn.addListener(listener);
		conn.addListener(received::add);
		conn.removeListener(listener);

		send(frame("!1PWR01\r\n"));

		assertEquals("PWR01", next());
		assertNull(removed.poll());
	}

	@Test
	public void logAbbreviatesLongMessages() {
		assertEquals("PWR01", EiscpConnectorSocketReaderThread.abbreviate("PWR01"));
		String coverArt = "NJA11" + "FF".repeat(500);
		assertEquals("NJA11" + "FF".repeat(57) + "F... (1005 chars)", EiscpConnectorSocketReaderThread.abbreviate(coverArt));
	}

	private String next() throws InterruptedException {
		return received.poll(2, TimeUnit.SECONDS);
	}

	private void send(byte[]... chunks) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (byte[] chunk : chunks) {
			bos.write(chunk);
		}
		OutputStream out = receiverSide.getOutputStream();
		out.write(bos.toByteArray());	// one write, so frames arrive back to back
		out.flush();
	}

	private String readFrame() throws IOException {
		DataInputStream in = new DataInputStream(receiverSide.getInputStream());
		byte[] header = new byte[16];
		in.readFully(header);
		byte[] payload = new byte[ByteBuffer.wrap(header, 8, 4).getInt()];
		in.readFully(payload);
		return new String(payload, StandardCharsets.US_ASCII);
	}

	/** Marker and header only, with freely chosen fields. */
	static byte[] header(int headerSize, int dataSize, int version) {
		return ByteBuffer.allocate(16)
				.put("ISCP".getBytes(StandardCharsets.US_ASCII))
				.putInt(headerSize)
				.putInt(dataSize)
				.put(new byte[] { (byte) version, 0, 0, 0 })
				.array();
	}

	/** Frame as a receiver sends it; the payload includes its terminators. */
	static byte[] frame(String payload) {
		byte[] data = payload.getBytes(StandardCharsets.UTF_8);
		return ByteBuffer.allocate(16 + data.length)
				.put("ISCP".getBytes(StandardCharsets.US_ASCII))
				.putInt(16)
				.putInt(data.length)
				.put(new byte[] { 1, 0, 0, 0 })
				.put(data)
				.array();
	}
}
