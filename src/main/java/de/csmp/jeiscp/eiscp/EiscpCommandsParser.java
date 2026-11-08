package de.csmp.jeiscp.eiscp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * Command catalogue from the YAML file of https://github.com/miracle2k/onkyo-eiscp (submodule).
 * It is parsed once on first use (class initialization makes that thread-safe); all maps and lists are unmodifiable.
 */
public class EiscpCommandsParser {
	private static final String YAML_MODELS = "models";
	private static final String YAML_DESCRIPTION = "description";
	private static final String YAML_NAME = "name";
	private static final String YAML_VALUES = "values";

	private static final String EISCP_COMMANDS_YAML = "miracle2k-onkyo-eiscp/eiscp-commands.yaml";
	private static final List<String> MAIN_ZONES = List.of("main", "dock");
	private static final List<String> OTHER_ZONES = List.of("zone2", "zone3", "zone4");

	private static final Logger log = LoggerFactory.getLogger(EiscpCommandsParser.class);

	private static final Map<String, List<String>> keysetForModel = new ConcurrentHashMap<>();

	private record Catalog(Map<String, Object> yaml, List<CommandBlock> blocks, Map<String, List<CommandBlock>> blocksByZone,
			Map<String, Command> byId, Map<String, CommandBlock> blockById, Map<String, Command> byIscp,
			Map<String, List<String>> modelsets) {
	}

	/** Holder idiom: loaded when first accessed, exactly once. */
	private static final class Holder {
		static final Catalog CATALOG = load();
	}

