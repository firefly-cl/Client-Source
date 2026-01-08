package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.PlayerUtils;
import net.minecraft.potion.Potion;

public class PotionCounterMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public PotionCounterMod() {
		super(TranslateText.POTION_COUNTER, TranslateText.POTION_COUNTER_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		int amount = PlayerUtils.getPotionsFromInventory(Potion.heal);
		
		return amount + " " + (amount <= 1 ? "pot" : "pots");
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.ARCHIVE : null;
	}
}
