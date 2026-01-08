package crack.firefly.com.System.mods.settings.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;

public class TextSetting extends Setting {

	private String defaultText, text;
	
	public TextSetting(TranslateText tText, Mod parent, String text) {
		super(tText, parent);
		this.text = text;
		this.defaultText = text;
		
		Firefly.getInstance().getModManager().addSettings(this);
	}
	
	@Override
	public void reset() {
		this.text = defaultText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDefaultText() {
		return defaultText;
	}
}
