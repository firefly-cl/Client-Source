package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRenderVisualizer extends Event {

	private float partialTicks;
	
	public EventRenderVisualizer(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
