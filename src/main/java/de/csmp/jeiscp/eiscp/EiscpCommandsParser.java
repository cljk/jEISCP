package de.csmp.jeiscp.eiscp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * parses the YAML-file of git-project
 * miracle2k-onkyo-eiscp
 * 
 * @see https://github.com/miracle2k/onkyo-eiscp
 * 
 * @author marcelpokrandt
 *
 */
public class EiscpCommandsParser {
	private static final String YAML_MODELS = "models";
	private static final String YAML_DESCRIPTION = "description";
	private static final String YAML_NAME = "name";
	private static final String YAML_VALUES = "values";

	private static final String EISCP_COMMANDS_YAML = "miracle2k-onkyo-eiscp/eiscp-commands.yaml";
	private static final List<String> ZONES = List.of("main", "dock");

	private static final Logger log = LoggerFactory.getLogger(EiscpCommandsParser.class);

	private static Map<String, List<String>> modelsets = null;
	private static Map<String, Object> ec = null;
	
	private static List<CommandBlock> mainCommandBlocks = null;
	private static Map<String, Command> idToCommandMap = null;
	private static Map<String, CommandBlock> idToCommandBlockMap = null;
	private static Map<String, Command> iscpToCommandMap = null;
	// private static Map<String, CommandBlock> iscpToCommandBlockMap = null;
	
	private static Map<String, List<String>> keysetForModel = new HashMap<String, List<String>>();
	
	

	public static Map<String, Command> getIscpToCommandMap() {
		if (iscpToCommandMap == null) {
			HashMap<String, Command> map = new HashMap<String, Command>();
			for (CommandBlock cmdBlock : getMainCommandBlocks()) {
				for (Command cmd : cmdBlock.getValues()) {
					map.put(cmd.getIscpCommand(), cmd);
				}
			}
			iscpToCommandMap = map;
		}
		return iscpToCommandMap;
	}
	public static Map<String, Command> getIdToCommandMap() {
		if (idToCommandMap == null) {
			idToCommandMap = new HashMap<String, Command>();
			for (CommandBlock cmdBlock : getMainCommandBlocks()) {
				for (Command cmd : cmdBlock.getValues()) {
					idToCommandMap.putIfAbsent(cmd.getIdentifier(), cmd);
				}
			}
		}
		return idToCommandMap;
	}

	/** Blocks sharing a name (e.g. NLS, NLA, NLU are all "net-usb-list-info") resolve to the first one in the YAML. */
	public static Map<String, CommandBlock> getIdToCommandBlockMap() {
		if (idToCommandBlockMap == null) {
			idToCommandBlockMap = new HashMap<String, CommandBlock>();
			for (CommandBlock cmdBlock : getMainCommandBlocks()) {
				idToCommandBlockMap.putIfAbsent(cmdBlock.getName(), cmdBlock);
			}
		}
		return idToCommandBlockMap;
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
		if (mainCommandBlocks == null) {
			List<CommandBlock> mc = new LinkedList<CommandBlock>();
			for (String zone : ZONES) {
				Map<String, Object> commands = (Map<String, Object>) getEiscpCommands().get(zone);
				if (commands != null) {
					for (String command : commands.keySet()) {
						mc.add(parseCommandBlock(command, (Map<String, Object>) commands.get(command)));
					}
				}
			}
			mainCommandBlocks = mc;
		}
		return mainCommandBlocks;
	}

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
		return cmdBlock;
	}

	/** YAML names are a string or a list of alternative names. */
	private static String firstName(Object name) {
		return name instanceof List ? (String) ((List<?>) name).get(0) : (String) name;
	}
	
	public static Map<String, Object> getEiscpCommands() {
		if (ec == null) {
			try (InputStream is = EiscpCommandsParser.class.getClassLoader().getResourceAsStream(EISCP_COMMANDS_YAML)) {
				Yaml y = new Yaml();
				ec = (Map<String, Object>) y.load(is);
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
			log.debug("parsed YAML @ " + EISCP_COMMANDS_YAML);
		}
		return ec;
	}
	
	public static Map<String, List<String>> getModelsets() {
		if (modelsets == null) {
			Map<String, List<String>> yamlModelsets = (Map<String, List<String>>) getEiscpCommands().get("modelsets");
			log.debug("loaded " + yamlModelsets.size() + " modelsets");

			// rework loaded modelset
			for (String setId : yamlModelsets.keySet()) {
				List<String> modelset = yamlModelsets.get(setId);
				List<String> reworkedModelset = new LinkedList<String>(modelset);
				
				
				for (String model : modelset) {
					int cDelim = model.indexOf("(");
					if (cDelim != -1) {
						String derivedModelName = model.substring(0, cDelim);
						if (! modelset.contains(derivedModelName)) {
							log.debug("assign " + model + " as equal to " + derivedModelName);
							reworkedModelset.add(derivedModelName);
						}
					}
				}
				
				yamlModelsets.put(setId, reworkedModelset);
			}
			
			modelsets = yamlModelsets;
		}
		
		return modelsets;
	}
	
	public static List<String> getKeysetForModel(String model) {
		if (! keysetForModel.containsKey(model)) {
			log.debug("build keyset for model " + model);
			
			List<String> keysetList = new LinkedList<String>();
			Map<String, List<String>> modelsets = getModelsets();
			for (String keyset : modelsets.keySet()) {
				List<String> modelList = modelsets.get(keyset);
				if (modelList.contains(model)) keysetList.add(keyset);
			}
			
			keysetForModel.put(model, keysetList);
			return keysetList;
		}
		
		return keysetForModel.get(model);
	}
}
