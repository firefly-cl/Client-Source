package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventScrollMouse extends Event {

	private int amount;
	
	public EventScrollMouse(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}