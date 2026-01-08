package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import java.util.Collection;

public class PotionStatusMod extends HUDMod {

    // PADRÃO: PRETO TRANSPARENTE E BRANCO
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);

    public PotionStatusMod() {
        super(TranslateText.POTION_STATUS, TranslateText.POTION_STATUS_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (mc.thePlayer == null) return;
        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float scale = getScale();
        float rowH = 22 * scale;
        float w = 110 * scale;
        float h = (effects.size() * rowH) + (4 * scale);
        this.setWidth((int) w); this.setHeight((int) h);

        nvg.setupAndDraw(() -> {
            nvg.drawRoundedRect(getX(), getY(), w, h, 5, bgColor.getColor());
            int i = 0;
            for (PotionEffect effect : effects) {
                Potion potion = Potion.potionTypes[effect.getPotionID()];
                String name = I18n.format(potion.getName());
                String duration = Potion.getDurationString(effect);
                float py = getY() + (i * rowH) + (5 * scale);
                nvg.drawText(name, getX() + (22 * scale), py, textColor.getColor(), 9 * scale, getHudFont(1));
                nvg.drawText(duration, getX() + (22 * scale), py + (9 * scale), new Color(200, 200, 200), 7 * scale, getHudFont(1));
                i++;
            }
        });

        int i = 0;
        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion.hasStatusIcon()) {
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                int iconIdx = potion.getStatusIconIndex();
                GlStateManager.pushMatrix();
                GlStateManager.translate(getX() + (4 * scale), getY() + (i * rowH) + (4 * scale), 0);
                GlStateManager.scale(scale * 0.8f, scale * 0.8f, 1);
                RenderUtils.drawTexturedModalRect(0, 0, iconIdx % 8 * 18, 198 + iconIdx / 8 * 18, 18, 18);
                GlStateManager.popMatrix();
            }
            i++;
        }
    }
}