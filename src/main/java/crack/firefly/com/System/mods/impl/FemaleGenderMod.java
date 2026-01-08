package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class FemaleGenderMod extends Mod {

	private static FemaleGenderMod instance;

	public FemaleGenderMod() {
		super(TranslateText.FEMALE_GENDER, TranslateText.FEMALE_GENDER_DESCRIPTION, ModCategory.PLAYER, "boobs");

		instance = this;
	}

	public static FemaleGenderMod getInstance() {
		return instance;
	}
}
