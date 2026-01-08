package crack.firefly.com.System.mods.impl;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventHitOverlay;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class HitColorMod extends Mod {

	private BooleanSetting customColorSetting = new BooleanSetting(TranslateText.CUSTOM_COLOR, this, false);
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, new Color(255, 0, 0), false);
    private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 0.45, 0, 1.0, false);
    
	public HitColorMod() {
		super(TranslateText.HIT_COLOR, TranslateText.HIT_COLOR_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onHitOverlay(EventHitOverlay event) {
		
		AccentColor currentColor = Firefly.getInstance().getColorManager().getCurrentColor();
		Color lastColor = customColorSetting.isToggled() ? colorSetting.getColor() : currentColor.getInterpolateColor();
		
		event.setRed(lastColor.getRed() / 255F);
		event.setGreen(lastColor.getGreen() / 255F);
		event.setBlue(lastColor.getBlue() / 255F);
		event.setAlpha(alphaSetting.getValueFloat());
	}
}
