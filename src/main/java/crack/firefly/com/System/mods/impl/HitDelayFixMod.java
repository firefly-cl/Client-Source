package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class HitDelayFixMod extends Mod {

	private static HitDelayFixMod instance;
	
	public HitDelayFixMod() {
		super(TranslateText.HIT_DELAY_FIX, TranslateText.HIT_DELAY_FIX_DESCRIPTION, ModCategory.PLAYER, "nodelay", true);
		
		instance = this;
	}

	public static HitDelayFixMod getInstance() {
		return instance;
	}
}
