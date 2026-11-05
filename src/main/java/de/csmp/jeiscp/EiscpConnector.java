package de.csmp.jeiscp;

import static de.csmp.jeiscp.EiscpConstants.DEFAULT_EISCP_PORT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * autodiscover device / aquire connection
 * 
 * @author marcelpokrandt
 *
 */
public class EiscpConnector {
	private static final Logger log = LoggerFactory.getLogger(EiscpConnector.class);

	
	Socket socket;
	BufferedOutputStream socketOut;
	BufferedInputStream socketIn;
	
	
	EiscpConnectorSocketReaderThread eiscpConnectorReaderThread = null;
	Thread eiscpConnectorReaderThreadThread = null;
	
	boolean closed = false;
	
	
	
	/**
	 * Connects to the first receiver that answers the broadcast.
	 * @throws java.net.SocketTimeoutException if none answers within {@link EiscpAutodiscovery#DEFAULT_TIMEOUT}
	 */
	public static EiscpConnector autodiscover() throws IOException {
		EiscpDevice device = new EiscpAutodiscovery().discover();
		return new EiscpConnector(device.getIpAddress(), device.getPort());
	}


	public EiscpConnector(String address, String autodiscoverResponse) throws UnknownHostException, IOException {
		// TODO parse
		log.debug("autodiscovered: " + autodiscoverResponse);
		init(address, DEFAULT_EISCP_PORT);
	}
	
	public EiscpConnector(String address) throws UnknownHostException, IOException {
		init(address, DEFAULT_EISCP_PORT);
	}
	
	public EiscpConnector(String address, int port) throws UnknownHostException, IOException {
		init(address, port);
	}
	
	public void addListener(EiscpListener listener) {
		if (eiscpConnectorReaderThreadThread == null) {
			eiscpConnectorReaderThread = new EiscpConnectorSocketReaderThread(this, socketIn);
			eiscpConnectorReaderThreadThread = new Thread(eiscpConnectorReaderThread);
			eiscpConnectorReaderThreadThread.start();
		}
		
		eiscpConnectorReaderThread.addListener(listener);
	}
	
	public void removeListener(EiscpListener listener) {
		eiscpConnectorReaderThread.removeListener(listener);
	}
	
	private void init(String address, int port) throws UnknownHostException, IOException {
		log.debug("connect to " + address + ":" + port);
		
		socket = new Socket(address, port);
		socketOut = new BufferedOutputStream(socket.getOutputStream());
		socketIn = new BufferedInputStream(socket.getInputStream());
		
		log.debug("connected");
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (! closed) close();
		super.finalize();
	}

	/** IP address of the connected receiver, e.g. {@code 192.168.0.97}. */
	public String getAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	public int available() throws IOException {
		return socketIn.available();
	}
	
	public byte[] readMessage() throws IOException {
		int a = socketIn.available();
		if (a <= 0) return null;
		
		byte[] res = new byte[a];
		int readBytes = socketIn.read(res);
		
		if (readBytes < a) throw new IOException("read too less bytes");
		return res;
	}
	
	
	public void sendIscpCommand(String command) throws IOException {
		log.debug("sendIscpCommand: " + command);
		String message = "!1" + command;
		
		sendIscpMessage(message);
	}
	
	/**
	 * 
	 * @param message already has prepended !1
	 * @throws IOException
	 */
	public synchronized void sendIscpMessage(String message) throws IOException {
		byte[] eiscpMessage = EiscpProtocolHelper.iscpToEiscpMessage(message);
		
		log.trace("sendIscpMessage: {} - eISCP message: {} bytes - {}",
					message, 
					eiscpMessage.length, 
					EiscpProtocolHelper.convertToHexString(eiscpMessage));
		
		socketOut.write(eiscpMessage);
		socketOut.flush();
	}

	public void close() {
		log.debug("-- close");
		
		if (eiscpConnectorReaderThread != null) {
			eiscpConnectorReaderThread.quit();
		}
		
		try {
			socketOut.close();
		} catch (Exception ex) {}

		try {
			socket.close();
		} catch (Exception ex) {}
		
		closed = true;
	}
}