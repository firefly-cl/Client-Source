package crack.firefly.com.System.security;

import crack.firefly.com.Firefly;

public class SecurityFeature {

	public SecurityFeature() {
		Firefly.getInstance().getEventManager().register(this);
	}
}
