package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_OFF_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_LEVEL_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_LEVEL_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MONITOR_OUT_RESOLUTION_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_LEFT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_PAUSE_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_PLAY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_RETURN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_RIGHT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_SELECT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_STOP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TOP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TRDN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_RANDOM_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_REPEAT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TRUP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ALBUM_NAME_INFO_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ARTIST_NAME_INFO_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_JACKET_ART_REQ_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TIME_INFO_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_PLAY_STATUS_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TIME_SEEK_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_ENTER_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_EXIT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_HOME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_LEFT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_MENU_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_QUICK_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_RIGHT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_STANDBY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.VIDEO_INFORMATION_QUERY_ISCP;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * The remote's operations as ISCP commands, so views never build command strings.
 * Toggles read the current value from {@link ReceiverState}.
 */
public class ReceiverControl {

	/** Where the direction pad, OK, return and home keys act: NET/USB lists or the receiver's menus on the TV. */
	public enum Target { NET, TV }

	public enum Direction { UP, DOWN, LEFT, RIGHT }

	public enum Playback { PREVIOUS, PLAY, PAUSE, STOP, NEXT }

	/** Highest level of {@code MVL}, 0x64. */
	public static final int MAX_VOLUME_LEVEL = 100;

	private static final String QUERY = "QSTN";

	private final Consumer<String> sender;
	private final ReceiverState state;
	private final List<Consumer<String>> sentListeners = new CopyOnWriteArrayList<>();
	private volatile String coverRequestedFor;

	/** @param sender passes an ISCP command (without {@code !1}) to the receiver */
	public ReceiverControl(Consumer<String> sender, ReceiverState state) {
		this.sender = sender;
		this.state = state;
	}

	/** Notified after every command sent through this control, on the sending thread. */
	public void addSentListener(Consumer<String> listener) {
		sentListeners.add(listener);
	}

	public void togglePower() {
		send(Boolean.TRUE.equals(state.getPower()) ? SYSTEM_POWER_STANDBY_ISCP : SYSTEM_POWER_ON_ISCP);
	}

	public void toggleMute() {
		send(Boolean.TRUE.equals(state.getMuted()) ? AUDIO_MUTING_OFF_ISCP : AUDIO_MUTING_ON_ISCP);
	}

	public void volumeUp() {
		send(MASTER_VOLUME_LEVEL_UP_ISCP);
	}

	public void volumeDown() {
		send(MASTER_VOLUME_LEVEL_DOWN_ISCP);
	}

	/** Sends the level (clamped to 0..100) unless the receiver already reports it, so echoes are not sent back. */
	public void setVolume(int level) {
		int clamped = Math.max(0, Math.min(MAX_VOLUME_LEVEL, level));
		Integer current = state.getVolume();
		if (current == null || current != clamped) {
			send(MASTER_VOLUME_ISCP + String.format("%02X", clamped));
		}
	}

	/**
	 * Jumps to a position of the playing track (NTS). Only while the total time is known; whether the receiver
	 * can seek depends on the source (USB and music server usually can, radio streams cannot).
	 */
	public void seek(int seconds) {
		ReceiverState.PlayTime playTime = state.getPlayTime();
		if (playTime == null || playTime.total() <= 0) {
			return;
		}
		int target = Math.max(0, Math.min(playTime.total(), seconds));
		send(NET_USB_TIME_SEEK_ISCP + clock(target, playTime.total() >= 3600));
	}

	/** {@code 01:05} or, with hours, {@code 00:01:05}. */
	public static String clock(int seconds, boolean withHours) {
		int h = seconds / 3600;
		int m = seconds / 60 % 60;
		int s = seconds % 60;
		return withHours ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", seconds / 60, s);
	}

	/** @param code SLI parameter such as {@code 2B} for NET */
	public void selectInput(String code) {
		send(INPUT_SELECTOR_ISCP + code);
	}

	public void navigate(Target target, Direction direction) {
		send(switch (direction) {
			case UP -> byTarget(target, NETWORK_USB_UP_ISCP, SETUP_UP_ISCP);
			case DOWN -> byTarget(target, NETWORK_USB_DOWN_ISCP, SETUP_DOWN_ISCP);
			case LEFT -> byTarget(target, NETWORK_USB_LEFT_ISCP, SETUP_LEFT_ISCP);
			case RIGHT -> byTarget(target, NETWORK_USB_RIGHT_ISCP, SETUP_RIGHT_ISCP);
		});
	}

