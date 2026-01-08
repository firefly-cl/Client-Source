package crack.firefly.com.System.mods.impl.waveycapes.layers;

import crack.firefly.com.menus.mod.Family.Providers.CosmeticsFamily;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class ShieldRenderLayer implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer rp;
    private static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation("Firefly/cosmetics/shield_white.png");

    public ShieldRenderLayer(RenderPlayer rp) {
        this.rp = rp;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float p1, float p2, float partialTicks, float p4, float p5, float p6, float p7) {
        if (CosmeticsFamily.isShieldActive && player.equals(Minecraft.getMinecraft().thePlayer) && !player.isInvisible()) {
            ModelPlayer model = rp.getMainModel();
            
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
            // 1. Gruda no braço esquerdo
            model.bipedLeftArm.postRender(0.0625F);

            // 2. POSIÇÃO FINAL CORRIGIDA
            // X: 0.14F -> Diminuído de 0.22 para COLAR mais no braço.
            // Y: -0.08F -> Ajuste fino da altura para o tamanho grande.
            // Z: -0.45F -> Ajustado de -0.55 para CENTRALIZAR (estava muito pra frente).
            GlStateManager.translate(0.14F, -0.08F, -0.45F); 
            
            // 3. ROTAÇÃO
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            
            // 4. ESCALA (Mantida em 0.80)
            float scale = 0.80F; 
            GlStateManager.scale(scale, scale, scale);

            Minecraft.getMinecraft().getTextureManager().bindTexture(SHIELD_TEXTURE);

            // 5. Renderiza com Extrusão 3D
            render3DShield(0.10F);

            GlStateManager.popMatrix();
        }
    }

    private void render3DShield(float thickness) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        // Face Frontal
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0, 0, 0).tex(0, 0).endVertex();
        worldrenderer.pos(1, 0, 0).tex(1, 0).endVertex();
        worldrenderer.pos(1, 1, 0).tex(1, 1).endVertex();
        worldrenderer.pos(0, 1, 0).tex(0, 1).endVertex();
        tessellator.draw();

        // Face Traseira
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0, 1, -thickness).tex(0, 1).endVertex();
        worldrenderer.pos(1, 1, -thickness).tex(1, 1).endVertex();
        worldrenderer.pos(1, 0, -thickness).tex(1, 0).endVertex();
        worldrenderer.pos(0, 0, -thickness).tex(0, 0).endVertex();
        tessellator.draw();
        
        // Extrusão (Cria as bordas sólidas)
        int slices = 32; 
        float step = 1.0F / (float)slices;
        for (int i = 0; i < slices; i++) {
            float pos = (float)i * step;
            float nextPos = pos + step;

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(pos, 0, -thickness).tex(pos, 0).endVertex();
            worldrenderer.pos(pos, 0, 0).tex(pos, 0).endVertex();
            worldrenderer.pos(pos, 1, 0).tex(pos, 1).endVertex();
            worldrenderer.pos(pos, 1, -thickness).tex(pos, 1).endVertex();
            worldrenderer.pos(nextPos, 1, -thickness).tex(nextPos, 1).endVertex();
            worldrenderer.pos(nextPos, 1, 0).tex(nextPos, 1).endVertex();
            worldrenderer.pos(nextPos, 0, 0).tex(nextPos, 0).endVertex();
            worldrenderer.pos(nextPos, 0, -thickness).tex(nextPos, 0).endVertex();
            tessellator.draw();

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0, pos, -thickness).tex(0, pos).endVertex();
            worldrenderer.pos(1, pos, -thickness).tex(1, pos).endVertex();
            worldrenderer.pos(1, pos, 0).tex(1, pos).endVertex();
            worldrenderer.pos(0, pos, 0).tex(0, pos).endVertex();
            worldrenderer.pos(0, nextPos, 0).tex(0, nextPos).endVertex();
            worldrenderer.pos(1, nextPos, 0).tex(1, nextPos).endVertex();
            worldrenderer.pos(1, nextPos, -thickness).tex(1, nextPos).endVertex();
            worldrenderer.pos(0, nextPos, -thickness).tex(0, nextPos).endVertex();
            tessellator.draw();
        }
    }

    @Override
    public boolean shouldCombineTextures() { return false; }
}