package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRender2D extends Event {
	
	private float partialTicks;
	
	public EventRender2D(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
