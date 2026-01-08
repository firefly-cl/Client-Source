package crack.firefly.com.Loader.mixin.mixins.util;

import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import crack.firefly.com.System.mods.impl.RawInputMod;
import net.minecraft.util.MouseHelper;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {

    @Shadow public int deltaX;
    @Shadow public int deltaY;

    // Armazena o resto do movimento que não virou um pixel inteiro (Sub-pixel precision)
    @Unique private double firefly$leftoverX;
    @Unique private double firefly$leftoverY;

    /**
     * Limpa os resíduos ao fechar menus para evitar que a câmera "pule"
     */
    @Inject(method = "mouseGuiClosed", at = @At("HEAD"))
    public void onGuiClosed(CallbackInfo ci) {
        this.firefly$leftoverX = 0;
        this.firefly$leftoverY = 0;
    }

    @Inject(method = "mouseXYChange", at = @At("HEAD"), cancellable = true)
    public void onRawInput(CallbackInfo ci) {
        RawInputMod mod = RawInputMod.getInstance();

        // Verificamos se o mod está ativo e se o mouse está capturado pelo jogo
        if (mod != null && mod.isToggled() && Mouse.isGrabbed()) {
            
            // Pega os deltas brutos da thread do JInput (double para máxima precisão)
            double[] rawDeltas = mod.getAndResetDeltas();

            // Somamos o movimento atual com o que sobrou do frame anterior
            double totalX = rawDeltas[0] + firefly$leftoverX;
            double totalY = rawDeltas[1] + firefly$leftoverY;

            // O Minecraft só aceita 'int', então arredondamos
            this.deltaX = (int) totalX;
            this.deltaY = (int) totalY;

            // O SEGREDO DO LUNAR: 
            // Guardamos a parte decimal para o próximo frame.
            // Exemplo: se moveu 1.4 pixels, deltaX vira 1 e guardamos 0.4 para somar depois.
            firefly$leftoverX = totalX - this.deltaX;
            firefly$leftoverY = totalY - this.deltaY;

            ci.cancel(); // Cancela a leitura padrão do Minecraft (LWJGL)
        } else {
            // Se o mod estiver desligado, resetamos os restos para não bugar ao ligar
            this.firefly$leftoverX = 0;
            this.firefly$leftoverY = 0;
        }
    }
}