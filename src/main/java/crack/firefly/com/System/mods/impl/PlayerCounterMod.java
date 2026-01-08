package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;

public class PlayerCounterMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public PlayerCounterMod() {
		super(TranslateText.PLAYER_COUNTER, TranslateText.PLAYER_COUNTER_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		return "Player: " + mc.thePlayer.sendQueue.getPlayerInfoMap().size();
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.USERS : null;
	}
}
