package de.csmp.jeiscp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Test;

public class EiscpAutodiscoveryTest {

	/** Answer of a TX-NR626 as captured on the wire: 38 bytes ISCP data, zero padded to 255 bytes. */
	static final byte[] NR626_ANSWER = java.util.Arrays.copyOf(HexFormat.ofDelimiter(" ").parseHex(
			"49 53 43 50 00 00 00 10 00 00 00 26 01 00 00 00 21 31 45 43 4E 54 58 2D 4E 52 36 32 36 2F 36 30 "
			+ "31 32 38 2F 58 58 2F 30 30 30 39 42 30 39 35 33 36 39 46 19 0D 0A"), 255);

	static final Duration RESEND = Duration.ofMillis(150);

	FakeReceiver receiver;

	@After
	public void stopReceiver() {
		if (receiver != null) {
			receiver.close();
		}
	}

	@Test
	public void findsReceiverOnFirstQuery() throws Exception {
		receiver = new FakeReceiver(0);

		EiscpDevice device = discovery(Duration.ofSeconds(3)).discover();

		assertEquals("TX-NR626", device.getDeviceType());
		assertEquals(Integer.valueOf(60128), device.getPort());
		assertEquals("127.0.0.1", device.getIpAddress());
		assertEquals(1, receiver.queries.get());
	}

	/** The old implementation sent one query and then waited forever. */
	@Test
	public void resendsUntilReceiverAnswers() throws Exception {
		receiver = new FakeReceiver(3);

		EiscpDevice device = discovery(Duration.ofSeconds(3)).discover();

		assertEquals("TX-NR626", device.getDeviceType());
		assertEquals(4, receiver.queries.get());
	}

	@Test
	public void ignoresForeignDatagrams() throws Exception {
		receiver = new FakeReceiver(0);
		receiver.sendBeforeAnswer(
				"not eISCP at all".getBytes(StandardCharsets.US_ASCII),
				EiscpProtocolHelper.iscpToEiscpMessage("!1PWR01"));

		EiscpDevice device = discovery(Duration.ofSeconds(3)).discover();

		assertEquals("TX-NR626", device.getDeviceType());
	}

	@Test
	public void givesUpAfterTimeout() throws Exception {
		receiver = new FakeReceiver(Integer.MAX_VALUE);
		Duration timeout = Duration.ofMillis(700);

		long start = System.nanoTime();
		try {
			discovery(timeout).discover();
			fail("expected timeout");
		} catch (SocketTimeoutException expected) {
			long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
			assertTrue("took " + elapsedMillis + "ms", elapsedMillis >= 650 && elapsedMillis < 2000);
			assertTrue("resent several times", receiver.queries.get() >= 3);
		}
	}

	@Test
	public void sendsTheIscpQuery() throws Exception {
		receiver = new FakeReceiver(0);

		discovery(Duration.ofSeconds(3)).discover();

		assertEquals("!xECNQSTN", receiver.lastQuery);
	}

	@Test
	public void identifiesModelOfGivenAddress() throws Exception {
		receiver = new FakeReceiver(1);

		EiscpDevice device = EiscpAutodiscovery.identify("127.0.0.1", receiver.port(), Duration.ofSeconds(3));

		assertEquals("TX-NR626", device.getDeviceType());
	}

	@Test
	public void identifyReturnsNullWithoutAnswer() throws Exception {
		receiver = new FakeReceiver(Integer.MAX_VALUE);

		assertNull(EiscpAutodiscovery.identify("127.0.0.1", receiver.port(), Duration.ofMillis(600)));
	}

	@Test
	public void parsesCapturedAnswer() throws Exception {
		DatagramPacket packet = new DatagramPacket(NR626_ANSWER, NR626_ANSWER.length, InetAddress.getByName("192.168.0.97"), 60128);

		EiscpDevice device = EiscpAutodiscovery.parseAnswer(packet);

		assertEquals("TX-NR626", device.getDeviceType());
		assertEquals("192.168.0.97", device.getIpAddress());
	}

	@Test
	public void parseRejectsNonDiscoveryMessages() throws Exception {
		byte[] power = EiscpProtocolHelper.iscpToEiscpMessage("!1PWR01");

		assertNull(EiscpAutodiscovery.parseAnswer(new DatagramPacket(power, power.length, InetAddress.getLoopbackAddress(), 1)));
		assertNull(EiscpAutodiscovery.parseAnswer(new DatagramPacket(new byte[3], 3, InetAddress.getLoopbackAddress(), 1)));
	}

	@Test
	public void broadcastAddressesEndWithLimitedBroadcast() {
		List<InetAddress> addresses = EiscpAutodiscovery.broadcastAddresses();

		assertEquals("255.255.255.255", addresses.get(addresses.size() - 1).getHostAddress());
		for (InetAddress address : addresses) {
			assertTrue(address instanceof Inet4Address);
			assertFalse(address.isLoopbackAddress());
		}
	}

	private EiscpAutodiscovery discovery(Duration timeout) {
		return new EiscpAutodiscovery(List.of(InetAddress.getLoopbackAddress()), receiver.port(), RESEND, timeout);
	}

	/** Answers autodiscovery queries on a free loopback port after dropping the first {@code drop} of them. */
	static class FakeReceiver implements AutoCloseable {
		final DatagramSocket socket;
		final AtomicInteger queries = new AtomicInteger();
		volatile String lastQuery;
		private volatile byte[][] beforeAnswer = new byte[0][];

		FakeReceiver(int drop) throws IOException {
			socket = new DatagramSocket(0, InetAddress.getLoopbackAddress());
			Thread thread = new Thread(() -> serve(drop), "fake-receiver");
			thread.setDaemon(true);
			thread.start();
		}

		int port() {
			return socket.getLocalPort();
		}

		void sendBeforeAnswer(byte[]... datagrams) {
			beforeAnswer = datagrams;
		}

		private void serve(int drop) {
			byte[] buffer = new byte[512];
			try {
				while (true) {
					DatagramPacket query = new DatagramPacket(buffer, buffer.length);
					socket.receive(query);
					// payload after the 16 byte header, without CR LF
					lastQuery = new String(query.getData(), 16, query.getLength() - 16, StandardCharsets.US_ASCII).trim();
					if (queries.incrementAndGet() <= drop) {
						continue;
					}
					for (byte[] datagram : beforeAnswer) {
						socket.send(new DatagramPacket(datagram, datagram.length, query.getSocketAddress()));
					}
					socket.send(new DatagramPacket(NR626_ANSWER, NR626_ANSWER.length, query.getSocketAddress()));
				}
			} catch (IOException closed) {
				// test finished
			}
		}

		@Override
		public void close() {
			socket.close();
		}
	}
}
