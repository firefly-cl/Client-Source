package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;
import net.minecraft.util.ResourceLocation;

public class CompColorPicker extends Comp {

    private SimpleAnimation openAnimation = new SimpleAnimation();
    private SimpleAnimation rainbowAnim = new SimpleAnimation();

    private ColorSetting colorSetting;
    private boolean open;
    private float scale;
    
    // Controles de arraste
    private boolean hueDragging, sbDragging, alphaDragging;
    
    private ResourceLocation HUE_IMG = new ResourceLocation("soar/hue.png");
    private ResourceLocation ALPHA_IMG = new ResourceLocation("soar/alpha.png");

    public CompColorPicker(ColorSetting setting) {
        super(0, 0);
        this.colorSetting = setting;
        this.scale = 1.0F;
        this.open = false;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        openAnimation.setAnimation(open ? 1.0F : 0.0F, 14);
        float animVal = openAnimation.getValue();

        // SEGURANÇA EXTRA: Se o mouse não estiver pressionado, desliga todos os draggs
        if (!org.lwjgl.input.Mouse.isButtonDown(0)) {
            hueDragging = sbDragging = alphaDragging = false;
        }

        // 1. Preview da Cor (Sempre visível)
        Color previewColor = colorSetting.getColor();
        float previewX = this.getX() + (106 * scale);
        float previewY = this.getY();
        
        nvg.drawRoundedRect(previewX, previewY, 16 * scale, 16 * scale, 4, previewColor);
        nvg.drawOutlineRoundedRect(previewX, previewY, 16 * scale, 16 * scale, 4, 1f, new Color(255,255,255,50));
        
        if(animVal < 0.01) return;

        // --- DIMENSÕES ---
        float size = 100 * scale;
        float padding = 6 * scale;
        float chromaBtnHeight = 14 * scale;
        float alphaBarHeight = 12 * scale;
        
        float totalHeight = chromaBtnHeight + padding + size + padding; 
        if(colorSetting.isShowAlpha()) totalHeight += alphaBarHeight + padding;
        
        float startX = this.getX();
        float startY = this.getY() - (totalHeight * animVal) - (5 * scale);

        // Fundo
        nvg.drawRoundedRect(startX - padding, startY - padding, size + 20 * scale + (padding*2), (totalHeight + padding*2) * animVal, 5, new Color(20, 20, 20, 240));
        nvg.drawOutlineRoundedRect(startX - padding, startY - padding, size + 20 * scale + (padding*2), (totalHeight + padding*2) * animVal, 5, 1f, new Color(255,255,255,30));

        if (animVal < 0.8f) return; 

        // 1. Botão Chroma
        boolean isRainbow = colorSetting.isRainbow();
        rainbowAnim.setAnimation(isRainbow ? 1.0f : 0.0f, 16);
        Color chromaBg = isRainbow ? new Color(0, 174, 255) : new Color(60, 60, 60);
        
        nvg.drawRoundedRect(startX, startY, size + 18 * scale, chromaBtnHeight, 3, chromaBg);
        nvg.drawCenteredText("Chroma / Rainbow", startX + (size + 18 * scale)/2, startY + 4 * scale, Color.WHITE, 8 * scale, Fonts.SEMIBOLD);

        float currentY = startY + chromaBtnHeight + padding;

        // --- LÓGICA DE ATUALIZAÇÃO (ARRASTE) ---
        if (open) {
            if (hueDragging) updateHue(mouseY, currentY, size);
            if (sbDragging) updateSB(mouseX, mouseY, startX, currentY, size);
            if (alphaDragging && colorSetting.isShowAlpha()) {
                updateAlpha(mouseX, startX, size + 18 * scale);
            }
        }

        // --- DESENHO CAIXA SB ---
        nvg.drawHSBBox(startX, currentY, size, size, 4, Color.getHSBColor(colorSetting.getHue(), 1, 1));
        
        float satX = startX + (colorSetting.getSaturation() * size);
        float briY = currentY + (size - (colorSetting.getBrightness() * size));
        
        nvg.drawArc(satX, briY, 3 * scale, 0, 360, 1.2F * scale, Color.WHITE);
        nvg.drawArc(satX, briY, 3.5f * scale, 0, 360, 1f, Color.BLACK);

        // --- DESENHO HUE BAR ---
        float hueX = startX + size + (6 * scale);
        nvg.drawRoundedImage(HUE_IMG, hueX, currentY, 12 * scale, size, 3 * scale);
        
        float hueYIndicator = currentY + (colorSetting.getHue() * size);
        nvg.drawRect(hueX - 1, hueYIndicator - 2, 14 * scale, 2 * scale, Color.WHITE);

        currentY += size + padding;

        // --- DESENHO ALPHA BAR ---
        if(colorSetting.isShowAlpha()) {
            float alphaWidth = size + 18 * scale;
            nvg.drawAlphaBar(startX, currentY, alphaWidth, alphaBarHeight, 3 * scale, Color.getHSBColor(colorSetting.getHue(), 1, 1));
            
            float alphaPercent = colorSetting.getAlpha() / 255f;
            float alphaX = startX + (alphaPercent * alphaWidth);
            
            nvg.drawArc(alphaX, currentY + (alphaBarHeight/2), 3 * scale, 0, 360, 1.2F * scale, Color.WHITE);
            nvg.drawArc(alphaX, currentY + (alphaBarHeight/2), 3.5f * scale, 0, 360, 1f, Color.BLACK);
        }
    }
    
