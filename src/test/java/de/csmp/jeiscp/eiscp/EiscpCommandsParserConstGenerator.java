package de.csmp.jeiscp.eiscp;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class EiscpCommandsParserConstGenerator {

	@Test
	public void generateConstFile() throws Exception {
		String fileName = "target/EiscpCommmandsConstants.java";
		
		FileWriter fw = new FileWriter(new File(fileName));
		fw.write("package " + this.getClass().getPackage().getName() + ";\n\n\n");
		fw.write("/** GENERATED FILE - do not modify */\n");
		fw.write("public class EiscpCommmandsConstants {\n\n");
		
		EiscpCommandsParser parser = new EiscpCommandsParser();
		List<CommandBlock> mainCmdBlock = parser.getMainCommandBlocks();
		
		Map<String, Command> cmds = EiscpCommandsParser.getMainCommands();
		Set<String> sortedIds = new TreeSet<String>(cmds.keySet());
		
		for (String id : sortedIds) {
			Command cmd = cmds.get(id);
			
			fw.write("/**\n");
			fw.write(" * ISCP Command: " + cmd.getIscpCommand() + "\n");
			fw.write(" * " + cmd.getDescription() + "\n */\n");
			
			String constId = id.replaceAll("-", "_").replaceAll("::", "_");
			if (constId.startsWith("1")) {
				constId = "N" + constId;
			}
			constId = constId.toUpperCase();
			fw.write("public static final String " + constId + " = \"" + id + "\";\n");
			
			fw.write("\n");
		}
		fw.write("}");
		fw.close();
		System.out.println("wrote to " + fileName);
		
	}

}
