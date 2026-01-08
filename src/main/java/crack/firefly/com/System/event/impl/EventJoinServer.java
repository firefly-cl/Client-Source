package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;

public class EventJoinServer extends Event {

	private String ip;
	
	public EventJoinServer(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
}
