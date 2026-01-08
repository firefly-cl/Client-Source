package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class ItemPhysicsMod extends Mod {

	private static ItemPhysicsMod instance;
	
	private NumberSetting speedSetting = new NumberSetting(TranslateText.SPEED, this, 1, 0.5, 4, false);
	
	public ItemPhysicsMod() {
		super(TranslateText.ITEM_PHYSICS, TranslateText.ITEM_PHYSICS_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		if(Items2DMod.getInstance().isToggled()) {
			Items2DMod.getInstance().setToggled(false);
		}
	}
	
	public static ItemPhysicsMod getInstance() {
		return instance;
	}

	public NumberSetting getSpeedSetting() {
		return speedSetting;
	}
}
