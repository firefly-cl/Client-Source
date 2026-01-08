package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;

public class ClientSpooferMod extends Mod {

	private static ClientSpooferMod instance;
	
	private ComboSetting typeSetting = new ComboSetting(TranslateText.TYPE, this, TranslateText.VANILLA, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.VANILLA), new Option(TranslateText.FORGE))));
	
	public ClientSpooferMod() {
		super(TranslateText.CLIENT_SPOOFER, TranslateText.CLIENT_SPOOFER_DESCRIPTION, ModCategory.OTHER);
		
		instance = this;
	}

	public static ClientSpooferMod getInstance() {
		return instance;
	}

	public ComboSetting getTypeSetting() {
		return typeSetting;
	}
}
