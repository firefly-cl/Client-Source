package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import net.minecraft.client.Minecraft;

public class FPSDisplayMod extends HUDMod {

    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, false);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    private BooleanSetting label = new BooleanSetting(TranslateText.TEXT, this, true);

    public FPSDisplayMod() {
        super(TranslateText.FPS_DISPLAY, TranslateText.FPS_DISPLAY_DESCRIPTION);
    }
    
    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        int fps = Minecraft.getDebugFPS();
        String text = label.isToggled() ? fps + " FPS" : String.valueOf(fps);
        float scale = this.getScale();
        
        int baseStringWidth = mc.fontRendererObj.getStringWidth(text);
        int baseFontHeight = mc.fontRendererObj.FONT_HEIGHT;
        float paddingX = 6f; 
        float paddingY = 4f;
        
        float w = (baseStringWidth + (paddingX * 2)) * scale;
        float h = (baseFontHeight + (paddingY * 2)) * scale;
        
        this.setWidth((int) w);
        this.setHeight((int) h);
        
        nvg.setupAndDraw(() -> {
            if (background.isToggled()) {
                nvg.drawRoundedRect(getX(), getY(), w, h, rounded.isToggled() ? 4 : 0, bgColor.getColor());
            }
        });

        GL11.glPushMatrix();
        GL11.glTranslatef(getX() + (w / 2f), getY() + (h / 2f), 0);
        GL11.glScalef(scale, scale, 1f);
        mc.fontRendererObj.drawStringWithShadow(text, -baseStringWidth / 2f, -(baseFontHeight / 2f) + 1f, textColor.getColor().getRGB());
        GL11.glPopMatrix();
    }
}