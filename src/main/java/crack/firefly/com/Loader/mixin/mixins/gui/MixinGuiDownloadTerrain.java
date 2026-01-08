package crack.firefly.com.Loader.mixin.mixins.gui;

import java.awt.Color;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDownloadTerrain.class)
public abstract class MixinGuiDownloadTerrain extends GuiScreen {

    @Unique
    private static final ResourceLocation LOGO_WHITE = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void onDrawTerrain(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        float w = (float) this.width;
        float h = (float) this.height;
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();

        drawRect(0, 0, this.width, this.height, new Color(12, 12, 12).getRGB());
        renderLogo(w, h);

        nvg.setupAndDraw(() -> {
            float barW = 180f;
            float barX = (w - barW) / 2f;
            float barY = h / 2f + 50f;

            nvg.drawRoundedRect(barX, barY, barW, 2.5f, 1f, new Color(255, 255, 255, 20));

            float time = (System.currentTimeMillis() % 2000) / 2000f;
            float progress = (float) (Math.sin(time * Math.PI * 2 - Math.PI / 2) * 0.5 + 0.5);
            float thumbW = 45f;
            float thumbX = barX + (progress * (barW - thumbW));
            
            nvg.drawRoundedRect(thumbX, barY, thumbW, 2.5f, 1f, new Color(0, 255, 180));
            nvg.drawCenteredText("CARREGANDO MUNDO...", w / 2f, barY + 12f, new Color(200, 200, 200), 8f, Fonts.REGULAR);
        });
    }

    @Unique
    private void renderLogo(float w, float h) {
        int logoW = 160;
        int logoH = 40;
        float x = (w / 2f) - (logoW / 2f);
        float y = (h / 2f) - (logoH / 2f) - 10f;

        mc.getTextureManager().bindTexture(LOGO_WHITE);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiScreen.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0f, 0f, logoW, logoH, (float)logoW, (float)logoH);
    }
}