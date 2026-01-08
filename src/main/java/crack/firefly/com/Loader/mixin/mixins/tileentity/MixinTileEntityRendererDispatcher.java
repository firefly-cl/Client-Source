package crack.firefly.com.Loader.mixin.mixins.tileentity;

import crack.firefly.com.System.mods.impl.FPSBoostMod;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @Shadow public double staticPlayerX;
    @Shadow public double staticPlayerY;
    @Shadow public double staticPlayerZ;

    @Inject(method = "renderTileEntity", at = @At("HEAD"), cancellable = true)
    private void aggressiveRenderCut(TileEntity tile, float ticks, int stage, CallbackInfo ci) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod == null || !mod.isToggled()) return;

        double dx = tile.getPos().getX() - staticPlayerX;
        double dy = tile.getPos().getY() - staticPlayerY;
        double dz = tile.getPos().getZ() - staticPlayerZ;
        double distSq = dx * dx + dy * dy + dz * dz;

        // --- LÓGICA DE BEDWARS LUNAR ---
        // Baús e EnderChests no meio do mapa sugam muito FPS. 
        // Vamos limitar a renderização deles a 24 blocos (1.5 chunks)
        if (tile instanceof TileEntityChest || tile instanceof TileEntityEnderChest) {
            if (distSq > 576.0) { // 24^2 = 576
                ci.cancel();
                return;
            }
        }

        // Se o mod de esconder Skulls estiver ON, corta na hora
        if (mod.hideSkulls.isToggled() && tile instanceof TileEntitySkull) {
            ci.cancel();
            return;
        }

        // Corte genérico para qualquer outra coisa a 2 chunks (32 blocos)
        if (distSq > 1024.0) {
            ci.cancel();
        }
    }
}