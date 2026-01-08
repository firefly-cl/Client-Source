package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class DamageTiltMod extends Mod {

	private static DamageTiltMod instance;
	
	public DamageTiltMod() {
		super(TranslateText.DAMAGE_TILT, TranslateText.DAMAGE_TILT_DESCRIPTION, ModCategory.PLAYER);
		
		instance = this;
	}

	public static DamageTiltMod getInstance() {
		return instance;
	}
}
