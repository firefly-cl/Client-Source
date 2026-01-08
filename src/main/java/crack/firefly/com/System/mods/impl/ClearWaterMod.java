package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventWaterOverlay;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class ClearWaterMod extends Mod {
	
	public ClearWaterMod() {
		super(TranslateText.CLEAR_WATER, TranslateText.CLEAR_WATER_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onWaterOverlay(EventWaterOverlay event) {
		event.setCancelled(true);
	}
}
