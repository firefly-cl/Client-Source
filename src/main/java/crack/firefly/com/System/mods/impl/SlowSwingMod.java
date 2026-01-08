package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class SlowSwingMod extends Mod {

	private static SlowSwingMod instance;
	
	private NumberSetting delaySetting = new NumberSetting(TranslateText.DELAY, this, 14, 2, 20, true);
	
	public SlowSwingMod() {
		super(TranslateText.SLOW_SWING, TranslateText.SLOW_SWING_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static SlowSwingMod getInstance() {
		return instance;
	}

	public NumberSetting getDelaySetting() {
		return delaySetting;
	}
}
