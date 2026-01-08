package crack.firefly.com.System.mods.settings.impl;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;
import crack.firefly.com.Support.ColorUtils;

public class ColorSetting extends Setting {

    private float hue, saturation, brightness;
    private int alpha;
    private Color defaultColor, color;
    private boolean showAlpha;
    
    // --- NOVO: Suporte estilo Badlion ---
    private boolean rainbow = false; // Opção Chroma
    private float rainbowSpeed = 15.0f; // Velocidade do Chroma

    public ColorSetting(TranslateText text, Mod parent, Color color, boolean showAlpha) {
        super(text, parent);
        
        this.color = color;
        this.defaultColor = color;
        
        // Converte a cor inicial para HSB
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        
        this.alpha = color.getAlpha();
        this.showAlpha = showAlpha;
        
        Firefly.getInstance().getModManager().addSettings(this);
    }
    
    // Construtor auxiliar
    public ColorSetting(TranslateText text, Mod parent, Color color) {
        this(text, parent, color, true);
    }
    
    @Override
    public void reset() {
        this.color = defaultColor;
        float[] hsb = Color.RGBtoHSB(defaultColor.getRed(), defaultColor.getGreen(), defaultColor.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = defaultColor.getAlpha();
        this.rainbow = false;
    }

    // --- LÓGICA DE COR CORRIGIDA ---
    public Color getColor() {
        if (this.rainbow) {
            try {
                // CORRIGIDO AQUI PARA O SEU COLORUTILS:
                // 1. Index: 0
                // 2. Speed: 15 (No seu ColorUtils, quanto maior, mais lento. 15 é suave).
                // 3. Alpha: 255 (Totalmente visível, depois aplicamos o alpha do slider).
                Color rainbowColor = ColorUtils.getRainbow(0, 15, 255); 
                
                // Aplica a opacidade configurada no slider sobre o arco-íris
                return ColorUtils.applyAlpha(rainbowColor, this.alpha);
            } catch (Exception e) {
                return this.color;
            }
        }
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = color.getAlpha();
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    // --- GETTERS E SETTERS HSB ---

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
        updateColor();
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
        updateColor();
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        updateColor();
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        updateColor();
    }

    private void updateColor() {
        Color c = Color.getHSBColor(hue, saturation, brightness);
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public boolean isShowAlpha() {
        return showAlpha;
    }
    
    // --- Controle do Rainbow ---
    
    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
    
    public float getRainbowSpeed() {
        return rainbowSpeed;
    }
    
    public void setRainbowSpeed(float rainbowSpeed) {
        this.rainbowSpeed = rainbowSpeed;
    }
}