    private void updateSB(int mouseX, int mouseY, float x, float y, float size) {
        float xDif = mouseX - x;
        float yDif = mouseY - y;
        colorSetting.setSaturation(Math.min(1, Math.max(0, xDif / size)));
        colorSetting.setBrightness(1 - Math.min(1, Math.max(0, yDif / size)));
    }

    private void updateHue(int mouseY, float y, float size) {
        float yDif = mouseY - y;
        colorSetting.setHue(Math.min(1, Math.max(0, yDif / size)));
    }

    private void updateAlpha(int mouseX, float x, float width) {
        float xDif = mouseX - x;
        colorSetting.setAlpha((int) (Math.min(1, Math.max(0, xDif / width)) * 255));
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0 && isInsidePreview(mouseX, mouseY)) {
            open = !open;
            return;
        }

        if(open && mouseButton == 0) {
            float size = 100 * scale;
            float padding = 6 * scale;
            float chromaBtnHeight = 14 * scale;
            
            float startX = this.getX();
            float totalHeight = chromaBtnHeight + padding + size + padding;
            if(colorSetting.isShowAlpha()) totalHeight += (12 * scale) + padding;
            float startY = this.getY() - (totalHeight) - (5 * scale);
            
            if(MouseUtils.isInside(mouseX, mouseY, startX, startY, size + 18 * scale, chromaBtnHeight)) {
                colorSetting.setRainbow(!colorSetting.isRainbow());
                return;
            }
            
            float currentY = startY + chromaBtnHeight + padding;
            if(MouseUtils.isInside(mouseX, mouseY, startX, currentY, size, size)) sbDragging = true;
            if(MouseUtils.isInside(mouseX, mouseY, startX + size + (6 * scale), currentY, 12 * scale, size)) hueDragging = true;
            
            currentY += size + padding;
            if(colorSetting.isShowAlpha() && MouseUtils.isInside(mouseX, mouseY, startX, currentY, size + 18 * scale, 12 * scale)) alphaDragging = true;
        }
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            hueDragging = false;
            sbDragging = false;
            alphaDragging = false;
        }
    }

    public boolean isInsidePreview(int mouseX, int mouseY) {
        return MouseUtils.isInside(mouseX, mouseY, this.getX() + (106 * scale), this.getY(), 16 * scale, 16 * scale);
    }
}