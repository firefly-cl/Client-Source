package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import java.text.DecimalFormat;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventDamageEntity;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

public class ReachDisplayMod extends HUDMod {

    private final DecimalFormat df = new DecimalFormat("0.##");
    private double distance = 0;
    private long hitTime = -1;

    // Configurações idênticas ao CPS
    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, false);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    private BooleanSetting label = new BooleanSetting(TranslateText.DISTANCE, this, true); // Usando DISTANCE do seu enum

    public ReachDisplayMod() {
        super(TranslateText.REACH_DISPLAY, TranslateText.REACH_DISPLAY_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
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

    @EventTarget
    public void onDamageEntity(EventDamageEntity event) {
        if(mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
            hitTime = System.currentTimeMillis();
        }
    }

    private String getFormattedText() {
        if ((System.currentTimeMillis() - hitTime) > 5000) distance = 0;
        if (distance == 0) return "No hits";
        return df.format(distance) + (label.isToggled() ? " blocks" : "");
    }
}