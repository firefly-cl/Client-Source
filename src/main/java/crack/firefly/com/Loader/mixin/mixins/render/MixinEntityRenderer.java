package crack.firefly.com.Loader.mixin.mixins.render;

import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.System.event.impl.*;
import crack.firefly.com.System.mods.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.client.shader.ShaderGroup;
import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow private float thirdPersonDistance;
    @Shadow private int[] lightmapColors;
    @Shadow private boolean renderHand;

    @Final @Unique
    private final Minecraft mc = Minecraft.getMinecraft();

    @Unique private float height, previousHeight;
    @Unique private final SimpleAnimation smooth = new SimpleAnimation(0.0F);
    @Unique private float rotationYaw, prevRotationYaw, rotationPitch, prevRotationPitch;

    // Eventos estáticos para evitar criação de objetos desnecessários no loop de render
    @Unique private static final EventRender3D EVENT_3D = new EventRender3D(0);
    @Unique private static final EventPlayerHeadRotation EVENT_HEAD = new EventPlayerHeadRotation(0, 0);
    @Unique private static final EventHurtCamera EVENT_HURT = new EventHurtCamera();
    @Unique private static final EventCameraRotation EVENT_CAM = new EventCameraRotation(0, 0, 0, 0);
    @Unique private static final EventZoomFov EVENT_ZOOM = new EventZoomFov(0);
    @Unique private static final EventGamma EVENT_GAMMA = new EventGamma(0);
    @Unique private static final EventShader EVENT_SHADER = new EventShader();

    /**
     * OTIMIZAÇÃO DE SHADERS (SATURATION/MOTION BLUR)
     */
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderWorld(FJ)V", shift = At.Shift.AFTER))
    private void onPostRenderWorld(float partialTicks, long nanoTime, CallbackInfo ci) {
        EVENT_SHADER.getGroups().clear();
        EVENT_SHADER.call();
        
        List<ShaderGroup> groups = EVENT_SHADER.getGroups();
        if (groups.isEmpty()) return; // Saída rápida se não houver shaders ativos

        for (int i = 0; i < groups.size(); i++) {
            ShaderGroup group = groups.get(i);
            if (group != null) {
                group.loadShaderGroup(partialTicks);
            }
        }
    }

    /**
     * EVENTO RENDER 3D (ESP, NAMETAGS, ETC)
     */
    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void onRender3D(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        EVENT_3D.setPartialTicks(partialTicks);
        EVENT_3D.call();
    }

    /**
     * CORREÇÃO DE ROTAÇÃO (FREELOOK COMPATÍVEL)
     */
    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V"))
    public void updateCameraAndRender(EntityPlayerSP player, float yaw, float pitch) {
        EVENT_HEAD.setYaw(yaw);
        EVENT_HEAD.setPitch(pitch);
        EVENT_HEAD.setCancelled(false);
        EVENT_HEAD.call();

        if (!EVENT_HEAD.isCancelled()) {
            player.setAngles(EVENT_HEAD.getYaw(), EVENT_HEAD.getPitch());
        }
    }

    /**
     * EFEITO DE DANO (HURTCAM) OTIMIZADO
     */
    @Redirect(method = "hurtCameraEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"))
    public void adjustHurtCameraEffect(float angle, float x, float y, float z) {
        EVENT_HURT.setCancelled(false);
        EVENT_HURT.setIntensity(1.0f);
        EVENT_HURT.call();

        if (EVENT_HURT.getIntensity() != 1.0F) {
            angle *= EVENT_HURT.getIntensity();
        }
        GlStateManager.rotate(angle, x, y, z);
    }

    /**
     * CÂMERA EM 3ª PESSOA E FREELOOK
     */
    @Inject(method = "orientCamera", at = @At("HEAD"))
    public void orientCamera(float partialTicks, CallbackInfo ci) {
        Entity view = mc.getRenderViewEntity();
        if (view == null) return;

        rotationYaw = view.rotationYaw;
        prevRotationYaw = view.prevRotationYaw;
        rotationPitch = view.rotationPitch;
        prevRotationPitch = view.prevRotationPitch;

        EVENT_CAM.setYaw(rotationYaw);
        EVENT_CAM.setPitch(rotationPitch);
        EVENT_CAM.setRoll(0);
        EVENT_CAM.setThirdPersonDistance(thirdPersonDistance);
        EVENT_CAM.setCancelled(false);
        EVENT_CAM.call();

        rotationYaw = EVENT_CAM.getYaw();
        rotationPitch = EVENT_CAM.getPitch();
        thirdPersonDistance = EVENT_CAM.getThirdPersonDistance();

        FreelookMod freelook = FreelookMod.getInstance();
        if (freelook.isToggled() && freelook.isActive()) {
            rotationYaw = freelook.getCameraYaw();
            rotationPitch = freelook.getCameraPitch();
            if (mc.gameSettings.thirdPersonView == 2) rotationYaw += 180.0F;
        }

        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;

        if (EVENT_CAM.getRoll() != 0) {
            GlStateManager.rotate(EVENT_CAM.getRoll(), 0, 0, 1);
        }
    }

    /**
     * SMOOTH SNEAK (AGAXAR SUAVE)
     */
    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyeHeight()F"))
    public float modifyEyeHeight(Entity entity) {
        AnimationsMod mod = AnimationsMod.getInstance();
        if (mod.isToggled() && mod.getSneakSetting().isToggled() && mod.getSmoothSneakSetting().isToggled()) {
            smooth.setAnimation(previousHeight + (height - previousHeight), mod.getSmoothSneakSpeedSetting() * 25);
            return smooth.getValue();
        }
        return entity.getEyeHeight();
    }

    @Inject(method = "updateRenderer", at = @At("HEAD"))
    public void preUpdateRenderer(CallbackInfo ci) {
        Entity entity = mc.getRenderViewEntity();
        if (entity == null) return;
        previousHeight = height;
        height += (entity.getEyeHeight() - height) * 0.5f; // Interpolação mais simples e leve
    }

    /**
     * FOV E GAMMA (BRILHO)
     */
    @Inject(method = "getFOVModifier", at = @At("RETURN"), cancellable = true)
    public void onFov(float partialTicks, boolean useFovSetting, CallbackInfoReturnable<Float> cir) {
        EVENT_ZOOM.setFov(cir.getReturnValue());
        EVENT_ZOOM.call();
        cir.setReturnValue(EVENT_ZOOM.getFov());
    }

    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;gammaSetting:F"))
    public float overrideGamma(GameSettings settings) {
        EVENT_GAMMA.setGamma(settings.gammaSetting);
        EVENT_GAMMA.call();
        return EVENT_GAMMA.getGamma();
    }

    /**
     * OTIMIZAÇÃO DE CORES (VIVID COLORS) - ESTILO LUNAR
     * Removido Math.pow para ganho massivo de FPS.
     */
    @Inject(method = "updateLightmap", at = @At("TAIL"))
    private void enhanceBrightColors(float partialTicks, CallbackInfo ci) {
        for (int i = 0; i < lightmapColors.length; i++) {
            int c = lightmapColors[i];
            
            // Extração de cores rápida usando bitwise
            float rf = ((c >> 16) & 0xFF) / 255.0f;
            float gf = ((c >> 8) & 0xFF) / 255.0f;
            float bf = (c & 0xFF) / 255.0f;

            // Curva de saturação otimizada (Multiplicação é 10x mais rápida que Math.pow)
            rf = fastVivid(rf);
            gf = fastVivid(gf);
            bf = fastVivid(bf);

            lightmapColors[i] = 0xFF000000 | ((int)(rf * 255) << 16) | ((int)(gf * 255) << 8) | (int)(bf * 255);
        }
    }

    @Unique
    private float fastVivid(float v) {
        // Se a cor for muito escura, não mexe (evita bugs em cavernas)
        if (v < 0.40f) return v;
        // Aproximação linear da curva de saturação: mais leve que Math.pow
        return Math.min(1.0f, v * 1.08f); 
    }

    // Shadowing de campos de rotação para os redirects de orientCamera
    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F"))
    public float getRotationYaw(Entity entity) { return rotationYaw; }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F"))
    public float getPrevRotationYaw(Entity entity) { return prevRotationYaw; }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F"))
    public float getRotationPitch(Entity entity) { return rotationPitch; }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F"))
    public float getPrevRotationPitch(Entity entity) { return prevRotationPitch; }
}