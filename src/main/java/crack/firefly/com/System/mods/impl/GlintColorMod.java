package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;
import crack.firefly.com.Support.ColorUtils;

public class GlintColorMod extends Mod {

	private static GlintColorMod instance;
	
	private ComboSetting typeSetting = new ComboSetting(TranslateText.TYPE, this, TranslateText.SYNC, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.SYNC), new Option(TranslateText.RAINBOW), new Option(TranslateText.CUSTOM))));
	
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, Color.RED, false);	
	
	public GlintColorMod() {
		super(TranslateText.GLINT_COLOR, TranslateText.GLINT_COLOR_DESCRIPTION, ModCategory.RENDER, "changeru");
		
		instance = this;
	}

	public static GlintColorMod getInstance() {
		return instance;
	}

	public Color getGlintColor() {
		
		Option type = typeSetting.getOption();
		
		if(type.getTranslate().equals(TranslateText.SYNC)) {
			
			AccentColor currentColor = Firefly.getInstance().getColorManager().getCurrentColor();
			
			return currentColor.getInterpolateColor();
		}else if(type.getTranslate().equals(TranslateText.RAINBOW)) {
			return ColorUtils.getRainbow(0, 25, 255);
		}else if(type.getTranslate().equals(TranslateText.CUSTOM)) {
			return ColorUtils.applyAlpha(colorSetting.getColor(), 255);
		}else {
			return Color.RED;
		}
	}
}
