package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.PlayerUtils;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.text.DecimalFormat;

public class SpeedometerMod extends HUDMod {

    private BooleanSetting graphSetting = new BooleanSetting(TranslateText.GRAPH, this, true);
    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, true);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    
    private final int speedCount = 100;
    private final double[] speeds = new double[speedCount];
    private long lastUpdate;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public SpeedometerMod() {
        super(TranslateText.SPEEDOMETER, TranslateText.SPEEDOMETER_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float scale = getScale();
        String text = "Speed: " + df.format(PlayerUtils.getSpeed()) + " m/s";
        
        float w = 110 * scale;
        float h = (graphSetting.isToggled() ? 50 : 16) * scale;
        this.setWidth((int) w); this.setHeight((int) h);

        // Fundo com NanoVG
        nvg.setupAndDraw(() -> {
            nvg.drawRoundedRect(getX(), getY(), w, h, rounded.isToggled() ? 4 : 0, bgColor.getColor());
        });

        // Texto com Fonte do Minecraft
        GL11.glPushMatrix();
        GL11.glTranslatef(getX() + (5 * scale), getY() + (4 * scale), 0);
        GL11.glScalef(scale, scale, 1);
        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, textColor.getColor().getRGB());
        GL11.glPopMatrix();

        if (graphSetting.isToggled()) {
            if (!mc.isGamePaused() && (System.currentTimeMillis() - lastUpdate > 30)) {
                addSpeed(PlayerUtils.getSpeed() / 5.0);
                lastUpdate = System.currentTimeMillis();
            }
            drawGraph(getX() + (5 * scale), getY() + (18 * scale), w - (10 * scale), h - (24 * scale));
        }
    }

    private void drawGraph(float x, float y, float width, float height) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5f);
        ColorUtils.setColor(textColor.getColor().getRGB());
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i < speedCount; i++) {
            GL11.glVertex2d(x + (width * i / (double) speedCount), y + height - (speeds[i] * height / 3.8));
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void addSpeed(double speed) {
        if (speed > 3.8) speed = 3.8;
        System.arraycopy(speeds, 1, speeds, 0, speedCount - 1);
        speeds[speedCount - 1] = speed;
    }
}