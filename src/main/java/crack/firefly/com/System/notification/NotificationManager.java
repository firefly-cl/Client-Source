package crack.firefly.com.System.notification;

import java.util.concurrent.LinkedBlockingQueue;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;

public class NotificationManager {

	private LinkedBlockingQueue<Notification> notifications = new LinkedBlockingQueue<Notification>();
	
	public NotificationManager() {
		Firefly.getInstance().getEventManager().register(new NotificationHandler(notifications));
	}
	
	public void post(TranslateText title, TranslateText message, NotificationType type) {
		notifications.add(new Notification(title, message, type));
	}
	public void post(String title, String message, NotificationType type) {
		notifications.add(new Notification(title, message, type));
	}
}
