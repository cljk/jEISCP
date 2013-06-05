package de.csmp.jeiscp.eiscp;

import java.util.HashSet;
import java.util.Set;

public class CommandBlock {

	String command;
	String name;
    String description;
    
    Set<Command> values = new HashSet<Command>();
    
    public CommandBlock() {
    	
    }
    
    public CommandBlock(String command, String name, String description) {
		super();
		this.command = command;
		this.name = name;
		this.description = description;
	}
    
    public CommandBlock(String command, String name, String description,
			Set<Command> values) {
		super();
		this.command = command;
		this.name = name;
		this.description = description;
		this.values = values;
	}

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

	public Set<Command> getValues() {
		return values;
	}

	public void setValues(Set<Command> values) {
		this.values = values;
	}

}
