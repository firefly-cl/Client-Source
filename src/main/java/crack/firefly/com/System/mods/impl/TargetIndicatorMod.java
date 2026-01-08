package crack.firefly.com.System.mods.impl;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender3D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.Render3DUtils;
import crack.firefly.com.Support.TargetUtils;
import net.minecraft.client.renderer.GlStateManager;

public class TargetIndicatorMod extends Mod {

	private BooleanSetting customColorSetting = new BooleanSetting(TranslateText.CUSTOM_COLOR, this, false);
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, Color.RED, false);
    
	public TargetIndicatorMod() {
		super(TranslateText.TARGET_INDICATOR, TranslateText.TARGET_INDICATOR_DESCRIPTION, ModCategory.RENDER);
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		AccentColor currentColor = Firefly.getInstance().getColorManager().getCurrentColor();
		
		if(TargetUtils.getTarget() != null && !TargetUtils.getTarget().equals(mc.thePlayer)) {
			Render3DUtils.drawTargetIndicator(TargetUtils.getTarget(), 0.67, customColorSetting.isToggled() ? ColorUtils.applyAlpha(colorSetting.getColor(), 255) : currentColor.getInterpolateColor());
			GlStateManager.enableBlend();
		}
	}
}
