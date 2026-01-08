package crack.firefly.com.System.notification;

import java.util.concurrent.LinkedBlockingQueue;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRenderNotification;

public class NotificationHandler {

	private LinkedBlockingQueue<Notification> notifications;
	private Notification currentNotification;
	
	public NotificationHandler(LinkedBlockingQueue<Notification> notifications) {
		this.notifications = notifications;
	}
	
	@EventTarget
	public void onRenderNotification(EventRenderNotification event) {
		
		if(currentNotification != null && !currentNotification.isShown()) {
			currentNotification = null;
		}
		
		if(currentNotification == null && !notifications.isEmpty()) {
			currentNotification = notifications.poll();
			currentNotification.show();
		}
		
		if(currentNotification != null) {
			currentNotification.draw();
		}
	}
}
