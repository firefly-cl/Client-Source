package crack.firefly.com.Loader.mixin.mixins.entity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.Loader.Abstractions.IMixinEntityPlayer;
import crack.firefly.com.System.event.impl.*;
import crack.firefly.com.System.mods.impl.FPSBoostMod;
import crack.firefly.com.System.mods.impl.FreelookMod;
import crack.firefly.com.System.mods.impl.skin3d.render.CustomizableModelPart;
import crack.firefly.com.System.mods.impl.waveycapes.sim.StickSimulation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer implements IMixinEntityPlayer {

    @Final @Unique private final Minecraft mc = Minecraft.getMinecraft(); 
    
    @Unique private static final EventUpdate UPDATE_EVENT = new EventUpdate();
    @Unique private static final EventJump JUMP_EVENT = new EventJump();
    @Unique private static final EventAttackEntity ATTACK_EVENT = new EventAttackEntity(null);

    @Unique private CustomizableModelPart headLayer;
    @Unique private CustomizableModelPart[] skinLayer;
    @Unique private StickSimulation stickSimulation = new StickSimulation();
    
    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    public void attackEntity(Entity entity, CallbackInfo ci) {
        if(entity.canAttackWithItem()) {
            ATTACK_EVENT.setEntity(entity);
            ATTACK_EVENT.call();
        }
    }
	
    @Inject(method = "jump", at = @At("HEAD"))
    public void preJump(CallbackInfo ci) {
        JUMP_EVENT.call();
    }
	
    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void onUpdate(CallbackInfo info) {
        if ((Object)this == mc.thePlayer) {
            UPDATE_EVENT.call();
        }
    }

    @Inject(method = "getArrowCountInEntity", at = @At("HEAD"), cancellable = true)
    private void onGetArrowCount(CallbackInfoReturnable<Integer> cir) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod != null && mod.isToggled() && mod.hideStuckArrows.isToggled()) {
            cir.setReturnValue(0);
        }
    }
    
    @Redirect(method = "getRotationYawHead", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationYawHead:F"))
    public float getRotationYawHead(EntityPlayer entityPlayer) {
        if (entityPlayer == mc.thePlayer && FreelookMod.getInstance().isToggled() && FreelookMod.getInstance().isActive()) {
            return FreelookMod.getInstance().getCameraYaw();
        }
        return entityPlayer.rotationYawHead;
    }

    @Redirect(method = "getPitch", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationPitch:F"))
    public float getRotationPitch(EntityPlayer entityPlayer) {
        if (entityPlayer == mc.thePlayer && FreelookMod.getInstance().isToggled() && FreelookMod.getInstance().isActive()) {
            return FreelookMod.getInstance().getCameraPitch();
        }
        return entityPlayer.rotationPitch;
    }

    @Override public StickSimulation getSimulation() { return stickSimulation; }
    @Override public CustomizableModelPart[] getSkinLayers() { return skinLayer; }
    @Override public void setupSkinLayers(CustomizableModelPart[] box) { this.skinLayer = box; }
    @Override public CustomizableModelPart getHeadLayers() { return headLayer; }
    @Override public void setupHeadLayers(CustomizableModelPart box) { this.headLayer = box; }
}