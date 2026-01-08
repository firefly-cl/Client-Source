package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class ShinyPotsMod extends Mod {

	private static ShinyPotsMod instance;
	
	public ShinyPotsMod() {
		super(TranslateText.SHINY_POTS, TranslateText.SHINY_POTS_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	public static ShinyPotsMod getInstance() {
		return instance;
	}
}
