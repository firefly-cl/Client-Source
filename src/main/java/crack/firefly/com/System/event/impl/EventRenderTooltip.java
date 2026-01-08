package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRenderTooltip extends Event {

	private float partialTicks;
	
	public EventRenderTooltip(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
