package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventSendChat extends Event {

	private String message;
	
	public EventSendChat(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
