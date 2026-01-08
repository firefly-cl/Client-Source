package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventMotionUpdate;
import crack.firefly.com.System.event.impl.EventRender3D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.Render3DUtils;
import net.minecraft.util.Vec3;

public class BreadcrumbsMod extends Mod {

	private List<Vec3> path = new ArrayList<>();
	
	private BooleanSetting customColorSetting = new BooleanSetting(TranslateText.CUSTOM_COLOR, this, false);
	private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, Color.RED, false);
    
    private BooleanSetting timeoutSetting = new BooleanSetting(TranslateText.TIMEOUT, this, true);
    private NumberSetting timeSetting = new NumberSetting(TranslateText.TIME, this, 15, 1, 150, true);
    
	public BreadcrumbsMod() {
		super(TranslateText.BREADCRUMBS, TranslateText.BREADCRUMBS_DESCRIPTION, ModCategory.RENDER, "playertrails");
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		AccentColor currentColor = Firefly.getInstance().getColorManager().getCurrentColor();
		
		Render3DUtils.renderBreadCrumbs(path, customColorSetting.isToggled() ? ColorUtils.applyAlpha(colorSetting.getColor(), 255) : currentColor.getInterpolateColor());
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		
        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }
        
        if (timeoutSetting.isToggled()) {
            while (path.size() > timeSetting.getValueInt()) {
                path.remove(0);
            }
        }
	}
}
