package crack.firefly.com.Loader.mixin.mixins.chunk;

import crack.firefly.com.System.mods.impl.FPSBoostMod;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderDispatcher.class)
public class MixinChunkRenderDispatcher {

    @Unique private long lastUploadTime = 0L;

    @Inject(method = "runChunkUploads", at = @At("HEAD"), cancellable = true)
    private void onRunChunkUploads(CallbackInfo ci) {
        FPSBoostMod mod = FPSBoostMod.getInstance();
        if (mod == null || !mod.isToggled() || !mod.getChunkDelaySetting().isToggled()) return;

        long delay = (long) mod.getDelaySetting().getValue();
        long now = System.currentTimeMillis();

        if (now - lastUploadTime < delay) {
            ci.cancel(); // pula esse ciclo SEM travar
            return;
        }

        lastUploadTime = now;
    }
}
