package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_PLAY_STATUS_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_POPUP_MESSAGE_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ALBUM_NAME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ARTIST_NAME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_JACKET_ART_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_LIST_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_LIST_TITLE_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TIME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ISCP;

import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector.ConnectionState;
import de.csmp.jeiscp.EiscpListener;

/**
 * Receiver state assembled from incoming ISCP messages, independent of any GUI toolkit.
 * Listeners are notified on the connector's reader thread, not on a UI thread.
 */
public class ReceiverState implements EiscpListener {
	private static final Logger log = LoggerFactory.getLogger(ReceiverState.class);

	/** {@code OTHER}: a message without a dedicated aspect; see {@link #getLastValue(String)}. */
	public enum Aspect { CONNECTION, POWER, VOLUME, MUTE, INPUT, NOW_PLAYING, PLAY_STATUS, NET_LIST, COVER_ART, POPUP, OTHER }

	/** NET/USB play status from {@code NST}; fast forward and rewind count as playing, end of file as stopped. */
	public enum PlayStatus { PLAY, PAUSE, STOP }

	/** Repeat mode from {@code NST}; {@code UNAVAILABLE} when the service offers none. */
	public enum Repeat {
		OFF, ALL, FOLDER, ONE, UNAVAILABLE;

		public boolean isOn() {
			return this != OFF && this != UNAVAILABLE;
		}
	}

	/** Shuffle mode from {@code NST}; {@code UNAVAILABLE} when the service offers none. */
	public enum Shuffle {
		OFF, ALL, ALBUM, FOLDER, UNAVAILABLE;

		public boolean isOn() {
			return this != OFF && this != UNAVAILABLE;
		}
	}

	/** NET popup (NPU), e.g. a service's connection error, as the receiver shows it on the TV. */
	public record Popup(String topTitle, String title, String message, List<String> buttons) {
	}

	public interface Listener {
		void changed(Aspect aspect);
	}

	/** How long a cover stays hidden after a title change when no different one arrives (same album). */
	static final Duration STALE_COVER_TIMEOUT = Duration.ofSeconds(15);

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();
	private final Clock clock;

	private volatile Boolean power;
	private volatile Integer volume;
	private volatile Boolean muted;
	private volatile String input;

	private volatile String artist;
	private volatile String title;
	private volatile String album;
	private volatile String time;
	private volatile String listTitle;
	private volatile String netService;
	private volatile PlayStatus playStatus;
	private volatile Repeat repeat;
	private volatile Shuffle shuffle;

	private volatile List<String> netLines = List.of();
	private volatile int netCursor = -1;
	private volatile byte[] coverArt;
	private volatile boolean coverArtCurrent;
	private volatile Instant coverStaleSince;
	private volatile Popup popup;
	private volatile ConnectionState connection = ConnectionState.CONNECTED;
	private final Map<String, String> lastValues = new ConcurrentHashMap<>();

	// only touched by the reader thread
	private final Map<Integer, String> netLineData = new TreeMap<>();
	private ByteArrayOutputStream coverArtBuffer;

	public ReceiverState() {
		this(Clock.systemUTC());
	}

