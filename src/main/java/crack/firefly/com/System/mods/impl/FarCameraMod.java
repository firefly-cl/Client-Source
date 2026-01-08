package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventCameraRotation;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class FarCameraMod extends Mod {

	private NumberSetting rangeSetting = new NumberSetting(TranslateText.RANGE, this, 15, 0, 50, true);
	
	public FarCameraMod() {
		super(TranslateText.FAR_CAMERA, TranslateText.FAR_CAMERA_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onCameraRotation(EventCameraRotation event) {
		event.setThirdPersonDistance(rangeSetting.getValueFloat());
	}
}
