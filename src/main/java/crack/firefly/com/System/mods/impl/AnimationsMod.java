package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class AnimationsMod extends Mod {

    private static AnimationsMod instance;
    
    private BooleanSetting blockHitSetting = new BooleanSetting(TranslateText.BLOCK_HIT, this, true);
    private BooleanSetting pushingSetting = new BooleanSetting(TranslateText.PUSHING, this, true);
    private BooleanSetting pushingParticleSetting = new BooleanSetting(TranslateText.PUSHING_PARTICLES, this, true);
    private BooleanSetting sneakSetting = new BooleanSetting(TranslateText.SNEAK, this, true);
    private BooleanSetting smoothSneakSetting = new BooleanSetting(TranslateText.SNEAKSMOOTH, this, false);
    private NumberSetting smoothSneakSpeedSetting = new NumberSetting(TranslateText.SMOOTH_SPEED, this, 6, 0.5, 20, false);
    private BooleanSetting healthSetting = new BooleanSetting(TranslateText.HEALTH, this, true);
    private BooleanSetting armorDamageSetting = new BooleanSetting(TranslateText.ARMOR_DAMAGE, this, false);
    private BooleanSetting itemSwitchSetting = new BooleanSetting(TranslateText.ITEM_SWITCH, this, false);
    private BooleanSetting rodSetting = new BooleanSetting(TranslateText.ROD, this, false);

    private BooleanSetting alwaysSwingSetting = new BooleanSetting(TranslateText.ALWAYS_SWING, this, true);
    private BooleanSetting bow17 = new BooleanSetting(TranslateText.BOW, this, true);
    private BooleanSetting eating17 = new BooleanSetting(TranslateText.EATING, this, true);
    private BooleanSetting swing17 = new BooleanSetting(TranslateText.SWING_17, this, true);
    private BooleanSetting damageFlashSetting = new BooleanSetting(TranslateText.DAMAGE_FLASH, this, true);
    private BooleanSetting lowFireSetting = new BooleanSetting(TranslateText.LOW_FIRE, this, true);
    private NumberSetting itemScaleSetting = new NumberSetting(TranslateText.ITEM_SCALE, this, 1.0, 0.1, 1.5, false);

    public AnimationsMod() {
        super(TranslateText.OLD_ANIMATION, TranslateText.OLD_ANIMATION_DESCRIPTION, ModCategory.RENDER, "oldoam1.7full");
        instance = this;
    }

    public static AnimationsMod getInstance() { return instance; }

    public BooleanSetting getBlockHitSetting() { return blockHitSetting; }
    public BooleanSetting getPushingSetting() { return pushingSetting; }
    public BooleanSetting getPushingParticleSetting() { return pushingParticleSetting; }
    public BooleanSetting getSneakSetting() { return sneakSetting; }
    public BooleanSetting getSmoothSneakSetting() { return smoothSneakSetting; }
    public float getSmoothSneakSpeedSetting() { return smoothSneakSpeedSetting.getValueFloat(); }
    public BooleanSetting getHealthSetting() { return healthSetting; }
    public BooleanSetting getArmorDamageSetting() { return armorDamageSetting; }
    public BooleanSetting getItemSwitchSetting() { return itemSwitchSetting; }
    public BooleanSetting getRodSetting() { return rodSetting; }
    public BooleanSetting getAlwaysSwingSetting() { return alwaysSwingSetting; }
    public BooleanSetting getBowSetting() { return bow17; }
    public BooleanSetting getEatingSetting() { return eating17; }
    public BooleanSetting getSwing17() { return swing17; }
    public BooleanSetting getDamageFlashSetting() { return damageFlashSetting; }
    public BooleanSetting getLowFireSetting() { return lowFireSetting; }
    public float getItemScale() { return itemScaleSetting.getValueFloat(); }
}