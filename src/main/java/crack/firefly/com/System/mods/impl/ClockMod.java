package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;

public class ClockMod extends HUDMod {

    // CORREÇÃO: IDs diferentes
    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    
    private BooleanSetting use24h = new BooleanSetting(TranslateText.TEXT, this, false);

    public ClockMod() {
        super(TranslateText.CLOCK, TranslateText.CLOCK_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        String formatString = use24h.isToggled() ? "HH:mm" : "h:mm a";
        String text = new SimpleDateFormat(formatString, Locale.US).format(Calendar.getInstance().getTime());
        
        float scale = this.getScale();
        
        int baseStringWidth = mc.fontRendererObj.getStringWidth(text);
        int baseFontHeight = mc.fontRendererObj.FONT_HEIGHT;
        float paddingX = 6f;
        float paddingY = 4f;
        
        float w = (baseStringWidth + (paddingX * 2)) * scale;
        float h = (baseFontHeight + (paddingY * 2)) * scale;
        
        this.setWidth((int) w);
        this.setHeight((int) h);
        
        float x = this.getX();
        float y = this.getY();
        
        nvg.setupAndDraw(() -> {
            if (background.isToggled()) {
                nvg.drawRoundedRect(x, y, w, h, 4, bgColor.getColor());
            }
        });
        
        GL11.glPushMatrix();
        float centerX = x + (w / 2f);
        float centerY = y + (h / 2f);
        GL11.glTranslatef(centerX, centerY, 0);
        GL11.glScalef(scale, scale, 1f);
        mc.fontRendererObj.drawStringWithShadow(text, -baseStringWidth / 2f, -(baseFontHeight / 2f) + 1f, textColor.getColor().getRGB());
        GL11.glPopMatrix();
    }
}