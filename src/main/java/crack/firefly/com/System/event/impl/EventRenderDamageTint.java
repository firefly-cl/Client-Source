package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRenderDamageTint extends Event {

	private float partialTicks;
	
	public EventRenderDamageTint(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
