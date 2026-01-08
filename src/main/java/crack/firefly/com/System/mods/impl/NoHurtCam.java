package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventHurtCamera;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class NoHurtCam extends Mod {

    private static NoHurtCam instance;
    
    // Configuração de intensidade proveniente do MinimalDamageShake
    private NumberSetting intensitySetting = new NumberSetting(TranslateText.INTENSITY, this, 0, 0, 100, true);
    
    public NoHurtCam() {
    super(TranslateText.NO_HURT_CAM, TranslateText.NO_HURT_CAM_DESCRIPTION, ModCategory.RENDER, "nohurtcam");
    instance = this;
 }
    @EventTarget
    public void onHurtCamera(EventHurtCamera event) {
        // Aplica a intensidade customizada na câmera ao levar dano
        event.setIntensity(intensitySetting.getValueFloat() / 100F);
    }

    public static NoHurtCam getInstance() {
        return instance;
    }
    
    public NumberSetting getIntensitySetting() {
        return intensitySetting;
    }
}