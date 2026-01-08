package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import java.awt.Color;

public class AuraCosmeticMod extends Mod {
    private static AuraCosmeticMod instance;

    public BooleanSetting chroma = new BooleanSetting(TranslateText.CHROMA, this, false);
    public ColorSetting color = new ColorSetting(TranslateText.COLOR, this, new Color(0, 200, 255), false);
    public NumberSetting scale = new NumberSetting(TranslateText.SCALE, this, 1.1, 1.0, 1.3, false);
    public NumberSetting speed = new NumberSetting(TranslateText.PULSE_SPEED, this, 1.0, 0.1, 3.0, false);

    public AuraCosmeticMod() {
        super(TranslateText.AURA, TranslateText.AURA_DESCRIPTION, ModCategory.RENDER);
        instance = this;
    }

    public static AuraCosmeticMod getInstance() { return instance; }

    public Color getAuraColor() {
        if (chroma.isToggled()) {
            float h = (System.currentTimeMillis() % 4000L) / 4000.0f;
            return Color.getHSBColor(h, 0.5f, 1.0f);
        }
        return color.getColor();
    }
}