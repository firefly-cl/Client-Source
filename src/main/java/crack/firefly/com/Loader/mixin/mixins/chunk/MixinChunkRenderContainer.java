package crack.firefly.com.Loader.mixin.mixins.chunk;

import crack.firefly.com.System.event.impl.EventPreRenderChunk;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderContainer.class)
public abstract class MixinChunkRenderContainer {

    @Unique
    private static final EventPreRenderChunk EVENT = new EventPreRenderChunk(null);

    @Inject(method = "preRenderChunk", at = @At("HEAD"))
    public void preRenderChunk(RenderChunk renderChunkIn, CallbackInfo callback) {
        // Mantém suporte para seus mods que usam esse evento
        if (EventPreRenderChunk.listeners > 0) {
            EVENT.setRenderChunk(renderChunkIn);
            EVENT.call();
        }
        
        // Removemos a lógica de animação que causava o erro de compilação
    }
}