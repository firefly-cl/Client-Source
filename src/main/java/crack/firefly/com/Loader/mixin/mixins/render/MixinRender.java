package crack.firefly.com.Loader.mixin.mixins.render;

import crack.firefly.com.System.mods.impl.FPSBoostMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void onShouldRender(T entity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod == null || !mod.isToggled()) return;

        // --- NOVO FIX PARA HOLOGRAMAS ---
        if (entity instanceof EntityArmorStand) {
            // Se for um Armor Stand de texto (invisível), NÃO vamos dar cancel no render,
            // mas o Minecraft por padrão já renderiza o texto. 
            // O lag do meio geralmente vem de MUITOS Armor Stands.
            // Vamos esconder apenas se estiverem muito longe (ex: 32 blocos)
            double dx = entity.posX - camX;
            double dz = entity.posZ - camZ;
            if ((dx * dx + dz * dz) > 1024.0) { // 32 blocos
                cir.setReturnValue(false);
            }
            return; // Sai daqui para não esconder o texto de quem está perto
        }

        // --- FILTROS DE DISTÂNCIA (Sem travar o jogo) ---
        double dx = entity.posX - camX;
        double dz = entity.posZ - camZ;
        double distSq = dx * dx + dz * dz;

        if (entity instanceof EntityPlayer) {
            if (entity == Minecraft.getMinecraft().thePlayer) return;
            // Mantém players visíveis conforme o seu slider
            if (distSq > (4096.0 * mod.playerRenderDist.getValue())) {
                cir.setReturnValue(false);
            }
        }
        
        // Esconde itens no chão que estão longe (evita lag no meio cheio de drop)
        if (entity instanceof net.minecraft.entity.item.EntityItem && distSq > 1024.0) {
            cir.setReturnValue(false);
        }
    }
}