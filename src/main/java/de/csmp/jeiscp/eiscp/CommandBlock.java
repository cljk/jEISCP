package de.csmp.jeiscp.eiscp;

import java.util.LinkedList;
import java.util.List;

public class CommandBlock {

	String command;
	String name;
    String description;
    
    List<Command> values = new LinkedList<Command>();
    
    @Override
	public String toString() {
		return command + " - " + name + " (" + description + ")";
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Command> getValues() {
		return values;
	}

	public void setValues(List<Command> values) {
		this.values = values;
	}

}
