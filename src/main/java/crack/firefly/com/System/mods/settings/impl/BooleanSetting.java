package crack.firefly.com.System.mods.settings.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;

public class BooleanSetting extends Setting {

	private boolean defaultValue, toggled;
	
	public BooleanSetting(TranslateText text, Mod parent, boolean toggled) {
		super(text, parent);
		
		this.toggled = toggled;
		this.defaultValue = toggled;
		
		Firefly.getInstance().getModManager().addSettings(this);
	}
	
	@Override
	public void reset() {
		this.toggled = defaultValue;
	}
	
	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggle) {
		this.toggled = toggle;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}
}
