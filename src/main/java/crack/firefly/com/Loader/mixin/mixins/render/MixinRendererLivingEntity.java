package crack.firefly.com.Loader.mixin.mixins.render;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.Loader.Abstractions.IMixinRenderPlayer;
import crack.firefly.com.System.event.impl.EventHitOverlay;
import crack.firefly.com.System.mods.impl.NametagMod;
import crack.firefly.com.System.mods.impl.Skin3DMod;
import crack.firefly.com.System.mods.impl.FreelookMod;
import crack.firefly.com.System.mods.impl.AnimationsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity <T extends EntityLivingBase> extends Render<T> {

    @Unique private float red, green, blue, alpha;
    @Unique private final Minecraft mc = Minecraft.getMinecraft();
    
    // Objeto de evento reutilizável para evitar criar 'new' toda hora (Otimização de memória/GC)
    @Unique private final EventHitOverlay hitEvent = new EventHitOverlay(1, 0, 0, 0.3F);

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(method = "renderName", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewY:F"))
    private float redirectYaw(RenderManager manager) {
        // Apenas processa se o Freelook estiver realmente ATIVO no momento
        FreelookMod freelook = FreelookMod.getInstance();
        if (freelook.isToggled() && freelook.isActive()) {
            float yaw = freelook.getCameraYaw();
            if (mc.gameSettings.thirdPersonView == 2) yaw += 180;
            return yaw;
        }
        return manager.playerViewY;
    }

    @Redirect(method = "renderName", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewX:F"))
    private float redirectPitch(RenderManager manager) {
        FreelookMod freelook = FreelookMod.getInstance();
        if (freelook.isToggled() && freelook.isActive()) {
            float pitch = freelook.getCameraPitch();
            if (mc.gameSettings.thirdPersonView == 2) pitch = -pitch;
            return pitch;
        }
        return manager.playerViewX;
    }

    @Inject(method = "renderName", at = @At("HEAD"), cancellable = true)
    public void preRenderName(T entity, double x, double y, double z, CallbackInfo ci) {
        // Se estiver muito longe, nem tenta renderizar o nome customizado (Otimização de Lobby)
        double distanceSq = entity.getDistanceSqToEntity(mc.getRenderManager().livingPlayer);
        if (distanceSq > 4096.0D) return; // Mais de 64 blocos

        if (this.canRenderName(entity)) {
            float f = entity.isSneaking() ? 32.0F : 64.0F;

            if (distanceSq < (double)(f * f)) {
                if (entity.isSneaking()) {
                    renderCustomSneakLabel(entity, x, y, z);
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private void renderCustomSneakLabel(T entity, double x, double y, double z) {
        String s = entity.getDisplayName().getFormattedText();
        FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);

        GlStateManager.rotate(-redirectYaw(this.renderManager), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(redirectPitch(this.renderManager), 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
        GlStateManager.translate(0.0F, 9.374999F, 0.0F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        
        int i = fontrenderer.getStringWidth(s) / 2;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(s, -i, 0, 553648127);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    @Inject(method = "renderModel", at = @At("TAIL"))
    private void renderModelLayers(T entity, float p2, float p3, float p4, float p5, float p6, float p7, CallbackInfo info) {
        if(Skin3DMod.getInstance().isToggled() && this instanceof IMixinRenderPlayer) {
            // Check de visibilidade básico para não renderizar o que não aparece
            if (!entity.isInvisible() || !entity.isInvisibleToPlayer(mc.thePlayer)) {
                IMixinRenderPlayer playerRenderer = (IMixinRenderPlayer) this;
                playerRenderer.getHeadLayer().doRenderLayer((AbstractClientPlayer) entity, p2, 0f, p3, p4, p5, p6, p7);
                playerRenderer.getBodyLayer().doRenderLayer((AbstractClientPlayer) entity, p2, 0f, p3, p4, p5, p6, p7);
            }
        }
    }
    
    @Inject(method = "setBrightness", at = @At("HEAD"))
    public void hitColor(T entity, float partialTicks, boolean combine, CallbackInfoReturnable<Boolean> cir) {
        // Se for uma entidade comum e não estiver levando dano, reseta a cor e sai rápido (Otimização pesada)
        if (entity.hurtTime <= 0 && entity.deathTime <= 0) {
            red = 0; green = 0; blue = 0; alpha = 0;
            return;
        }

        AnimationsMod mod = AnimationsMod.getInstance();
        if (mod.isToggled() && !mod.getDamageFlashSetting().isToggled()) {
            red = 0; green = 0; blue = 0; alpha = 0;
            return;
        }

        // Reutiliza o mesmo objeto de evento em vez de criar um novo 'new EventHitOverlay'
        hitEvent.setRed(1.0F); hitEvent.setGreen(0.0F); hitEvent.setBlue(0.0F); hitEvent.setAlpha(0.3F);
        hitEvent.call();
        
        red = hitEvent.getRed(); green = hitEvent.getGreen(); blue = hitEvent.getBlue(); alpha = hitEvent.getAlpha();
    }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 1, ordinal = 0))
    public float setBrightnessRed(float original) { return red; }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0, ordinal = 0))
    public float setBrightnessGreen(float original) { return green; }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0, ordinal = 1))
    public float setBrightnessBlue(float original) { return blue; }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0.3F, ordinal = 0))
    public float setBrightnessAlpha(float original) { return alpha; }

    @Redirect(method = "canRenderName", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;livingPlayer:Lnet/minecraft/entity/Entity;"))
    public Entity renderOwnName(RenderManager manager) {
        if(NametagMod.getInstance().isToggled()) return null;
        return manager.livingPlayer;
    }
}