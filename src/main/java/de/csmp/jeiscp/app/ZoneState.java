package de.csmp.jeiscp.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Power, volume, mute and input of one {@link Zone}. A receiver's {@code N/A} makes the value unknown again. */
public final class ZoneState {
	private static final Logger log = LoggerFactory.getLogger(ZoneState.class);

	private static final String NOT_AVAILABLE = "N/A";

	private final Zone zone;
	private volatile Boolean power;
	private volatile Integer volume;
	private volatile Boolean muted;
	private volatile String input;

	ZoneState(Zone zone) {
		this.zone = zone;
	}

	void received(String command, String parameter) {
		String value = NOT_AVAILABLE.equals(parameter) ? null : parameter;
		if (command.equals(zone.powerCommand())) {
			power = value == null ? null : "01".equals(value);
		} else if (command.equals(zone.muteCommand())) {
			muted = value == null ? null : "01".equals(value);
		} else if (command.equals(zone.inputCommand())) {
			input = value;
		} else if (command.equals(zone.volumeCommand())) {
			receivedVolume(value);
		}
	}

	private void receivedVolume(String value) {
		try {
			volume = value == null ? null : Integer.valueOf(value, 16);
		} catch (NumberFormatException ex) {
			log.debug("ignore volume {} of {}", value, zone);
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

	/** @return input code as for {@code SLI}, {@code 80} for "the main zone's source"; {@code null} while unknown */
	public String getInput() {
		return input;
	}
}
