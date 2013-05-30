package de.csmp.jeiscp.eiscp;


public class Command {


	String command;
	String name;
    String description;
    
    String models;
    
    CommandBlock commandBlock;
    

	@Override
	public String toString() {
		return getIscpCommand() + " - " + getIdentifier() + " (" + description + ")";
	}

	public String getIdentifier() {
		return commandBlock.getName() + "::" + name;
	}
	
	public String getIscpCommand() {
		return commandBlock.getCommand() + command;
	}
	
	
    public Command(CommandBlock commandBlock) {
    	this.commandBlock = commandBlock;
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

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}


	public CommandBlock getCommandBlock() {
		return commandBlock;
	}


	public void setCommandBlock(CommandBlock commandBlock) {
		this.commandBlock = commandBlock;
	}

}
