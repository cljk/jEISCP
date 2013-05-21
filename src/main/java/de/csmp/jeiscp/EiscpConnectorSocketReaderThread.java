package de.csmp.jeiscp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static de.csmp.jeiscp.EiscpConstants.*;


public class EiscpConnectorSocketReaderThread implements Runnable {
	private static final Log log = LogFactory.getLog(EiscpConnectorSocketReaderThread.class);
	
	EiscpConnector conn;
	BufferedInputStream socketIn;
	EiscpListener listener;
	
	private boolean quit = false;
	
	ByteArrayOutputStream buffer = new ByteArrayOutputStream(128);
	
	public EiscpConnectorSocketReaderThread(EiscpConnector conn, BufferedInputStream socketIn, EiscpListener listener) {
		this.conn = conn;
		this.socketIn = socketIn;
		this.listener = listener;
	}

	@Override
	public void run() {
		byte[] response = new byte[4];

		while (! quit) {
			log.trace("readLoop");
			try {
				// read first block manually to skip EOFs in buffer
				do {
					response[0] = (byte) socketIn.read();
				} while (EiscpProtocolHelper.isEofMarker(response[0]));
				if (response[0] != 0x49) 
					throw new EiscpMessageFormatException(
							"First byte not valid for begin of message: " + 
										EiscpProtocolHelper.convertToHexString(response[0]));
				response[1] = (byte) socketIn.read();
				response[2] = (byte) socketIn.read();
				response[3] = (byte) socketIn.read();
				EiscpProtocolHelper.validateIscpSignature(response, 0);
				
				blockedReadQuadrupel(response);
				EiscpProtocolHelper.validateHeaderLengthSignature(response, 0);
				
				blockedReadQuadrupel(response);
				int messageSize = EiscpProtocolHelper.readMessageSize(response, 0);
				
				blockedReadQuadrupel(response);
				EiscpProtocolHelper.validateEiscpVersion(response, 0);

				buffer.reset();
				for (int i=0; i<messageSize; i++) {
					buffer.write(socketIn.read());
				}
				
				byte[] iscpMessage = buffer.toByteArray();
				String res = EiscpProtocolHelper.parseIscpMessage(iscpMessage);
				listener.receivedIscpMessage(res);
			} catch (EiscpMessageFormatException ex) {
				log.warn(ex.getMessage() + " - " + EiscpProtocolHelper.convertToHexString(response));
				log.debug("skip bytes until EOF/CR");
				
				if (isEofMarkerfInArray(response)) {
					log.debug("found eof in response block");
				} else {
					boolean eofFound = false;
					try {
						while (! eofFound) {
							byte b = (byte) socketIn.read();
							if (b == -1) {
								log.debug("end of stream");
								quit();
								eofFound = true;
							} else {
								log.debug("discard " + EiscpProtocolHelper.convertToHexString(new byte[]{b}));
								eofFound = EiscpProtocolHelper.isEofMarker(b);
							}
						};
						log.trace("found EOF");
					} catch (Exception ex2) {
						log.error("not handled", ex2);
					}
				}
			} catch (Exception ex) {
				log.warn(ex.getMessage());
				ex.printStackTrace();
				quit();
			}
		}
	}

	public boolean isEofMarkerfInArray(byte[] response) {
		boolean eofFound = false;
		for (int i=0; i<response.length; i++) {
			eofFound = eofFound || EiscpProtocolHelper.isEofMarker(response[i]);
		}
		return eofFound;
	}

	private void blockedReadQuadrupel(byte[] bb) throws IOException {
		bb[0] = (byte) socketIn.read();
		bb[1] = (byte) socketIn.read();
		bb[2] = (byte) socketIn.read();
		bb[3] = (byte) socketIn.read();
	}
	
	public void quit() {
		quit = true;
	}
}
