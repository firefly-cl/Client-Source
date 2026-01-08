package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventLocationSkin;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.skin.SkinManager;
import net.minecraft.util.ResourceLocation;

public class SkinProtectMod extends Mod {

    private static SkinProtectMod instance;

    public SkinProtectMod() {
        super(TranslateText.SKIN_PROTECT, TranslateText.SKIN_PROTECT_DESCRIPTION, ModCategory.PLAYER, "skin_changer");
        instance = this;
    }
    
    @EventTarget
    public void onLocationSkin(EventLocationSkin event) {
        if(mc.thePlayer != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
            
            if (SkinManager.getInstance().hasCustomSkin()) {
                event.setCancelled(true);
                event.setSkin(SkinManager.getInstance().getCustomSkin());
                // Nota: O tipo de modelo (slim/default) deve ser injetado via MixinAbstractClientPlayer
            }
        }
    }

    public static SkinProtectMod getInstance() {
        return instance;
    }
}