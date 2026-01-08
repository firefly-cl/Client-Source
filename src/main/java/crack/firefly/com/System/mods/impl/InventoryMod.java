package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.util.ResourceLocation; 
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;

public class InventoryMod extends Mod {

    private static InventoryMod instance;

    private BooleanSetting animationSetting = new BooleanSetting(TranslateText.ANIMATION, this, false);
    private ComboSetting animationTypeSetting = new ComboSetting(TranslateText.ANIMATION_TYPE, this, TranslateText.NORMAL, new ArrayList<Option>(Arrays.asList(
            new Option(TranslateText.NORMAL), new Option(TranslateText.BACKIN))));
    private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    
    // NOVA OPÇÃO DE BLUR ADICIONADA AQUI
    private BooleanSetting blurSetting = new BooleanSetting(TranslateText.BLUR, this, true);
    
    private BooleanSetting preventPotionShiftSetting = new BooleanSetting(TranslateText.PREVENT_POTION_SHIFT, this, true);
    private BooleanSetting particleSetting = new BooleanSetting(TranslateText.PARTICLE, this, false);
    private BooleanSetting logoSetting = new BooleanSetting(TranslateText.LOGO, this, true);
    
    private NumberSetting guiScaleSetting = new NumberSetting(TranslateText.SCALE, this, 1.0, 0.5, 2.0, false);
    
    private final ResourceLocation logoLocation = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");

    public InventoryMod() {
        super(TranslateText.INVENTORY, TranslateText.INVENTORY_DESCRIPTION, ModCategory.OTHER);
        instance = this;
    }

    public static InventoryMod getInstance() {
        return instance;
    }

    public BooleanSetting getAnimationSetting() { return animationSetting; }
    public ComboSetting getAnimationTypeSetting() { return animationTypeSetting; }
    public BooleanSetting getBackgroundSetting() { return backgroundSetting; }
    
    // GETTER PARA A OPÇÃO DE BLUR
    public BooleanSetting getBlurSetting() { return blurSetting; }
    
    public BooleanSetting getPreventPotionShiftSetting() { return preventPotionShiftSetting; }
    public BooleanSetting getParticleSetting() { return particleSetting; }
    public BooleanSetting getLogoSetting() { return logoSetting; }
    public ResourceLocation getLogoLocation() { return logoLocation; }
    
    public NumberSetting getGuiScaleSetting() {
        return guiScaleSetting;
    }
}