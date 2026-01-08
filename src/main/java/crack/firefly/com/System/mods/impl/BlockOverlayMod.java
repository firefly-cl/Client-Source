package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import java.awt.Color;

public class BlockOverlayMod extends Mod {

    private static BlockOverlayMod instance;

    // Configurações da Borda (Outline)
    private BooleanSetting outline = new BooleanSetting(TranslateText.OUTLINE, this, true);
    private ColorSetting outlineColor = new ColorSetting(TranslateText.OUTLINE_COLOR, this, Color.WHITE, false);
    private NumberSetting outlineAlpha = new NumberSetting(TranslateText.OUTLINE_ALPHA, this, 255, 0, 255, true);
    private NumberSetting width = new NumberSetting(TranslateText.OUTLINE_WIDTH, this, 2.0, 0.5, 5.0, false);
    
    // Configurações do Preenchimento (Fill)
    private BooleanSetting fill = new BooleanSetting(TranslateText.FILL, this, true);
    private ColorSetting fillColor = new ColorSetting(TranslateText.FILL_COLOR, this, new Color(255, 255, 255, 100), false);
    private NumberSetting fillAlpha = new NumberSetting(TranslateText.FILL_ALPHA, this, 100, 0, 255, true);

    // Chroma Global
    private BooleanSetting chroma = new BooleanSetting(TranslateText.CHROMA, this, false);
    private NumberSetting chromaSpeed = new NumberSetting(TranslateText.CHROMA_SPEED, this, 2.0, 0.5, 10.0, false);

    public BlockOverlayMod() {
        super(TranslateText.BLOCK_OVERLAY, TranslateText.BLOCK_OVERLAY_DESCRIPTION, ModCategory.RENDER, "blockoverlay");
        instance = this;
    }

    public static BlockOverlayMod getInstance() { return instance; }

    /**
     * Retorna a cor da borda processada (Chroma ou Fixa)
     */
    public Color getFinalOutlineColor() {
        return getProcessedColor(outlineColor.getColor(), outlineAlpha.getValueInt());
    }

    /**
     * Retorna a cor do preenchimento processada (Chroma ou Fixa)
     */
    public Color getFinalFillColor() {
        return getProcessedColor(fillColor.getColor(), fillAlpha.getValueInt());
    }

    private Color getProcessedColor(Color baseColor, int alpha) {
        if (chroma.isToggled()) {
            float speed = 5000f / chromaSpeed.getValueFloat();
            float hue = (System.currentTimeMillis() % (int)speed) / speed;
            Color c = Color.getHSBColor(hue, 0.6f, 1f);
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
        }
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
    }

    public BooleanSetting getOutline() { return outline; }
    public BooleanSetting getFill() { return fill; }
    public NumberSetting getWidth() { return width; }
    public NumberSetting getOutlineAlpha() { return outlineAlpha; }
    public NumberSetting getFillAlpha() { return fillAlpha; }
}