package crack.firefly.com.Loader.mixin.mixins.entity;

import crack.firefly.com.System.mods.impl.DamageTiltMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public boolean onGround;
    @Shadow public double motionX, motionY, motionZ;
    @Shadow public float rotationYaw;

    @Inject(method = "spawnRunningParticles", at = @At("HEAD"), cancellable = true)
    private void onSpawnParticles(CallbackInfo ci) {
        if (!this.onGround) ci.cancel();
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    private void onApplyCollision(Entity other, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // itens nunca precisam de colisão
        if (other instanceof EntityItem) {
            ci.cancel();
            return;
        }

        // só cancela se ambos NÃO forem o player e estiverem longe
        if ((Object) this != mc.thePlayer && other != mc.thePlayer) {
            if (other.getDistanceSqToEntity(mc.thePlayer) > 36) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "setVelocity", at = @At("HEAD"))
    private void onSetVelocity(double x, double y, double z, CallbackInfo ci) {
        DamageTiltMod mod = DamageTiltMod.getInstance();
        if (!mod.isToggled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || (Object) this != mc.thePlayer) return;

        EntityPlayer player = mc.thePlayer;

        float dx = (float) (player.motionX - x);
        float dz = (float) (player.motionZ - z);

        float yaw = (float) (Math.atan2(dz, dx) * 57.295776f - player.rotationYaw);
        if (Float.isFinite(yaw)) {
            player.attackedAtYaw = yaw;
        }
    }
}
