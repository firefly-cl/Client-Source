package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class FPSSpooferMod extends Mod {

	private static FPSSpooferMod instance;
	
	private NumberSetting multiplierSetting = new NumberSetting(TranslateText.MULTIPLIER, this, 2, 1, 30, true);
	
	public FPSSpooferMod() {
		super(TranslateText.FPS_SPOOFER, TranslateText.FPS_SPOOFER_DESCRIPTION, ModCategory.OTHER, "fake");
		
		instance = this;
	}

	public static FPSSpooferMod getInstance() {
		return instance;
	}

	public NumberSetting getMultiplierSetting() {
		return multiplierSetting;
	}
}
