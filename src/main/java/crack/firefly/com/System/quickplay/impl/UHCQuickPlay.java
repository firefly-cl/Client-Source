package crack.firefly.com.System.quickplay.impl;

import java.util.ArrayList;

import crack.firefly.com.System.quickplay.QuickPlay;
import crack.firefly.com.System.quickplay.QuickPlayCommand;
import net.minecraft.util.ResourceLocation;

public class UHCQuickPlay extends QuickPlay{

	public UHCQuickPlay() {
		super("UHC", new ResourceLocation("soar/icons/hypixel/UHC.png"));
	}

	@Override
	public void addCommands() {
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/l hc"));
		commands.add(new QuickPlayCommand("Solo", "/play uhc_solo"));
		commands.add(new QuickPlayCommand("Teams", "/play uhc_teams"));
		commands.add(new QuickPlayCommand("Events Mode", "/play /play uhc_events"));
		
		this.setCommands(commands);
	}
}