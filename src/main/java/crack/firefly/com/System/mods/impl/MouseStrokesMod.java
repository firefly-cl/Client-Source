package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventPlayerHeadRotation;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import net.minecraft.util.MathHelper;
import java.awt.Color;

public class MouseStrokesMod extends HUDMod {

    private float mouseX, mouseY, lastMouseX, lastMouseY;
    // PADRÃO: PRETO TRANSPARENTE E BRANCO
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting dotColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);

    public MouseStrokesMod() {
        super(TranslateText.MOUSE_STROKES, TranslateText.MOUSE_STROKES_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float scale = getScale();
        float size = 50 * scale;
        this.setWidth((int) size); this.setHeight((int) size);

        float renderX = (lastMouseX + (mouseX - lastMouseX) * event.getPartialTicks()) * scale;
        float renderY = (lastMouseY + (mouseY - lastMouseY) * event.getPartialTicks()) * scale;

        nvg.setupAndDraw(() -> {
            nvg.drawRoundedRect(getX(), getY(), size, size, 6, bgColor.getColor());
            float dotSize = 6 * scale;
            nvg.drawRoundedRect(getX() + (size / 2f) - (dotSize / 2f) + renderX, 
                               getY() + (size / 2f) - (dotSize / 2f) + renderY, 
                               dotSize, dotSize, dotSize / 2f, dotColor.getColor());
        });
    }

    @EventTarget
    public void onPlayerHeadRotation(EventPlayerHeadRotation event) {
        mouseX += event.getYaw() / 45F;
        mouseY -= event.getPitch() / 45F;
        mouseX = MathHelper.clamp_float(mouseX, -18, 18);
        mouseY = MathHelper.clamp_float(mouseY, -18, 18);
    }

    @EventTarget
    public void onTick(EventTick event) {
        lastMouseX = mouseX; lastMouseY = mouseY;
        mouseX *= 0.8F; mouseY *= 0.8F;
    }
}