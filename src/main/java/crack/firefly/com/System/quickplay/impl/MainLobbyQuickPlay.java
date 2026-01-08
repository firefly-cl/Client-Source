package crack.firefly.com.System.quickplay.impl;

import java.util.ArrayList;

import crack.firefly.com.System.quickplay.QuickPlay;
import crack.firefly.com.System.quickplay.QuickPlayCommand;
import net.minecraft.util.ResourceLocation;

public class MainLobbyQuickPlay extends QuickPlay{

	public MainLobbyQuickPlay() {
		super("MainLobby", new ResourceLocation("soar/icons/hypixel/MainLobby.png"));
	}

	@Override
	public void addCommands(){
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/lobby main"));
		
		this.setCommands(commands);
	}
}