	@SuppressWarnings("unchecked")
	private static Catalog load() {
		Map<String, Object> yaml;
		try (InputStream is = EiscpCommandsParser.class.getClassLoader().getResourceAsStream(EISCP_COMMANDS_YAML)) {
			yaml = (Map<String, Object>) new Yaml().load(is);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		log.debug("parsed YAML @ " + EISCP_COMMANDS_YAML);

		Map<String, List<CommandBlock>> blocksByZone = new LinkedHashMap<>();
		for (List<String> zones : List.of(MAIN_ZONES, OTHER_ZONES)) {
			for (String zone : zones) {
				blocksByZone.put(zone, parseZone((Map<String, Object>) yaml.get(zone)));
			}
		}
		List<CommandBlock> blocks = new ArrayList<>();
		for (String zone : MAIN_ZONES) {
			blocks.addAll(blocksByZone.get(zone));
		}

		// names are not unique (e.g. NLS, NLA and NLU are all "net-usb-list-info"): the first block or command wins
		Map<String, Command> byId = new LinkedHashMap<>();
		Map<String, CommandBlock> blockById = new LinkedHashMap<>();
		Map<String, Command> byIscp = new LinkedHashMap<>();
		for (CommandBlock block : blocks) {
			blockById.putIfAbsent(block.getName(), block);
			for (Command cmd : block.getValues()) {
				byId.putIfAbsent(cmd.getIdentifier(), cmd);
				byIscp.put(cmd.getIscpCommand(), cmd);
			}
		}

		// the other zones repeat names like "power" and prefixes like NTC or TUN: they only add what the main zones lack
		for (String zone : OTHER_ZONES) {
			for (CommandBlock block : blocksByZone.get(zone)) {
				for (Command cmd : block.getValues()) {
					byIscp.putIfAbsent(cmd.getIscpCommand(), cmd);
				}
			}
		}

		return new Catalog(Collections.unmodifiableMap(yaml), List.copyOf(blocks), Collections.unmodifiableMap(blocksByZone),
				Collections.unmodifiableMap(byId), Collections.unmodifiableMap(blockById), Collections.unmodifiableMap(byIscp),
				modelsets((Map<String, List<String>>) yaml.get("modelsets")));
	}

	@SuppressWarnings("unchecked")
	private static List<CommandBlock> parseZone(Map<String, Object> commands) {
		List<CommandBlock> blocks = new ArrayList<>();
		if (commands != null) {
			for (Map.Entry<String, Object> command : commands.entrySet()) {
				blocks.add(parseCommandBlock(command.getKey(), (Map<String, Object>) command.getValue()));
			}
		}
		return List.copyOf(blocks);
	}

	/** Also lists "TX-NR609" for an entry "TX-NR609(Ether)", so device types from autodiscovery match. */
	private static Map<String, List<String>> modelsets(Map<String, List<String>> yamlModelsets) {
		log.debug("loaded " + yamlModelsets.size() + " modelsets");
		Map<String, List<String>> result = new LinkedHashMap<>();
		for (Map.Entry<String, List<String>> set : yamlModelsets.entrySet()) {
			List<String> models = new ArrayList<>(set.getValue());
			for (String model : set.getValue()) {
				int cDelim = model.indexOf('(');
				if (cDelim != -1) {
					String derivedModelName = model.substring(0, cDelim);
					if (!models.contains(derivedModelName)) {
						models.add(derivedModelName);
					}
				}
			}
			result.put(set.getKey(), List.copyOf(models));
		}
		return Collections.unmodifiableMap(result);
	}

	@SuppressWarnings("unchecked")
	private static CommandBlock parseCommandBlock(String command, Map<String, Object> cmdMap) {
		CommandBlock cmdBlock = new CommandBlock();
		cmdBlock.setCommand(command);
		cmdBlock.setName(firstName(cmdMap.get(YAML_NAME)));
		cmdBlock.setDescription((String) cmdMap.get(YAML_DESCRIPTION));

		Map<Object, Map<String, Object>> values = (Map<Object, Map<String, Object>>) cmdMap.get(YAML_VALUES);
		for (Map.Entry<Object, Map<String, Object>> value : values.entrySet()) {
			if (value.getKey() instanceof String) {
				Command subC = new Command(cmdBlock);
				subC.setCommand((String) value.getKey());
				subC.setName(firstName(value.getValue().get(YAML_NAME)));
				subC.setDescription((String) value.getValue().get(YAML_DESCRIPTION));
				subC.setModels((String) value.getValue().get(YAML_MODELS));
				cmdBlock.getValues().add(subC);
			} else if (log.isDebugEnabled()) {
				// value ranges such as [0, 100] are keyed by lists
				log.debug("subCommandKey unhandeled: " + value.getKey().getClass().getName() + " - " + value.getKey() + " for " + cmdBlock);
			}
		}
		cmdBlock.setValues(Collections.unmodifiableSet(cmdBlock.getValues()));
		return cmdBlock;
	}

	/** YAML names are a string or a list of alternative names. */
	private static String firstName(Object name) {
		return name instanceof List ? (String) ((List<?>) name).get(0) : (String) name;
	}

	public static Map<String, Command> getIscpToCommandMap() {
		return Holder.CATALOG.byIscp();
	}

	public static Map<String, Command> getIdToCommandMap() {
		return Holder.CATALOG.byId();
	}

	/** Blocks sharing a name (e.g. NLS, NLA, NLU are all "net-usb-list-info") resolve to the first one in the YAML. */
	public static Map<String, CommandBlock> getIdToCommandBlockMap() {
		return Holder.CATALOG.blockById();
	}

	public static Command getCommand(String commandId) {
		return getIdToCommandMap().get(commandId);
	}

	public static Command getCommandByIscp(String command) {
		return getIscpToCommandMap().get(command);
	}

	public static String getIscpCommand(String commandId) {
		return getCommand(commandId).getIscpCommand();
	}

	/** Commands of the main zone plus the "dock" zone, where upstream keeps the NET/USB commands (NTC, NLS, NJA, ...). */
	public static List<CommandBlock> getMainCommandBlocks() {
		return Holder.CATALOG.blocks();
	}

	/**
	 * Commands of one zone of the YAML. Also {@code zone2} to {@code zone4}, which share only the lookup by ISCP string
	 * with {@link #getMainCommandBlocks()}; they repeat some main commands such as {@code NTC}.
	 * @param zone {@code main}, {@code dock}, {@code zone2}, {@code zone3} or {@code zone4}
	 * @return empty for any other name
	 */
	public static List<CommandBlock> getCommandBlocks(String zone) {
		return Holder.CATALOG.blocksByZone().getOrDefault(zone, List.of());
	}

	/** The raw YAML; only its top level is unmodifiable. */
	public static Map<String, Object> getEiscpCommands() {
		return Holder.CATALOG.yaml();
	}

	public static Map<String, List<String>> getModelsets() {
		return Holder.CATALOG.modelsets();
	}

	public static List<String> getKeysetForModel(String model) {
		return keysetForModel.computeIfAbsent(model, m -> {
			log.debug("build keyset for model " + m);
			List<String> keysets = new ArrayList<>();
			getModelsets().forEach((keyset, models) -> {
				if (models.contains(m)) {
					keysets.add(keyset);
				}
			});
			return List.copyOf(keysets);
		});
	}
}
