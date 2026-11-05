package de.csmp.jeiscp.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** The NET menu: list title and service (NLT), lines and cursor (NLS) and popups (NPU). */
public final class NetList {

	/** As the receiver shows it on the TV, e.g. a service's connection error. */
	public record Popup(String topTitle, String title, String message, List<String> buttons) {
	}

	enum Update { NONE, CHANGED, NEW_PAGE }

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

	private volatile String title;
	private volatile String service;
	private volatile List<String> lines = List.of();
	private volatile int cursor = -1;
	private volatile Popup popup;
	private final Map<Integer, String> lineData = new TreeMap<>();	// only the reader thread

	NetList() {
	}

	void listTitle(String nlt) {
		title = nlt.length() > 22 ? nlt.substring(22) : "";	// the readable title follows fixed-width fields
		service = parseService(nlt);
	}

	/** NLS: {@code C<line><P|C>} moves the cursor or starts a new page, {@code U|A<line><property><text>} sets a line. */
	Update info(String nls) {
		if (nls.length() < 2) {
			return Update.NONE;
		}
		char type = nls.charAt(0);
		int line = Character.isDigit(nls.charAt(1)) ? Character.digit(nls.charAt(1), 10) : -1;
		if (type == 'C') {
			boolean newPage = nls.length() > 2 && nls.charAt(2) == 'P';
			if (newPage) {
				lineData.clear();
				lines = List.of();
				popup = null;
			}
			cursor = line;
			return newPage ? Update.NEW_PAGE : Update.CHANGED;
		}
		if ((type == 'U' || type == 'A') && line >= 0) {
			lineData.put(line, nls.length() > 3 ? nls.substring(3) : "");
			lines = List.copyOf(lineData.values());
			return Update.CHANGED;
		}
		return Update.NONE;
	}

	void popup(String npu) {
		popup = parsePopup(npu);
	}

	/** The receiver sends NLT right after connecting, before the first SLI answer, so only a real change resets. */
	void inputChanged() {
		service = null;
		popup = null;
	}

	/** Any input answer closes a popup. */
	void inputSelected() {
		popup = null;
	}

	/** The user closed the popup; the receiver sends no message for that. */
	public void dismissPopup() {
		popup = null;
	}

	public String getTitle() {
		return title;
	}

	/** NET service such as {@code Spotify} or {@code TuneIn Radio}, from the last NLT; {@code null} on the NET top level. */
	public String getService() {
		return service;
	}

	public List<String> getLines() {
		return lines;
	}

	public int getCursor() {
		return cursor;
	}

	/** @return {@code null} if no popup is open, as far as the receiver told us */
	public Popup getPopup() {
		return popup;
	}

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
		String text = nlt.substring(22).trim();
		if (name == null && nlt.charAt(3) != '0' && !text.isEmpty()) {
			name = text;
		}
		return name;
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
			List<String> items = new ArrayList<>();
			for (int i = 1; i + 1 < parts.length; i += 2) {
				if (!parts[i].isEmpty()) {
					items.add(parts[i] + (parts[i + 1].isEmpty() ? "" : ": " + parts[i + 1]));
				}
			}
			return new Popup(top, "", String.join("\n", items), List.of());
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
}
