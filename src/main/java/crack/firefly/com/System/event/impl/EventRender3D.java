package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRender3D extends Event {
    private float partialTicks;
	
    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}