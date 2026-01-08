package crack.firefly.com.Loader.mixin.mixins.gui;

import java.awt.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {

    @Shadow public Minecraft mc;
    
    // Shadows necessárias para acessar o tamanho da tela
    @Shadow public int width;
    @Shadow public int height;

    /**
     * @author Firefly
     * @reason Substituir o fundo de terra pelo fundo escuro do Badlion 4.0
     */
    @Overwrite
    public void drawBackground(int tint) {
        if (this.mc.theWorld != null) {
            // Fundo escurecido suave quando você abre o ESC dentro do jogo
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            // COR DO BADLION 4.0: Um cinza bem escuro (quase preto) e sólido
            // RGB: 12, 12, 12 é o padrão do BLC
            this.drawRect(0, 0, this.width, this.height, new Color(12, 12, 12).getRGB());
        }
    }
    
    /**
     * @author Firefly
     * @reason Garante que o fundo do mundo (menus) siga o mesmo padrão
     */
    @Overwrite
    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld != null) {
            // Mantém o gradiente padrão de pausa se houver um mundo
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            // Chama o nosso drawBackground customizado acima
            this.drawBackground(tint);
        }
    }
}