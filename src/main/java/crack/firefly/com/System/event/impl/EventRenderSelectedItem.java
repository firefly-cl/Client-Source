package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventRenderSelectedItem extends Event {

	private int color;
	
	public EventRenderSelectedItem(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
}