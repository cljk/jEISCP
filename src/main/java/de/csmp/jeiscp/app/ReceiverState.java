package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_ISCP;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpListener;

/**
 * Receiver state assembled from incoming ISCP messages, independent of any GUI toolkit.
 * Listeners are notified on the connector's reader thread, not on a UI thread.
 */
public class ReceiverState implements EiscpListener {
	private static final Logger log = LoggerFactory.getLogger(ReceiverState.class);

	/** {@code OTHER}: a message without a dedicated aspect; see {@link #getLastValue(String)}. */
	public enum Aspect { POWER, VOLUME, MUTE, INPUT, NOW_PLAYING, NET_LIST, COVER_ART, POPUP, OTHER }

	/** NET popup (NPU), e.g. a service's connection error, as the receiver shows it on the TV. */
	public record Popup(String topTitle, String title, String message, List<String> buttons) {
	}

	public interface Listener {
		void changed(Aspect aspect);
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();

	private volatile Boolean power;
	private volatile Integer volume;
	private volatile Boolean muted;
	private volatile String input;

	private volatile String artist;
	private volatile String title;
	private volatile String album;
	private volatile String time;
	private volatile String listTitle;

	private volatile List<String> netLines = List.of();
	private volatile int netCursor = -1;
	private volatile byte[] coverArt;
	private volatile boolean coverArtCurrent;
	private volatile Popup popup;
	private final Map<String, String> lastValues = new ConcurrentHashMap<>();

	// only touched by the reader thread
	private final Map<Integer, String> netLineData = new TreeMap<>();
	private ByteArrayOutputStream coverArtBuffer;

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
				input = parameter;
				popup = null;
				fire(Aspect.INPUT);
				break;
			case NET_USB_ARTIST_NAME_INFO_ISCP:
				artist = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_TITLE_NAME_ISCP:
				title = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_ALBUM_NAME_INFO_ISCP:
				album = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_TIME_INFO_ISCP:
				time = parameter;
				fire(Aspect.NOW_PLAYING);
				break;
			case NET_USB_LIST_TITLE_INFO_ISCP:
				// the readable title starts after the fixed-width header fields
				listTitle = parameter.length() > 22 ? parameter.substring(22) : "";
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
			coverArt = coverArtBuffer.toByteArray();
			coverArtCurrent = true;
			coverArtBuffer = null;
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

	public String getListTitle() {
		return listTitle;
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
	/** @return {@code null} if no popup is open, as far as the receiver told us */
	public Popup getPopup() {
		return popup;
	}

	/** The user closed the popup; the receiver sends no message for that. */
	public void dismissPopup() {
		popup = null;
	}

	public boolean isCoverArtCurrent() {
		return coverArtCurrent;
	}
}
