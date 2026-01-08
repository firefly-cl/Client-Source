package crack.firefly.com.System.quickplay;

import java.util.ArrayList;

import crack.firefly.com.System.quickplay.impl.ArcadeQuickPlay;
import crack.firefly.com.System.quickplay.impl.BedwarsQuickPlay;
import crack.firefly.com.System.quickplay.impl.DuelsQuickPlay;
import crack.firefly.com.System.quickplay.impl.MainLobbyQuickPlay;
import crack.firefly.com.System.quickplay.impl.MurderMysteryQuickPlay;
import crack.firefly.com.System.quickplay.impl.SkywarsQuickPlay;
import crack.firefly.com.System.quickplay.impl.TNTQuickPlay;
import crack.firefly.com.System.quickplay.impl.UHCQuickPlay;

public class QuickPlayManager {

	private ArrayList<QuickPlay> quickPlays = new ArrayList<QuickPlay>();
	
	public QuickPlayManager() {
		quickPlays.add(new ArcadeQuickPlay());
		quickPlays.add(new BedwarsQuickPlay());
		quickPlays.add(new DuelsQuickPlay());
		quickPlays.add(new MainLobbyQuickPlay());
		quickPlays.add(new MurderMysteryQuickPlay());
		quickPlays.add(new SkywarsQuickPlay());
		quickPlays.add(new TNTQuickPlay());
		quickPlays.add(new UHCQuickPlay());
	}

	public ArrayList<QuickPlay> getQuickPlays() {
		return quickPlays;
	}
}
