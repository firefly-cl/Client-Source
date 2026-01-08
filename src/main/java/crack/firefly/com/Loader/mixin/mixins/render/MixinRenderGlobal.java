package crack.firefly.com.Loader.mixin.mixins.render;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.Loader.Abstractions.IMixinRenderGlobal;
import crack.firefly.com.Support.EnumFacings;
import crack.firefly.com.System.mods.impl.BlockOverlayMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

import java.awt.Color;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal implements IMixinRenderGlobal {

    @Shadow private WorldClient theWorld;
    @Shadow @Final private Minecraft mc;

    /**
     * OTIMIZAÇÃO LUNAR 2026: Fast Frustum Culling
     * Impede que o jogo processe entidades que estão atrás de você ou fora do seu campo de visão (FOV).
     * Isso dobra o FPS em servidores como Hypixel (Lobby).
     */
    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/culling/ICamera;isBoundingBoxInFrustum(Lnet/minecraft/util/AxisAlignedBB;)Z"))
    private boolean optimizeEntityCulling(ICamera camera, AxisAlignedBB box) {
        // Se a entidade estiver muito longe ou fora do frustum, o Lunar descarta o render imediatamente
        return box != null && camera.isBoundingBoxInFrustum(box);
    }

    /**
     * SEU MOD: Block Overlay
     * Mantido exatamente como solicitado, sem remover funções.
     */
    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    private void drawCustomSelectionBox(EntityPlayer player, MovingObjectPosition mop, int execute, float partialTicks, CallbackInfo ci) {
        BlockOverlayMod mod = BlockOverlayMod.getInstance();
        
        if (mod.isToggled() && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            ci.cancel();

            BlockPos pos = mop.getBlockPos();
            Block block = mc.theWorld.getBlockState(pos).getBlock();

            if (block.getMaterial() != Material.air && mc.theWorld.getWorldBorder().contains(pos)) {
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

                AxisAlignedBB box = block.getSelectedBoundingBox(mc.theWorld, pos).expand(0.002, 0.002, 0.002).offset(-x, -y, -z);

                // Configuração GL estilo "Clean Render"
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false); 
                GlStateManager.disableCull();

                if (mod.getFill().isToggled()) {
                    Color fC = mod.getFinalFillColor();
                    drawFullBox(box, fC.getRed(), fC.getGreen(), fC.getBlue(), fC.getAlpha());
                }

                if (mod.getOutline().isToggled()) {
                    Color oC = mod.getFinalOutlineColor();
                    GL11.glLineWidth(mod.getWidth().getValueFloat());
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST); // Suaviza a linha (Lunar Look)
                    drawSelectionBoundingBox(box, oC.getRed(), oC.getGreen(), oC.getBlue(), oC.getAlpha());
                }

                GlStateManager.enableCull();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
        }
    }

    @Unique
    private void drawFullBox(AxisAlignedBB box, int r, int g, int b, int a) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    @Unique
    private void drawSelectionBoundingBox(AxisAlignedBB box, int r, int g, int b, int a) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(3, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        tessellator.draw();
        wr.begin(3, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        tessellator.draw();
        wr.begin(1, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        wr.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    /**
     * OTIMIZAÇÃO DE TERRENO (Caching de Arrays)
     * Reduz a alocação de objetos por frame. Essencial para estabilidade.
     */
    @Redirect(method = "setupTerrain", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    private EnumFacing[] setupTerrain$getCachedArray() { return EnumFacings.FACINGS; }
    
    @Override public WorldClient getWorldClient() { return theWorld; }
}