package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventText;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.TextSetting;

public class NameProtectMod extends Mod {

	private TextSetting nameSetting = new TextSetting(TranslateText.NAME, this, "You");
	
	public NameProtectMod() {
		super(TranslateText.NAME_PROTECT, TranslateText.NAME_PROTECT_DESCRIPTION, ModCategory.PLAYER, "nickhider");
	}
	
	@EventTarget
	public void onText(EventText event) {
		event.replace(mc.getSession().getUsername(), nameSetting.getText());
	}
}
