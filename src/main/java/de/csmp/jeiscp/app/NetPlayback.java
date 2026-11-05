package de.csmp.jeiscp.app;

/** What a NET or USB source plays: titles (NAT, NTI, NAL), time (NTM) and play status, repeat and shuffle (NST). */
public final class NetPlayback {

	/** Fast forward and rewind count as playing, end of file as stopped. */
	public enum PlayStatus { PLAY, PAUSE, STOP }

	/** {@code UNAVAILABLE} when the service offers none. */
	public enum Repeat {
		OFF, ALL, FOLDER, ONE, UNAVAILABLE;

		public boolean isOn() {
			return this != OFF && this != UNAVAILABLE;
		}
	}

	/** {@code UNAVAILABLE} when the service offers none. */
	public enum Shuffle {
		OFF, ALL, ALBUM, FOLDER, UNAVAILABLE;

		public boolean isOn() {
			return this != OFF && this != UNAVAILABLE;
		}
	}

	/** Position in seconds; {@code total} is 0 if the receiver does not know it. */
	public record PlayTime(int elapsed, int total) {
		/** 0..1, or -1 without a known total. */
		public double progress() {
			return total > 0 ? Math.min(1.0, elapsed / (double) total) : -1;
		}
	}

	private volatile String artist;
	private volatile String title;
	private volatile String album;
	private volatile String time;
	private volatile PlayStatus playStatus;
	private volatile Repeat repeat;
	private volatile Shuffle shuffle;

	NetPlayback() {
	}

	void artist(String value) {
		artist = value;
	}

	void title(String value) {
		title = value;
	}

	void album(String value) {
		album = value;
	}

	/**
	 * The TX-NR626 does not answer {@code NSTQSTN} and sends {@code NST} only on changes. Until then a running time
	 * means playing: it sends {@code NTM} only while something plays.
	 * @return whether this made the play status known
	 */
	boolean time(String ntm) {
		PlayTime before = getPlayTime();
		time = ntm;
		PlayTime now = getPlayTime();
		if (playStatus == null && before != null && now != null && now.elapsed() > before.elapsed()) {
			playStatus = PlayStatus.PLAY;
			return true;
		}
		return false;
	}

	void status(String nst) {
		playStatus = parsePlayStatus(nst);
		repeat = parseRepeat(nst);
		shuffle = parseShuffle(nst);
	}

	/** Another input has its own status. */
	void inputChanged() {
		playStatus = null;
		repeat = null;
		shuffle = null;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getAlbum() {
		return album;
	}

	/** The last NTM as received, e.g. {@code 01:42/05:37}. */
	public String getTime() {
		return time;
	}

	/** @return {@code null} unless the last NTM had a readable elapsed time */
	public PlayTime getPlayTime() {
		return parsePlayTime(time);
	}

	/** @return {@code null} while unknown */
	public PlayStatus getPlayStatus() {
		return playStatus;
	}

	/** @return {@code null} while unknown */
	public Repeat getRepeat() {
		return repeat;
	}

	/** @return {@code null} while unknown */
	public Shuffle getShuffle() {
		return shuffle;
	}

	/** NTM is {@code mm:ss/mm:ss} or {@code hh:mm:ss/hh:mm:ss}; unknown parts read {@code --:--}. */
	static PlayTime parsePlayTime(String ntm) {
		if (ntm == null) {
			return null;
		}
		String[] parts = ntm.split("/");
		int elapsed = seconds(parts[0]);
		if (elapsed < 0) {
			return null;
		}
		return new PlayTime(elapsed, parts.length > 1 ? Math.max(0, seconds(parts[1])) : 0);
	}

	/** @return -1 if not readable */
	private static int seconds(String clock) {
		int result = 0;
		for (String field : clock.trim().split(":")) {
			try {
				result = result * 60 + Integer.parseInt(field);
			} catch (NumberFormatException ex) {
				return -1;
			}
		}
		return result;
	}

	/** NST {@code prs}: the first letter; {@code null} if it is unknown. */
	static PlayStatus parsePlayStatus(String nst) {
		if (nst.isEmpty()) {
			return null;
		}
		return switch (nst.charAt(0)) {
			case 'P', 'F', 'R' -> PlayStatus.PLAY;
			case 'p' -> PlayStatus.PAUSE;
			case 'S', 'E' -> PlayStatus.STOP;
			default -> null;
		};
	}

	/** NST {@code prs}: the second letter; {@code null} if it is unknown. */
	static Repeat parseRepeat(String nst) {
		if (nst.length() < 2) {
			return null;
		}
		return switch (nst.charAt(1)) {
			case '-' -> Repeat.OFF;
			case 'R' -> Repeat.ALL;
			case 'F' -> Repeat.FOLDER;
			case '1' -> Repeat.ONE;
			case 'x' -> Repeat.UNAVAILABLE;
			default -> null;
		};
	}

	/** NST {@code prs}: the third letter; {@code null} if it is unknown. */
	static Shuffle parseShuffle(String nst) {
		if (nst.length() < 3) {
			return null;
		}
		return switch (nst.charAt(2)) {
			case '-' -> Shuffle.OFF;
			case 'S' -> Shuffle.ALL;
			case 'A' -> Shuffle.ALBUM;
			case 'F' -> Shuffle.FOLDER;
			case 'x' -> Shuffle.UNAVAILABLE;
			default -> null;
		};
	}
}
