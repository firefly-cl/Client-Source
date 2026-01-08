package crack.firefly.com.System.quickplay;

public class QuickPlayCommand {

	private String name;
	private String command;
	
	public QuickPlayCommand(String name, String command) {
		this.name = name;
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public String getCommand() {
		return command;
	}
}
