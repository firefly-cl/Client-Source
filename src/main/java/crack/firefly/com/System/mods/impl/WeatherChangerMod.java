package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;

public class WeatherChangerMod extends Mod {

	private static WeatherChangerMod instance;
	
	private ComboSetting weatherSetting = new ComboSetting(TranslateText.WEATHER, this, TranslateText.CLEAR, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.CLEAR), new Option(TranslateText.RAIN), new Option(TranslateText.STORM), new Option(TranslateText.SNOW))));
	
	private NumberSetting rainStrength = new NumberSetting(TranslateText.RAIN_STRENGTH, this, 1, 0, 1, false);
	private NumberSetting thunderStrength = new NumberSetting(TranslateText.THUNDER_STRENGTH, this, 1, 0, 1, false);
	
	public WeatherChangerMod() {
		super(TranslateText.WEATHER_CHANGER, TranslateText.WEATHER_CHANGER_DESCRIPTION, ModCategory.WORLD);
		
		instance = this;
	}

	public static WeatherChangerMod getInstance() {
		return instance;
	}

	public ComboSetting getWeatherSetting() {
		return weatherSetting;
	}

	public NumberSetting getRainStrength() {
		return rainStrength;
	}

	public NumberSetting getThunderStrength() {
		return thunderStrength;
	}
}
