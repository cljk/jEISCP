package de.csmp.jeiscp.app;

import java.util.Arrays;
import java.util.List;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

/** The additional zones of a receiver with the 3-letter commands that differ per zone. */
public enum Zone {
	ZONE2("zone2", "Zone 2", "ZPW", "ZVL", "ZMT", "SLZ"),
	ZONE3("zone3", "Zone 3", "PW3", "VL3", "MT3", "SL3"),
	ZONE4("zone4", "Zone 4", "PW4", "VL4", "MT4", "SL4");

	private final String catalogueName;
	private final String label;
	private final String powerCommand;
	private final String volumeCommand;
	private final String muteCommand;
	private final String inputCommand;

	Zone(String catalogueName, String label, String powerCommand, String volumeCommand, String muteCommand, String inputCommand) {
		this.catalogueName = catalogueName;
		this.label = label;
		this.powerCommand = powerCommand;
		this.volumeCommand = volumeCommand;
		this.muteCommand = muteCommand;
		this.inputCommand = inputCommand;
	}

	/** @return its name in the YAML catalogue, for {@link EiscpCommandsParser#getCommandBlocks(String)} */
	public String catalogueName() {
		return catalogueName;
	}

	public String label() {
		return label;
	}

	public String powerCommand() {
		return powerCommand;
	}

	public String volumeCommand() {
		return volumeCommand;
	}

	public String muteCommand() {
		return muteCommand;
	}

	public String inputCommand() {
		return inputCommand;
	}

	/** @return the zone a 3-letter command belongs to, {@code null} if it is none of the four above */
	static Zone ofCommand(String command) {
		for (Zone zone : values()) {
			if (command.equals(zone.powerCommand) || command.equals(zone.volumeCommand)
					|| command.equals(zone.muteCommand) || command.equals(zone.inputCommand)) {
				return zone;
			}
		}
		return null;
	}

	/**
	 * Zones of a model according to the YAML catalogue, whose model sets are coarse: a listed zone may not be wired up.
	 * @param device {@code null} if the model is unknown; then all zones are offered
	 */
	public static List<Zone> supportedBy(EiscpDevice device) {
		return Arrays.stream(values())
				.filter(zone -> device == null || device.isCapable(EiscpCommandsParser.getCommandByIscp(zone.powerCommand + "QSTN")))
				.toList();
	}
}
