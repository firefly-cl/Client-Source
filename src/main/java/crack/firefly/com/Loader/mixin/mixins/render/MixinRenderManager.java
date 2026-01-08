package crack.firefly.com.Loader.mixin.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.Loader.Abstractions.IMixinRenderManager;
import crack.firefly.com.System.event.impl.EventRenderHitbox;
import crack.firefly.com.System.mods.impl.FreelookMod;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IMixinRenderManager {

    @Shadow public TextureManager renderEngine;
    @Shadow private double renderPosX, renderPosY, renderPosZ;
    @Shadow private float playerViewY, playerViewX;

    @Inject(method = "renderDebugBoundingBox", at = @At("HEAD"), cancellable = true)
    public void onRenderHitbox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventRenderHitbox event = new EventRenderHitbox(entityIn, x, y, z, entityYaw, partialTicks);
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
    
    /**
     * FIX FINAL NAMETAG LUNAR STYLE
     * Sobrescrevemos a rotação de renderização global das labels.
     */
    @Inject(method = "cacheActiveRenderInfo", at = @At("RETURN"))
    public void onCacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, Entity livingEntityIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks, CallbackInfo ci) {
        FreelookMod freelook = FreelookMod.getInstance();

        if (freelook.isToggled() && freelook.isActive()) {
            // Pega o ângulo da câmera livre e aplica no RenderManager
            // Isso faz com que Nametags e Hologramas encarem você durante o Freelook
            this.playerViewY = freelook.getPrevCameraYaw() + (freelook.getCameraYaw() - freelook.getPrevCameraYaw()) * partialTicks;
            this.playerViewX = freelook.getPrevCameraPitch() + (freelook.getCameraPitch() - freelook.getPrevCameraPitch()) * partialTicks;
        }
    }
    
    @Override public double getRenderPosX() { return this.renderPosX; }
    @Override public double getRenderPosY() { return this.renderPosY; }
    @Override public double getRenderPosZ() { return this.renderPosZ; }
}