	ReceiverState(Clock clock) {
		this.clock = clock;
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	@Override
	public void receivedIscpMessage(String message) {
		if (message == null || message.length() < 3) {
			return;
		}
		String command = message.substring(0, 3);
		String parameter = message.substring(3);
		lastValues.put(command, parameter);

		switch (command) {
			case SYSTEM_POWER_ISCP:
				power = "01".equals(parameter);
				fire(Aspect.POWER);
				break;
			case MASTER_VOLUME_ISCP:
				try {
					volume = Integer.parseInt(parameter, 16);
					fire(Aspect.VOLUME);
				} catch (NumberFormatException ex) {
					log.debug("ignore volume {}", parameter);
				}
				break;
			case AUDIO_MUTING_ISCP:
				muted = "01".equals(parameter);
				fire(Aspect.MUTE);
				break;
			case INPUT_SELECTOR_ISCP:
				if (input != null && !parameter.equals(input)) {
					// a real change; the receiver sends NLT right after connecting, before the first SLI answer
					netService = null;
					playStatus = null;
					repeat = null;
					shuffle = null;
				}
				input = parameter;
				popup = null;
				fire(Aspect.INPUT);
				break;
			case NET_USB_ARTIST_NAME_INFO_ISCP:
				artist = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_TITLE_NAME_ISCP:
				if (title != null && !parameter.equals(title) && coverArt != null) {
					coverStaleSince = clock.instant();	// the shown cover belongs to the previous title
				}
				title = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_ALBUM_NAME_INFO_ISCP:
				album = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_TIME_INFO_ISCP:
				PlayTime before = getPlayTime();
				time = parameter;
				fire(Aspect.NOW_PLAYING);
				inferPlaying(before);
				break;
			case NET_USB_PLAY_STATUS_ISCP:
				playStatus = parsePlayStatus(parameter);
				repeat = parseRepeat(parameter);
				shuffle = parseShuffle(parameter);
				fire(Aspect.PLAY_STATUS);
				break;
			case NET_USB_LIST_TITLE_INFO_ISCP:
				// the readable title starts after the fixed-width header fields
				listTitle = parameter.length() > 22 ? parameter.substring(22) : "";
				netService = parseService(parameter);
				fire(Aspect.NET_LIST);
				break;
			case NET_USB_LIST_INFO_ISCP:
				handleNetListInfo(parameter);
				break;
			case NET_POPUP_MESSAGE_ISCP:
				popup = parsePopup(parameter);
				fire(Aspect.POPUP);
				break;
			case NET_USB_JACKET_ART_ISCP:
				handleCoverArt(parameter);
				break;
			default:
				fire(Aspect.OTHER);
		}
	}

	/** NLS: {@code C<line><P|C>} moves the cursor or starts a new page, {@code U|A<line><property><text>} sets a line. */
	private void handleNetListInfo(String parameter) {
		if (parameter.length() < 2) {
			return;
		}
		char type = parameter.charAt(0);
		int line = Character.isDigit(parameter.charAt(1)) ? Character.digit(parameter.charAt(1), 10) : -1;

		if (type == 'C') {
			if (parameter.length() > 2 && parameter.charAt(2) == 'P') {
				netLineData.clear();
				popup = null;
				netLines = List.of();
				coverArtCurrent = false;
			}
			netCursor = line;
			fire(Aspect.NET_LIST);
		} else if ((type == 'U' || type == 'A') && line >= 0) {
			netLineData.put(line, parameter.length() > 3 ? parameter.substring(3) : "");
			netLines = List.copyOf(netLineData.values());
			fire(Aspect.NET_LIST);
		}
	}

	/**
	 * NPU: display type, then NUL-separated texts. Type {@code T}/{@code B}: top title, popup title, message,
	 * then the button cursor ({@code 0} = no buttons) directly followed by the button texts.
	 * Type {@code L}: top title, then up to six item/parameter pairs.
	 */
	static Popup parsePopup(String parameter) {
		if (parameter.isEmpty()) {
			return null;
		}
		char type = parameter.charAt(0);
		String[] parts = parameter.substring(1).split("\0", -1);
		String top = part(parts, 0);
		if (type == 'L') {
			List<String> lines = new ArrayList<>();
			for (int i = 1; i + 1 < parts.length; i += 2) {
				if (!parts[i].isEmpty()) {
					lines.add(parts[i] + (parts[i + 1].isEmpty() ? "" : ": " + parts[i + 1]));
				}
			}
			return new Popup(top, "", String.join("\n", lines), List.of());
		}
		List<String> buttons = new ArrayList<>();
		String cursorAndFirst = part(parts, 3);
		if (!cursorAndFirst.isEmpty() && cursorAndFirst.charAt(0) != '0') {
			buttons.add(cursorAndFirst.substring(1));
			for (int i = 4; i < parts.length; i++) {
				buttons.add(parts[i]);
			}
			buttons.removeIf(String::isBlank);
		}
		return new Popup(top, part(parts, 1), part(parts, 2), List.copyOf(buttons));
	}

	private static String part(String[] parts, int index) {
		return index < parts.length ? parts[index].trim() : "";
	}

	/** NJA: image type (0 BMP, 1 JPEG), block flag (0 first, 1 next, 2 last), hex data. */
	private void handleCoverArt(String parameter) {
		if (parameter.length() < 2) {
			return;
		}
		char flag = parameter.charAt(1);
		String hex = parameter.substring(2);

		if (flag == '0') {
			coverArtBuffer = new ByteArrayOutputStream();
		}
		if (coverArtBuffer == null) {
			return;	// missed the first block
		}
		writeHexAsBytes(coverArtBuffer, hex);

		if (flag == '2') {
			byte[] received = coverArtBuffer.toByteArray();
			coverArtBuffer = null;
			if (coverStaleSince != null && Arrays.equals(received, coverArt)) {
				// on a title change the receiver usually repeats the previous cover before sending the new one
				log.debug("ignore repeated cover of the previous title");
				return;
			}
			coverArt = received;
			coverArtCurrent = true;
			coverStaleSince = null;
			fire(Aspect.COVER_ART);
		}
	}

	private static void writeHexAsBytes(ByteArrayOutputStream bos, String hex) {
		for (int i = 0; i + 1 < hex.length(); i += 2) {
			bos.write((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
	}

	private void fire(Aspect aspect) {
		for (Listener listener : listeners) {
			try {
				listener.changed(aspect);
			} catch (RuntimeException ex) {
				log.error("error in state listener: {}", ex.getMessage(), ex);
			}
		}
	}

	/** @return {@code null} while unknown */
	public Boolean getPower() {
		return power;
	}

	/** @return {@code null} while unknown */
	public Integer getVolume() {
		return volume;
	}

	/** @return {@code null} while unknown */
	public Boolean getMuted() {
		return muted;
	}

	/**
	 * The TX-NR626 does not answer {@code NSTQSTN} and sends {@code NST} only on changes. Until then a running time
	 * means playing: it sends {@code NTM} only while something plays.
	 */
	private void inferPlaying(PlayTime before) {
		PlayTime now = getPlayTime();
		if (playStatus == null && before != null && now != null && now.elapsed() > before.elapsed()) {
			playStatus = PlayStatus.PLAY;
			fire(Aspect.PLAY_STATUS);
		}
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

	/** @return SLI parameter such as {@code 2B}, {@code null} while unknown */
	public String getInput() {
		return input;
	}

	/** NET and Bluetooth deliver now-playing and list information. */
	public boolean isNetworkInput() {
		String current = input;
		return "2B".equals(current) || "2E".equals(current);
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

	public String getTime() {
		return time;
	}

	/** Playback position from NTM in seconds; {@code total} is 0 if the receiver does not know it. */
	public record PlayTime(int elapsed, int total) {
		/** 0..1, or -1 without a known total. */
		public double progress() {
			return total > 0 ? Math.min(1.0, elapsed / (double) total) : -1;
		}
	}

	/** @return {@code null} unless the last NTM had a readable elapsed time */
	public PlayTime getPlayTime() {
		return parsePlayTime(time);
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

	public String getListTitle() {
		return listTitle;
	}

	/** NET service such as {@code Spotify} or {@code TuneIn Radio}, from the last NLT; {@code null} on the NET top level. */
	public String getNetService() {
		return netService;
	}

	/** NLT service types from the ISCP documentation. */
	private static final Map<String, String> SERVICES = Map.ofEntries(
			Map.entry("00", "Music Server"), Map.entry("01", "Favorite"), Map.entry("02", "vTuner"),
			Map.entry("03", "SiriusXM"), Map.entry("04", "Pandora"), Map.entry("05", "Rhapsody"),
			Map.entry("06", "Last.fm"), Map.entry("07", "Napster"), Map.entry("08", "Slacker"),
			Map.entry("09", "Mediafly"), Map.entry("0A", "Spotify"), Map.entry("0B", "AUPEO!"),
			Map.entry("0C", "radiko"), Map.entry("0D", "e-onkyo"), Map.entry("0E", "TuneIn Radio"),
			Map.entry("0F", "MP3tunes"), Map.entry("10", "Simfy"), Map.entry("11", "Home Media"),
			Map.entry("12", "Deezer"), Map.entry("13", "iHeartRadio"), Map.entry("18", "AirPlay"),
			Map.entry("19", "TIDAL"), Map.entry("1A", "onkyo music"),
			Map.entry("F0", "USB"), Map.entry("F1", "USB Rear"), Map.entry("F2", "Internet Radio"));

	/**
	 * NLT is {@code xx u y cccc iiii ll s r aa bb ss title}: {@code xx} the service type, {@code y} the layer
	 * (0 = NET top), {@code bb} the service icon. Old firmware keeps {@code xx} at F3 (NET) inside a service and
	 * only names it in the title, so below the top level the title stands in.
	 */
	static String parseService(String nlt) {
		if (nlt.length() < 22) {
			return null;
		}
		String name = SERVICES.get(nlt.substring(0, 2));
		if (name == null) {
			name = SERVICES.get(nlt.substring(18, 20));
		}
		String title = nlt.substring(22).trim();
		if (name == null && nlt.charAt(3) != '0' && !title.isEmpty()) {
			name = title;
		}
		return name;
	}

	/** Last parameter received for a 3-letter command, e.g. {@code 0C} for {@code LMD}; {@code null} if none yet. */
	public String getLastValue(String command) {
		return lastValues.get(command);
	}

	public List<String> getNetLines() {
		return netLines;
	}

	public int getNetCursor() {
		return netCursor;
	}

	public byte[] getCoverArt() {
		return coverArt;
	}

	/** True between a completed cover image and the next list page. */
	/** Mirrors the connector's state, so views can show a lost connection. */
	public void setConnectionState(ConnectionState next) {
		connection = next;
		fire(Aspect.CONNECTION);
	}

	public ConnectionState getConnectionState() {
		return connection;
	}

	public boolean isConnected() {
		return connection == ConnectionState.CONNECTED;
	}

	/** @return {@code null} if no popup is open, as far as the receiver told us */
	public Popup getPopup() {
		return popup;
	}

	/** The user closed the popup; the receiver sends no message for that. */
	public void dismissPopup() {
		popup = null;
	}

	/**
	 * Whether the cover should be shown: it completed after the last list page and belongs to the current title.
	 * After a title change it stays hidden until a different cover arrives, or for {@link #STALE_COVER_TIMEOUT}
	 * if the new title has the same cover.
	 */
	public boolean isCoverArtCurrent() {
		Instant staleSince = coverStaleSince;
		boolean fresh = staleSince == null || !clock.instant().isBefore(staleSince.plus(STALE_COVER_TIMEOUT));
		return coverArtCurrent && fresh;
	}
}
