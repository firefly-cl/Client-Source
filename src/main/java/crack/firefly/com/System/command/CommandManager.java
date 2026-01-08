package crack.firefly.com.System.command;

import java.util.ArrayList;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.command.impl.ScreenshotCommand;
import crack.firefly.com.System.command.impl.TranslateCommand;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventSendChat;

public class CommandManager {

	private ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager() {
		
		commands.add(new ScreenshotCommand());
		commands.add(new TranslateCommand());
		
		Firefly.getInstance().getEventManager().register(this);
	}
	
	@EventTarget
	public void onSendChat(EventSendChat event) {
		
		if(event.getMessage().startsWith(".soarcmd")) {
			
			event.setCancelled(true);
			
			String[] args = event.getMessage().split(" ");
			
			if(args.length > 1) {
				for(Command c : commands) {
					if(args[1].equals(c.getPrefix())) {
						c.onCommand(event.getMessage().replace(".soarcmd ", "").replace(args[1] + " ", ""));
					}
				}
			}
		}
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
