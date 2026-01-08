package crack.firefly.com.Loader.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crack.firefly.com.System.skin.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Inject(method = "getLocationSkin", at = @At("HEAD"), cancellable = true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> callback) {
        // Verifica se essa entidade é O JOGADOR que está usando o client
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        
        // Compara pelo UUID ou pelo nome para garantir que é você
        if (player.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
            
            // Se o SkinManager tiver uma skin carregada, usa ela!
            if (SkinManager.getInstance().hasCustomSkin()) {
                callback.setReturnValue(SkinManager.getInstance().getCustomSkin());
            }
        }
    }
}