package crack.firefly.com.System.quickplay.impl;

import java.util.ArrayList;

import crack.firefly.com.System.quickplay.QuickPlay;
import crack.firefly.com.System.quickplay.QuickPlayCommand;
import net.minecraft.util.ResourceLocation;

public class SkywarsQuickPlay extends QuickPlay {

	public SkywarsQuickPlay() {
		super("Skywars", new ResourceLocation("soar/icons/hypixel/Skywars.png"));
	}

	@Override
	public void addCommands() {
		
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/l s"));
		commands.add(new QuickPlayCommand("Solo Normal", "/play solo_normal"));
		commands.add(new QuickPlayCommand("Solo Insane", "/play solo_insane"));
		commands.add(new QuickPlayCommand("Teams Normal", "/play teams_normal"));
		commands.add(new QuickPlayCommand("Teams Insane", "/play teams_insane"));
		commands.add(new QuickPlayCommand("Mega", "/play mega_normal"));
		
		this.setCommands(commands);
	}
}
