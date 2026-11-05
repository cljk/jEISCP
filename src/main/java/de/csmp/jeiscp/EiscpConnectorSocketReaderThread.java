package de.csmp.jeiscp;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads the eISCP frames of one connection and hands their ISCP messages to {@code sink}.
 * After corrupt data it resynchronizes on the next {@code ISCP} marker. If reading ends without {@link #quit()}
 * (end of stream, I/O error), {@code onLost} learns about it.
 */
public class EiscpConnectorSocketReaderThread implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(EiscpConnectorSocketReaderThread.class);

	/** Larger data sizes come from a corrupt header; cover art blocks are far smaller. */
	static final int MAX_MESSAGE_SIZE = 64 * 1024;
	private static final byte[] MAGIC = "ISCP".getBytes(StandardCharsets.US_ASCII);

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
		byte[] header = new byte[12];	// header size, data size, version; the marker is read by syncToMarker()

		while (! quit) {
			log.trace("readLoop");
			try {
				syncToMarker();
				readFully(header);
				EiscpProtocolHelper.validateHeaderLengthSignature(header, 0);
				int messageSize = EiscpProtocolHelper.readMessageSize(header, 4);
				EiscpProtocolHelper.validateEiscpVersion(header, 8);
				if (messageSize < 2 || messageSize > MAX_MESSAGE_SIZE) {
					throw new EiscpMessageFormatException("implausible data size " + messageSize);
				}

				byte[] iscpMessage = new byte[messageSize];
				readFully(iscpMessage);

				String iscpResult = EiscpProtocolHelper.parseIscpMessage(iscpMessage);
				if (log.isDebugEnabled()) {
					log.debug("received: {}", abbreviate(iscpResult));
				}
				sink.receivedIscpMessage(iscpResult);
			} catch (EiscpMessageFormatException ex) {
				// the next syncToMarker() skips whatever follows up to the next frame
				log.warn("{} - {}; resync on the next ISCP marker", ex.getMessage(), EiscpProtocolHelper.convertToHexString(header));
			} catch (Exception ex) {
				stopped(ex);
			}
		}
	}

	/** Consumes bytes until the last four were "ISCP"; anything before them is logged as skipped. */
	private void syncToMarker() throws IOException {
		int matched = 0;
		long skipped = 0;
		while (matched < MAGIC.length) {
			int b = socketIn.read();
			if (b == -1) {
				throw new EOFException("end of stream");
			}
			if (b == MAGIC[matched]) {
				matched++;
			} else {
				skipped += matched;
				// the marker's letters are all different, so only its first letter can start a new match
				if (b == MAGIC[0]) {
					matched = 1;
				} else {
					matched = 0;
					skipped++;
				}
			}
		}
		if (skipped > 0) {
			log.warn("skipped {} bytes before the next ISCP frame", skipped);
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
