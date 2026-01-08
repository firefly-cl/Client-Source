package crack.firefly.com.Loader.mixin.mixins.resources;

import java.io.File;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.resources.ResourcePackRepository;

@Mixin(ResourcePackRepository.class)
public abstract class MixinResourcePackRepository {

    @Shadow
    @Final
    private File dirServerResourcepacks;
    
    /**
     * @author Firefly
     * @reason Garante que a pasta de packs do servidor exista antes da limpeza.
     * Isso evita erros de IO e possíveis NullPointerExceptions no loop de deleção.
     */
    @Inject(method = "deleteOldServerResourcesPacks", at = @At("HEAD"), cancellable = true)
    private void preDeleteOldPacks(CallbackInfo ci) {
        // Se a pasta não existe, não há nada para deletar. 
        // Criamos a pasta apenas para garantir a integridade do sistema de arquivos.
        if (!this.dirServerResourcepacks.exists()) {
            if (this.dirServerResourcepacks.mkdirs()) {
                // Se acabamos de criar a pasta, ela está vazia, então podemos cancelar 
                // a execução do resto do método original para economizar processamento.
                ci.cancel();
            }
        }
    }
}