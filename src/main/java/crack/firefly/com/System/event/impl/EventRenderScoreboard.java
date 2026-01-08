package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;
import net.minecraft.scoreboard.ScoreObjective;

public class EventRenderScoreboard extends Event {

	private ScoreObjective objective;
	
	public EventRenderScoreboard(ScoreObjective objective) {
		this.objective = objective;
	}

	public ScoreObjective getObjective() {
		return objective;
	}
}