package de.csmp.jeiscp;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads the eISCP frames of one connection and hands their ISCP messages to {@code sink}.
 * If reading ends without {@link #quit()} (end of stream, I/O error), {@code onLost} learns about it.
 */
public class EiscpConnectorSocketReaderThread implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(EiscpConnectorSocketReaderThread.class);

	private final BufferedInputStream socketIn;
	private final EiscpListener sink;
	private final BiConsumer<EiscpConnectorSocketReaderThread, Exception> onLost;

	private volatile boolean quit = false;

	public EiscpConnectorSocketReaderThread(BufferedInputStream socketIn, EiscpListener sink,
			BiConsumer<EiscpConnectorSocketReaderThread, Exception> onLost) {
		this.socketIn = socketIn;
		this.sink = sink;
		this.onLost = onLost;
	}

	@Override
	public void run() {
		byte[] response = new byte[4];

		while (! quit) {
			log.trace("readLoop");
			try {
				readFully(response);
				EiscpProtocolHelper.validateIscpSignature(response, 0);

				readFully(response);
				EiscpProtocolHelper.validateHeaderLengthSignature(response, 0);

				readFully(response);
				int messageSize = EiscpProtocolHelper.readMessageSize(response, 0);

				readFully(response);
				EiscpProtocolHelper.validateEiscpVersion(response, 0);

				// eISCP encapulation-header ends here - ISCP begins !1xxx

				byte[] iscpMessage = new byte[messageSize];
				readFully(iscpMessage);

				String iscpResult = EiscpProtocolHelper.parseIscpMessage(iscpMessage);
				if (log.isDebugEnabled()) {
					log.debug("received: {}", abbreviate(iscpResult));
				}
				sink.receivedIscpMessage(iscpResult);
			} catch (EiscpMessageFormatException ex) {
				log.warn(ex.getMessage() + " - " + EiscpProtocolHelper.convertToHexString(response));
				log.debug("skip bytes until EOF/CR");

				if (isEofMarkerfInArray(response)) {
					log.debug("found eof in response block");
				} else {
					try {
						skipToTerminator();
						log.trace("found EOF");
					} catch (IOException ex2) {
						stopped(ex2);
					}
				}
			} catch (Exception ex) {
				stopped(ex);
			}
		}
	}

	private void stopped(Exception cause) {
		if (quit) {
			log.debug("reader stopped: {}", cause.getMessage());	// connection closed on purpose
		} else {
			quit = true;
			onLost.accept(this, cause);
		}
	}

	/** Cover art (NJA) arrives as long hex blocks; the log keeps only their start. */
	static String abbreviate(String message) {
		int max = 120;
		return message.length() <= max ? message : message.substring(0, max) + "... (" + message.length() + " chars)";
	}

	public boolean isEofMarkerfInArray(byte[] response) {
		boolean eofFound = false;
		for (int i=0; i<response.length; i++) {
			eofFound = eofFound || EiscpProtocolHelper.isEofMarker(response[i]);
		}
		return eofFound;
	}

	private void skipToTerminator() throws IOException {
		while (true) {
			int b = socketIn.read();
			if (b == -1) {
				throw new EOFException("end of stream");
			}
			if (EiscpProtocolHelper.isEofMarker((byte) b)) {
				return;
			}
		}
	}

	private void readFully(byte[] buffer) throws IOException {
		int read = 0;
		while (read < buffer.length) {
			int n = socketIn.read(buffer, read, buffer.length - read);
			if (n == -1) {
				throw new EOFException("end of stream");
			}
			read += n;
		}
	}

	public void quit() {
		quit = true;
	}
}
