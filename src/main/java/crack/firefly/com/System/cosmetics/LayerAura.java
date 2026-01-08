package crack.firefly.com.System.cosmetics;

import crack.firefly.com.System.mods.impl.AuraCosmeticMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import java.awt.Color;

public class LayerAura implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer renderPlayer;

    public LayerAura(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float p1, float p2, float partialTicks, float p4, float p5, float p6, float scale) {
        AuraCosmeticMod mod = AuraCosmeticMod.getInstance();
        if (mod == null || !mod.isToggled()) return;

        Color auraColor = mod.getAuraColor();
        float speedVal = mod.speed.getValueFloat();
        
        float pulse = (float) (Math.sin((System.currentTimeMillis() / 200.0) * speedVal) * 0.04 + 1.04);
        float customScale = mod.scale.getValueFloat() * pulse;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0); 
        
        GlStateManager.color(auraColor.getRed()/255f, auraColor.getGreen()/255f, auraColor.getBlue()/255f, 0.35f);
        GlStateManager.scale(customScale, customScale, customScale);
        
        this.renderPlayer.getMainModel().render(player, p1, p2, p4, p5, p6, scale);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() { return false; }
}