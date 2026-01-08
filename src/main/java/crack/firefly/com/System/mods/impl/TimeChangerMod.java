package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class TimeChangerMod extends Mod {

	private static TimeChangerMod instance;
	
	private NumberSetting timeSetting = new NumberSetting(TranslateText.TIME, this, 12, 0, 24, false);
	
	public TimeChangerMod() {
		super(TranslateText.TIME_CHANGER, TranslateText.TIME_CHANGER_DESCRIPTION, ModCategory.WORLD);
		
		instance = this;
	}

	public static TimeChangerMod getInstance() {
		return instance;
	}

	public NumberSetting getTimeSetting() {
		return timeSetting;
	}

}
