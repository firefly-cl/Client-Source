package crack.firefly.com.System.security;

import java.util.ArrayList;

import crack.firefly.com.System.security.impl.DemoSecurity;
import crack.firefly.com.System.security.impl.ExplosionSecurity;
import crack.firefly.com.System.security.impl.Log4jSecurity;
import crack.firefly.com.System.security.impl.ParticleSecurity;
import crack.firefly.com.System.security.impl.ResourcePackSecurity;
import crack.firefly.com.System.security.impl.TeleportSecurity;

public class SecurityFeatureManager {

	private ArrayList<SecurityFeature> features = new ArrayList<SecurityFeature>();
	
	public SecurityFeatureManager() {
		features.add(new DemoSecurity());
		features.add(new ExplosionSecurity());
		features.add(new Log4jSecurity());
		features.add(new ParticleSecurity());
		features.add(new ResourcePackSecurity());
		features.add(new TeleportSecurity());
	}

	public ArrayList<SecurityFeature> getFeatures() {
		return features;
	}
}
