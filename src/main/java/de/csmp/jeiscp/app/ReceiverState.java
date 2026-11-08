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
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_PLAY_STATUS_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TIME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ISCP;

import java.time.Clock;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector.ConnectionState;
import de.csmp.jeiscp.EiscpListener;

/**
 * Receiver state from incoming ISCP messages, independent of any GUI toolkit: power, volume, input and connection here,
 * NET playback, cover and menu in their parts. Listeners are notified on the connector's reader thread.
 */
public class ReceiverState implements EiscpListener {
	private static final Logger log = LoggerFactory.getLogger(ReceiverState.class);

	/** {@code OTHER}: a message without a dedicated aspect; see {@link #getLastValue(String)}. */
	public enum Aspect { CONNECTION, POWER, VOLUME, MUTE, INPUT, NOW_PLAYING, PLAY_STATUS, NET_LIST, COVER_ART, POPUP, ZONE, OTHER }

	public interface Listener {
		void changed(Aspect aspect);
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();
	private final NetPlayback playback = new NetPlayback();
	private final CoverArt cover;
	private final NetList netList = new NetList();
	private final Map<Zone, ZoneState> zones = new EnumMap<>(Zone.class);	// filled once, read by all threads

	private volatile Boolean power;
	private volatile Integer volume;
	private volatile Boolean muted;
	private volatile String input;
	private volatile ConnectionState connection = ConnectionState.CONNECTED;
	private final Map<String, String> lastValues = new ConcurrentHashMap<>();

	public ReceiverState() {
		this(Clock.systemUTC());
	}

	ReceiverState(Clock clock) {
		cover = new CoverArt(clock);
		for (Zone zone : Zone.values()) {
			zones.put(zone, new ZoneState(zone));
		}
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

		Zone zone = Zone.ofCommand(command);
		if (zone != null) {
			zones.get(zone).received(command, parameter);
			fire(Aspect.ZONE);
			return;
		}

		switch (command) {
			case SYSTEM_POWER_ISCP -> {
				power = "01".equals(parameter);
				fire(Aspect.POWER);
			}
			case MASTER_VOLUME_ISCP -> {
				try {
					volume = Integer.parseInt(parameter, 16);
					fire(Aspect.VOLUME);
				} catch (NumberFormatException ex) {
					log.debug("ignore volume {}", parameter);
				}
			}
			case AUDIO_MUTING_ISCP -> {
				muted = "01".equals(parameter);
				fire(Aspect.MUTE);
			}
			case INPUT_SELECTOR_ISCP -> {
				if (input != null && !parameter.equals(input)) {
					// a real change; the receiver sends NLT right after connecting, before the first SLI answer
					netList.inputChanged();
					playback.inputChanged();
				}
				input = parameter;
				netList.inputSelected();
				fire(Aspect.INPUT);
			}
			case NET_USB_ARTIST_NAME_INFO_ISCP -> {
				playback.artist(parameter);
				fire(Aspect.NOW_PLAYING);
			}
			case NET_USB_TITLE_NAME_ISCP -> {
				String before = playback.getTitle();
				if (before != null && !before.equals(parameter)) {
					cover.titleChanged();	// first, so no reader sees the new title with the old cover as current
				}
				playback.title(parameter);
				fire(Aspect.NOW_PLAYING);
			}
			case NET_USB_ALBUM_NAME_INFO_ISCP -> {
				playback.album(parameter);
				fire(Aspect.NOW_PLAYING);
			}
			case NET_USB_TIME_INFO_ISCP -> {
				boolean statusInferred = playback.time(parameter);
				fire(Aspect.NOW_PLAYING);
				if (statusInferred) {
					fire(Aspect.PLAY_STATUS);
				}
			}
			case NET_USB_PLAY_STATUS_ISCP -> {
				playback.status(parameter);
				fire(Aspect.PLAY_STATUS);
			}
			case NET_USB_LIST_TITLE_INFO_ISCP -> {
				netList.listTitle(parameter);
				fire(Aspect.NET_LIST);
			}
			case NET_USB_LIST_INFO_ISCP -> {
				NetList.Update update = netList.info(parameter);
				if (update == NetList.Update.NEW_PAGE) {
					cover.listPageStarted();
				}
				if (update != NetList.Update.NONE) {
					fire(Aspect.NET_LIST);
				}
			}
			case NET_POPUP_MESSAGE_ISCP -> {
				netList.popup(parameter);
				fire(Aspect.POPUP);
			}
			case NET_USB_JACKET_ART_ISCP -> {
				if (cover.block(parameter)) {
					fire(Aspect.COVER_ART);
				}
			}
			default -> fire(Aspect.OTHER);
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

	/** Titles, time, play status, repeat and shuffle of a NET or USB source. */
	public NetPlayback playback() {
		return playback;
	}

	public CoverArt cover() {
		return cover;
	}

	/** List, cursor, service and popup of the NET menu. */
	public NetList netList() {
		return netList;
	}

	public ZoneState zone(Zone zone) {
		return zones.get(zone);
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

	/** Last parameter received for a 3-letter command, e.g. {@code 0C} for {@code LMD}; {@code null} if none yet. */
	public String getLastValue(String command) {
		return lastValues.get(command);
	}

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
}
