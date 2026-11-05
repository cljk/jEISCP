package de.csmp.jeiscp.app;

import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The cover from NJA blocks. On a title change the TX-NR626 repeats the previous cover before the new one, so after a
 * title change the cover stays hidden until a different one arrives, or for {@link #STALE_TIMEOUT} (same album).
 */
public final class CoverArt {
	private static final Logger log = LoggerFactory.getLogger(CoverArt.class);

	static final Duration STALE_TIMEOUT = Duration.ofSeconds(15);

	private final Clock clock;
	private volatile byte[] image;
	private volatile boolean current;
	private volatile Instant staleSince;
	private ByteArrayOutputStream buffer;	// only the reader thread

	CoverArt(Clock clock) {
		this.clock = clock;
	}

	/**
	 * NJA: image type (0 BMP, 1 JPEG), block flag (0 first, 1 next, 2 last), hex data.
	 * @return whether a new cover is complete
	 */
	boolean block(String nja) {
		if (nja.length() < 2) {
			return false;
		}
		char flag = nja.charAt(1);
		if (flag == '0') {
			buffer = new ByteArrayOutputStream();
		}
		if (buffer == null) {
			return false;	// missed the first block
		}
		String hex = nja.substring(2);
		for (int i = 0; i + 1 < hex.length(); i += 2) {
			buffer.write((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		if (flag != '2') {
			return false;
		}
		byte[] received = buffer.toByteArray();
		buffer = null;
		if (staleSince != null && Arrays.equals(received, image)) {
			log.debug("ignore repeated cover of the previous title");
			return false;
		}
		image = received;
		current = true;
		staleSince = null;
		return true;
	}

	/** The shown cover belongs to the previous title. */
	void titleChanged() {
		if (image != null) {
			staleSince = clock.instant();
		}
	}

	/** A list page covers the cover until the next one completes. */
	void listPageStarted() {
		current = false;
	}

	/** @return the last complete image, BMP or JPEG; {@code null} if none */
	public byte[] getImage() {
		return image;
	}

	/** Whether to show it: complete after the last list page, and of the current title or stale long enough. */
	public boolean isCurrent() {
		Instant since = staleSince;
		boolean fresh = since == null || !clock.instant().isBefore(since.plus(STALE_TIMEOUT));
		return current && fresh;
	}
}
