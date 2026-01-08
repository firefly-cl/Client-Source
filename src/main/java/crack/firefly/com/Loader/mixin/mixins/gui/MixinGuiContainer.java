package crack.firefly.com.Loader.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.impl.GuiMod;
import crack.firefly.com.System.mods.impl.InventoryMod;
import crack.firefly.com.System.mods.settings.impl.combo.Option;
import crack.firefly.com.ui.particle.ParticleEngine;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.easing.EaseBackIn;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {

    @Shadow protected abstract boolean checkHotbarKeys(int keyCode);
    @Shadow protected int xSize;
    @Shadow protected int ySize;
    @Shadow protected int guiLeft;
    @Shadow protected int guiTop;
    @Shadow private int dragSplittingButton;
    @Shadow private int dragSplittingRemnant;

    private SimpleAnimation xAnimation;
    private SimpleAnimation yAnimation;
    private Animation xAnimationBackIn;
    private Animation yAnimationBackIn;
    private ParticleEngine particle = new ParticleEngine();
    
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        InventoryMod mod = InventoryMod.getInstance();
        if(mod.isToggled() && mod.getAnimationSetting().isToggled()) {
            Option option = mod.getAnimationTypeSetting().getOption();
            if(option.getTranslate().equals(TranslateText.NORMAL)) {
                xAnimation = new SimpleAnimation(0.0F);
                yAnimation = new SimpleAnimation(0.0F);
            } else {
                xAnimationBackIn = new EaseBackIn(380, this.width, 2);
                yAnimationBackIn = new EaseBackIn(380, this.height, 2);
            }
        }
    }
    
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        InventoryMod mod = InventoryMod.getInstance();
        
        // 1. DESENHAR FUNDO E PARTÍCULAS (Sempre atrás de tudo)
        if(!mod.isToggled() || (mod.isToggled() && mod.getBackgroundSetting().isToggled())) {
            this.drawDefaultBackground();
        }
        
        if(mod.isToggled() && mod.getParticleSetting().isToggled()) {
            particle.draw(mouseX, mouseY);
        }

        // 2. LOGO ESTILO LUNAR (FIXA NO CANTO)
        // Desenhamos aqui para que ela NÃO sofra a escala da animação do inventário
        if (mod.isToggled() && mod.getLogoSetting().isToggled()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); 
            
            try {
                this.mc.getTextureManager().bindTexture(mod.getLogoLocation());
                
                // Proporção original: 1987x457 (~4.34)
                int logoWidth = 120; 
                int logoHeight = 28; 
                
                int margin = 8; // Distância da borda da tela
                
                // Posicionamento: Canto Inferior Direito
                int xPos = this.width - logoWidth - margin;
                int yPos = this.height - logoHeight - margin;

                Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
            } catch (Exception e) {
                // Previne crash se a textura falhar
            }
            
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        // 3. INÍCIO DAS TRANSFORMAÇÕES DO INVENTÁRIO
        // Tudo o que for desenhado a partir daqui será afetado pelo zoom/animação
        GlStateManager.pushMatrix();

        // Zoom do GuiMod
        GuiMod guiMod = GuiMod.getInstance();
        if (guiMod != null && guiMod.isToggled() && guiMod.getInventoryScale() != 1.0f) {
            float scale = guiMod.getInventoryScale();
            float centerX = this.width / 2.0f;
            float centerY = this.height / 2.0f;
            GlStateManager.translate(centerX, centerY, 0);
            GlStateManager.scale(scale, scale, 1.0f);
            GlStateManager.translate(-centerX, -centerY, 0);
        }

        // Animação de entrada
        if(mod.isToggled() && mod.getAnimationSetting().isToggled()) {
            double xmod = 0;
            double ymod = 0;
            Option option = mod.getAnimationTypeSetting().getOption();
            
            if(option.getTranslate().equals(TranslateText.NORMAL)) {
                xAnimation.setAnimation(this.width, 18);
                yAnimation.setAnimation(this.height, 18);
                xmod = this.width / 2 - (xAnimation.getValue() / 2);
                ymod = this.height / 2 - (yAnimation.getValue() / 2);
                GlStateManager.translate(xmod, ymod, 0);
                GlStateManager.scale(xAnimation.getValue() / this.width, yAnimation.getValue() / this.height, 1);    
            } else {
                xmod = this.width / 2 - (xAnimationBackIn.getValue() / 2);
                ymod = this.height / 2 - (yAnimationBackIn.getValue() / 2);
                GlStateManager.translate(xmod, ymod, 0);
                GlStateManager.scale(xAnimationBackIn.getValue() / this.width, yAnimationBackIn.getValue() / this.height, 1);    
            }
        }
    }
    
    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreenReturn(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        // Fecha a matrix de transformação aberta no HEAD
        GlStateManager.popMatrix();
    }
    
    @Inject(method = "updateDragSplitting", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void fixRemnants(CallbackInfo ci) {
        if (this.dragSplittingButton == 2) {
            this.dragSplittingRemnant = mc.thePlayer.inventory.getItemStack().getMaxStackSize();
            ci.cancel();
        }
    }
    
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void preMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (mouseButton - 100 == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.thePlayer.closeScreen();
            ci.cancel();
        }
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void postMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        checkHotbarKeys(mouseButton - 100);
    }
    
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawDefaultBackground()V"))
    public void removeDrawDefaultBackground() {
        // Removido pois já chamamos manualmente no topo do drawScreen
    }
}