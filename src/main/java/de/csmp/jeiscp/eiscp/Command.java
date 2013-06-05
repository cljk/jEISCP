package de.csmp.jeiscp.eiscp;



public class Command {


	String command;
	String name;
    String description;
    
    String models;
    
    CommandBlock commandBlock;
    


    
    
	public Command() {
		super();
	}

	public Command(String command, String name, String description,
			String[] models, CommandBlock commandBlock) {
		super();
		this.command = command;
		this.name = name;
		this.description = description;
		//this.models = models;FIXME
		this.commandBlock = commandBlock;
		
		commandBlock.getValues().add(this);
	}

	public Command(String command, String name, String description,
			String models, CommandBlock commandBlock) {
		super();
		this.command = command;
		this.name = name;
		this.description = description;
		this.models = models;
		this.commandBlock = commandBlock;
		
		commandBlock.getValues().add(this);
	}

    
	@Override
	public String toString() {
		return getIscpCommand() + " - " + getIdentifier() + " (" + description + ")";
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
