package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventClickMouse extends Event {

	private int button;
	
	public EventClickMouse(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}
}
