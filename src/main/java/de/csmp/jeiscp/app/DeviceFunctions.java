package de.csmp.jeiscp.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.CommandBlock;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

/**
 * Commands a receiver model supports according to the YAML catalogue, reduced to values that can be sent as they are.
 * The catalogue's model sets are coarse, so this may offer functions the device lacks.
 */
public final class DeviceFunctions {

	/** Literal values like {@code 01}, {@code UP} or {@code P/P}; placeholders such as {@code nnnnn} or {@code B{xx}} are left out. */
	private static final Pattern ACTION = Pattern.compile("[A-Z0-9/]+");
	private static final String QUERY = "QSTN";
	private static final Set<String> ACRONYMS = Set.of("am", "arc", "av", "bd", "cd", "cec", "dlna", "dsd", "dsx", "dts", "dvd", "eq",
			"ex", "fm", "hd", "hdmi", "ipv", "isf", "lfe", "mcacc", "net", "osd", "pc", "pcm", "pqls", "rds", "thx", "tv", "usb", "xm");

	public record Function(CommandBlock block, List<Command> actions, boolean queryable) {
		public String label() {
			return prettify(block.getName());
		}

		public String prefix() {
			return block.getCommand();
		}
	}

	private DeviceFunctions() {
	}

	/** @param device {@code null} if the model is unknown; then every function is offered */
	public static List<Function> forDevice(EiscpDevice device) {
		List<Function> functions = new ArrayList<>();
		for (CommandBlock block : EiscpCommandsParser.getMainCommandBlocks()) {
			List<Command> actions = new ArrayList<>();
			boolean queryable = false;
			for (Command cmd : block.getValues()) {
				if (device != null && !device.isCapable(cmd)) {
					continue;
				}
				if (QUERY.equals(cmd.getCommand())) {
					queryable = true;
				} else if (ACTION.matcher(cmd.getCommand()).matches()) {
					actions.add(cmd);
				}
			}
			if (!actions.isEmpty()) {
				actions.sort(Comparator.comparing(Command::getCommand));
				functions.add(new Function(block, List.copyOf(actions), queryable));
			}
		}
		functions.sort(Comparator.comparing(Function::label).thenComparing(Function::prefix));
		return List.copyOf(functions);
	}

	/** Label for a value key: its catalogue name, or the raw value if it has none. */
	public static String actionLabel(Command cmd) {
		return cmd.getName() != null ? prettify(cmd.getName()) : cmd.getCommand();
	}

	/** {@code listening-mode} becomes {@code Listening mode}, {@code tv-logic} becomes {@code TV logic}. */
	public static String prettify(String id) {
		if (id == null || id.isEmpty()) {
			return "";
		}
		String[] words = id.split("-");
		for (int i = 0; i < words.length; i++) {
			if (ACRONYMS.contains(words[i])) {
				words[i] = words[i].toUpperCase(Locale.ROOT);
			} else if (i == 0 && !words[i].isEmpty()) {
				words[i] = Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1);
			}
		}
		return String.join(" ", words);
	}

	/** Readable current value of a block, e.g. {@code Stereo} for {@code LMD} and {@code 0C}; the raw value if unknown. */
	public static String describeValue(String prefix, String value) {
		if (value == null) {
			return null;
		}
		Command cmd = EiscpCommandsParser.getCommandByIscp(prefix + value);
		return cmd != null && cmd.getName() != null ? prettify(cmd.getName()) : value;
	}
}
