package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventFovUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.Support.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BowZoomMod extends Mod {

	private NumberSetting factorSetting = new NumberSetting(TranslateText.FACTOR, this, 5, 1, 15, true);
	
	public BowZoomMod() {
		super(TranslateText.BOW_ZOOM, TranslateText.BOW_ZOOM_DESCRIPTION, ModCategory.PLAYER);
	}

	@EventTarget
	public void onFovUpdate(EventFovUpdate event) {
		
		float base = 1.0F;
		EntityPlayer entity = event.getEntity();
		ItemStack item = entity.getItemInUse();
		int useDuration = entity.getItemInUseDuration();
		
		float bowFov = factorSetting.getValueFloat();
		
		if(item != null && item.getItem() == Items.bow) {
			int duration = (int) Math.min(useDuration, 20.0F);
			float modifier = PlayerUtils.MODIFIER_BY_TICK.get(duration);
			base-= modifier * bowFov;
			event.setFov(base);
		}
        mc.renderGlobal.setDisplayListEntitiesDirty();
	}
}
