package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventAttackEntity;
import crack.firefly.com.System.event.impl.EventDamageEntity;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;

public class ComboCounterMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	private long hitTime = -1;
	private int combo, possibleTarget;
	
	public ComboCounterMod() {
		super(TranslateText.COMBO_COUNTER, TranslateText.COMBO_COUNTER_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if((System.currentTimeMillis() - hitTime) > 2000) {
			combo = 0;
		}
	}
	
	@EventTarget
	public void onAttackEntity(EventAttackEntity event) {
		possibleTarget = event.getEntity().getEntityId();
	}
	
	@EventTarget
	public void onDamageEntity(EventDamageEntity event) {
		if(event.getEntity().getEntityId() == possibleTarget) {
			combo++;
			possibleTarget = -1;
			hitTime = System.currentTimeMillis();
		} else if(event.getEntity() == mc.thePlayer) {
			combo = 0;
		}
	}
	
	@Override
	public String getText() {
		if(combo == 0) {
			return "No Combo";
		}else {
			return combo + " Combo";
		}
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.BAR_CHERT : null;
	}
}
