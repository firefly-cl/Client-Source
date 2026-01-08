package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;

public class PlayTimeDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public PlayTimeDisplayMod() {
		super(TranslateText.PLAY_TIME_DISPLAY, TranslateText.PLAY_TIME_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		
		int sec = (int) ((System.currentTimeMillis() - Firefly.getInstance().getLaunchTime()) / 1000);
		int min = (sec % 3600) / 60;
		int hour = sec / 3600;
		sec = sec % 60;
		
		return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.CLOCK : null;
	}
}
