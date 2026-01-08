package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.notification.NotificationType;

public class Items2DMod extends Mod {

	private static Items2DMod instance;
	
	public Items2DMod() {
		super(TranslateText.ITEMS_2D, TranslateText.ITEMS_2D_DESCRIPTION, ModCategory.RENDER);
		
		instance = this;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		if(ItemPhysicsMod.getInstance().isToggled()) {
			ItemPhysicsMod.getInstance().setToggled(false);
			Firefly.getInstance().getNotificationManager().post(TranslateText.ITEM_PHYSICS.getText(),  "Disabled due to incompatibility" , NotificationType.WARNING);
		}
	}
	
	public static Items2DMod getInstance() {
		return instance;
	}
}
