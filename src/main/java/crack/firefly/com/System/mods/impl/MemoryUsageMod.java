package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class MemoryUsageMod extends HUDMod {

    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, true);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);

    public MemoryUsageMod() {
        super(TranslateText.MEMORY_USAGE, TranslateText.MEMORY_USAGE_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float scale = getScale();
        
        long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100L / Runtime.getRuntime().maxMemory();
        String text = "Mem: " + mem + "%";

        float w = (mc.fontRendererObj.getStringWidth(text) + 8) * scale;
        float h = 16 * scale;
        this.setWidth((int) w); this.setHeight((int) h);

        nvg.setupAndDraw(() -> {
            nvg.drawRoundedRect(getX(), getY(), w, h, rounded.isToggled() ? 4 : 0, bgColor.getColor());
        });

        GL11.glPushMatrix();
        GL11.glTranslatef(getX() + (4 * scale), getY() + (4 * scale), 0);
        GL11.glScalef(scale, scale, 1);
        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, textColor.getColor().getRGB());
        GL11.glPopMatrix();
    }
}