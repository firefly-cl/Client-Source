package crack.firefly.com.Loader.mixin.mixins.gui;

import java.awt.Color;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {

    @Unique
    private static final ResourceLocation LOGO_WHITE = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");

    /**
     * @author Firefly
     * @reason Forçar o visual Badlion 4.0 na tela de conexão original
     */
    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        // 1. Cancela o desenho original (fundo preto e texto "Connecting...")
        ci.cancel();

        float w = (float) this.width;
        float h = (float) this.height;
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();

        // 2. Fundo Escuro Badlion Style (RGB 12, 12, 12)
        drawRect(0, 0, this.width, this.height, new Color(12, 12, 12).getRGB());

        // 3. Renderizar sua Logo Horizontal Centralizada
        renderLogo(w, h);

        // 4. Interface Moderna NanoVG
        nvg.setupAndDraw(() -> {
            // Título Principal
            nvg.drawCenteredText("ESTABELECENDO CONEXÃO", w / 2f, h / 2f + 25f, Color.WHITE, 10f, Fonts.SEMIBOLD);
            
            // Subtítulo cinza
            nvg.drawCenteredText("AUTENTICANDO COM O SERVIDOR...", w / 2f, h / 2f + 38f, new Color(160, 160, 160), 8f, Fonts.REGULAR);

            // Barra de progresso deslizante (Igual ao Badlion 4.0)
            float barW = 120f;
            float barX = (w - barW) / 2f;
            float barY = h / 2f + 55f;
            
            // Fundo da barrinha
            nvg.drawRoundedRect(barX, barY, barW, 2f, 1f, new Color(255, 255, 255, 15));
            
            // Destaque que corre (Ciano Firefly)
            float progress = (float) Math.abs(Math.sin(System.currentTimeMillis() / 600.0));
            nvg.drawRoundedRect(barX + (progress * (barW - 35f)), barY, 35f, 2f, 1f, new Color(0, 255, 180));
        });

        // 5. Desenha os botões (para o botão Cancelar continuar funcionando)
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Unique
    private void renderLogo(float w, float h) {
        int logoW = 160;
        int logoH = 40;
        float x = (w / 2f) - (logoW / 2f);
        float y = (h / 2f) - (logoH / 2f) - 30f;

        mc.getTextureManager().bindTexture(LOGO_WHITE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Desenha a imagem com as proporções corretas
        GuiScreen.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0f, 0f, logoW, logoH, (float)logoW, (float)logoH);
        GlStateManager.disableBlend();
    }
}