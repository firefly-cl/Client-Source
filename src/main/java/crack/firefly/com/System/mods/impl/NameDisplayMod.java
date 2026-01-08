package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;
import crack.firefly.com.System.nanovg.font.LegacyIcon;

public class NameDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	private ComboSetting prefixSetting = new ComboSetting(TranslateText.PREFIX, this, TranslateText.NAME, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.NAME), new Option(TranslateText.IGN))));
	
	public NameDisplayMod() {
		super(TranslateText.NAME_DISPLAY, TranslateText.NAME_DISPLAY_DESCRIPTION);
	}

	@Override
	public String getText() {
		
		Option option = prefixSetting.getOption();
		String prefix = "null";
		
		if(option.getTranslate().equals(TranslateText.NAME)) {
			prefix = "Name";
		}
		
		if(option.getTranslate().equals(TranslateText.IGN)) {
			prefix = "Ign";
		}
		
		return prefix + ": " + mc.thePlayer.getGameProfile().getName();
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.USER : null;
	}
}
