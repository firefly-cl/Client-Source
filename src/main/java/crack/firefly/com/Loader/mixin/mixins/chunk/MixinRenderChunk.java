package crack.firefly.com.Loader.mixin.mixins.chunk;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunk {

    @Shadow private BlockPos position;

    @Inject(method = "setPosition", at = @At("HEAD"), cancellable = true)
    private void onSetPosition(BlockPos pos, CallbackInfo ci) {
        // Otimização "Lunar": Se o chunk não mudou de lugar, não gaste CPU reconstruindo ele
        if (pos.equals(this.position)) {
            ci.cancel();
        }
    }
}