package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.ServerUtils;

public class ServerIPDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public ServerIPDisplayMod() {
		super(TranslateText.SERVER_IP, TranslateText.SERVER_IP_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		return ServerUtils.getServerIP();
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.SERVER : null;
	}
}
