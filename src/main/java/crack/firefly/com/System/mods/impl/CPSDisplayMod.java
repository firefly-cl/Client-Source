package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import crack.firefly.com.Firefly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventClickMouse;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;

public class CPSDisplayMod extends HUDMod {

    private final List<Long> leftPresses = new ArrayList<>();
    private final List<Long> rightPresses = new ArrayList<>();

    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, false);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    
    private BooleanSetting splitCps = new BooleanSetting(TranslateText.MOUSE_BUTTONS, this, true);
    private BooleanSetting label = new BooleanSetting(TranslateText.CPS, this, true); 

    public CPSDisplayMod() {
        super(TranslateText.CPS_DISPLAY, TranslateText.CPS_DISPLAY_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        cleanOldClicks(); // Limpeza de clicks antigos
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        String text = getFormattedText();
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
    
    private String getFormattedText() {
        int left = leftPresses.size();
        int right = rightPresses.size();
        StringBuilder sb = new StringBuilder();
        if (splitCps.isToggled()) sb.append(left).append(" | ").append(right);
        else sb.append(left + right);
        if (label.isToggled()) sb.append(" CPS");
        return sb.toString();
    }
    
    @EventTarget
    public void onClickMouse(EventClickMouse event) {
        if (Mouse.getEventButtonState()) {
            if (event.getButton() == 0) leftPresses.add(System.currentTimeMillis());
            else if (event.getButton() == 1) rightPresses.add(System.currentTimeMillis());
        }
    }
    
    private void cleanOldClicks() {
        long time = System.currentTimeMillis();
        leftPresses.removeIf(t -> time - t > 1000);
        rightPresses.removeIf(t -> time - t > 1000);
    }
}