	public void ok(Target target) {
		send(byTarget(target, NETWORK_USB_SELECT_ISCP, SETUP_ENTER_ISCP));
	}

	/** NET: one level back; TV: leaves the menu level. */
	public void back(Target target) {
		send(byTarget(target, NETWORK_USB_RETURN_ISCP, SETUP_EXIT_ISCP));
	}

	/** NET: top of the service menu; TV: the receiver's home menu. */
	public void home(Target target) {
		send(byTarget(target, NETWORK_USB_TOP_ISCP, SETUP_HOME_ISCP));
	}

	public void openSetupMenu() {
		send(SETUP_MENU_ISCP);
	}

	public void openQuickMenu() {
		send(SETUP_QUICK_ISCP);
	}

	public void playback(Playback playback) {
		send(switch (playback) {
			case PREVIOUS -> NETWORK_USB_TRDN_ISCP;
			case PLAY -> NETWORK_USB_PLAY_ISCP;
			case PAUSE -> NETWORK_USB_PAUSE_ISCP;
			case STOP -> NETWORK_USB_STOP_ISCP;
			case NEXT -> NETWORK_USB_TRUP_ISCP;
		});
	}

	/** The receiver steps to the service's next repeat mode and reports it with {@code NST}. */
	public void toggleRepeat() {
		send(NETWORK_USB_REPEAT_ISCP);
	}

	/** The receiver steps to the service's next shuffle mode and reports it with {@code NST}. */
	public void toggleShuffle() {
		send(NETWORK_USB_RANDOM_ISCP);
	}

	public void numberKey(int digit) {
		if (digit < 0 || digit > 9) {
			throw new IllegalArgumentException("not a digit: " + digit);
		}
		send(NETWORK_USB_ISCP + digit);
	}

	/** @param prefix 3-letter command such as {@code LMD} */
	public void query(String prefix) {
		send(prefix + QUERY);
	}

	/** What the remote shows at start and after a reconnect. */
	public void queryStatus() {
		send(SYSTEM_POWER_QUERY_ISCP);
		send(MASTER_VOLUME_QUERY_ISCP);
		send(AUDIO_MUTING_QUERY_ISCP);
		send(VIDEO_INFORMATION_QUERY_ISCP);
		send(MONITOR_OUT_RESOLUTION_QUERY_ISCP);
		send(INPUT_SELECTOR_QUERY_ISCP);
	}

	/**
	 * On a NET or Bluetooth input, asks for what is still unknown: artist, title, album, time, play status and cover art.
	 * The receiver pushes these by itself only when they change, so a freshly started app would miss them.
	 */
	public void queryNowPlayingIfMissing() {
		if (state.isNetworkInput()) {
			if (state.getArtist() == null) {
				send(NET_USB_ARTIST_NAME_INFO_QUERY_ISCP);
			}
			if (state.getTitle() == null) {
				send(NET_USB_TITLE_NAME_QUERY_ISCP);
			}
			if (state.getAlbum() == null) {
				send(NET_USB_ALBUM_NAME_INFO_QUERY_ISCP);
			}
			if (state.getTime() == null) {
				send(NET_USB_TIME_INFO_QUERY_ISCP);
			}
			if (state.getPlayStatus() == null) {
				send(NET_USB_PLAY_STATUS_QUERY_ISCP);
			}
			requestCoverIfMissing();
		}
	}

	/**
	 * Asks for the cover only while something plays (time and title are known) and once per title:
	 * on a menu list an old cover would hide the list, and a service without art would be asked every second.
	 */
	public void requestCoverIfMissing() {
		if (!state.isNetworkInput() || state.getCoverArt() != null || state.getPlayTime() == null || state.getTitle() == null) {
			return;
		}
		String track = state.getInput() + "|" + state.getTitle();
		if (!track.equals(coverRequestedFor)) {
			coverRequestedFor = track;
			send(NET_USB_JACKET_ART_REQ_ISCP);
		}
	}

	/** Any ISCP command, e.g. a value from the functions page. */
	public void send(String command) {
		sender.accept(command);
		for (Consumer<String> listener : sentListeners) {
			listener.accept(command);
		}
	}

	private static String byTarget(Target target, String net, String tv) {
		return target == Target.NET ? net : tv;
	}
}
