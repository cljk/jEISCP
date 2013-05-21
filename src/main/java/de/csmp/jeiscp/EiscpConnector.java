package de.csmp.jeiscp;

import static de.csmp.jeiscp.EiscpConstants.DEFAULT_EISCP_PORT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EiscpConnector {
	

	private static final Log log = LogFactory.getLog(EiscpConnector.class);

	
	Socket socket;
	BufferedOutputStream socketOut;
	BufferedInputStream socketIn;
	
	
	EiscpConnectorSocketReaderThread t = null;
	Thread lt = null;
	
	boolean closed = false;
	
	
	
	public static EiscpConnector autodiscover() throws Exception {
		String queryDatagramString = EiscpConstants.AUTODISCOVER_QSTN;
		byte[] queryDatagram = EiscpProtocolHelper.iscpToEiscpMessage(queryDatagramString);
		
		int port = DEFAULT_EISCP_PORT;

		DatagramSocket datagramSocket = new DatagramSocket();
		datagramSocket.setBroadcast(true);
		
		log.debug("send autodiscover datagram: " + queryDatagramString);// + " -> " + EiscpProtocolHelper.convertToHexString(queryDatagram));
		DatagramPacket p = new DatagramPacket(
				queryDatagram, queryDatagram.length);
		p.setAddress(
			InetAddress.getByAddress(
				new byte[] { 
					(byte) 255, (byte) 255, (byte) 255, (byte) 255 }));
		p.setPort(port);
		datagramSocket.send(p);

		do {
			try {
				log.info("wait for autodiscover answere");
				return receiveAutodiscoverAnswere(datagramSocket);	
			} catch (Exception ex) {
				log.warn(ex);
			}
		} while(true);
	}

	public static EiscpConnector receiveAutodiscoverAnswere(DatagramSocket datagramSocket) throws Exception {
		byte[] buf = new byte[256];
		
		DatagramPacket pct = new DatagramPacket(buf, buf.length);
		
		datagramSocket.receive(pct);

		byte[] receivedMessage = new byte[pct.getLength()];
		System.arraycopy(buf, 0, receivedMessage, 0, receivedMessage.length);

		log.debug("answere from " + pct.getSocketAddress());
		
		String responseString = EiscpProtocolHelper.interpreteEiscpResponse(receivedMessage);
		String address = pct.getAddress().getHostAddress().toString();
		EiscpConnector conn = new EiscpConnector(address, responseString);
		return conn;
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
	
	public void attachListener(EiscpListener listener) {
		if (lt == null) {
			t = new EiscpConnectorSocketReaderThread(this, socketIn, listener);
			lt = new Thread(t);
			lt.start();
		} else {
			// FIXME implement more than one listener
		}
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
	
	
	public void sendCommand(String command) throws IOException {
		log.debug("sendCommand: " + command);
		String message = "!1" + command;
		
		sendIscpMessage(message);
	}
	
	
	public void sendIscpMessage(String message) throws IOException {
		byte[] eiscpMessage = EiscpProtocolHelper.iscpToEiscpMessage(message);
		
		if (log.isDebugEnabled()) {
			log.debug("sendIscpMessage: " + message + 
					" - eISCP message: " + eiscpMessage.length + " bytes - " + EiscpProtocolHelper.convertToHexString(eiscpMessage));
		}
		socketOut.write(eiscpMessage);
		socketOut.flush();
	}



	public void close() {
		log.debug("-- close");
		
		if (t != null) {
			t.quit();
			//lt.notify();
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
