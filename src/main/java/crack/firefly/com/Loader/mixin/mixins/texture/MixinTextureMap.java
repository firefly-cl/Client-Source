package crack.firefly.com.Loader.mixin.mixins.texture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.impl.EventSwitchTexture;
import net.minecraft.client.renderer.texture.TextureMap;

@Mixin(TextureMap.class)
public class MixinTextureMap {

	@Inject(method = "loadTextureAtlas", at = @At("RETURN"))
	public void preLoadTextureAtlas(CallbackInfo ci) {
        if(Firefly.getInstance().getEventManager() != null) {
            new EventSwitchTexture().call();
        }
	}
}
