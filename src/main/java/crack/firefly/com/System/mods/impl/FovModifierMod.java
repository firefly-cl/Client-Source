package crack.firefly.com.System.mods.impl;

import java.util.Collection;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventFovUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.Support.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FovModifierMod extends Mod {

    // 1. Corrigido: Usei TranslateText.FOV_MODIFIER para evitar o erro de String.
    // Esse slider será o seu multiplicador base (Ex: 1.0 é normal, 1.2 é visão mais ampla).
    private NumberSetting customFov = new NumberSetting(TranslateText.FOV_MODIFIER, this, 1, 0.1, 2.5, false);
    
    private NumberSetting sprintingSetting = new NumberSetting(TranslateText.SPRINTING, this, 0, -5, 5, false);
    private NumberSetting bowSetting = new NumberSetting(TranslateText.BOW, this, 0, -5, 5, false);
    private NumberSetting speedSetting = new NumberSetting(TranslateText.SPEED, this, 0, -5, 5, false);
    private NumberSetting slownessSetting = new NumberSetting(TranslateText.SLOWNESS, this, 0, -5, 5, false);
    
    public FovModifierMod() {
        super(TranslateText.FOV_MODIFIER, TranslateText.FOV_MODIFIER_DESCRIPTION, ModCategory.PLAYER);
        // 2. Removi o addSettings. Se a sua base for como a maioria das bases Firefly,
        // ela detecta as variáveis "NumberSetting" automaticamente por reflexão.
    }

    @EventTarget
    public void onFovUpdate(EventFovUpdate event) {
        
        // 3. O SEGREDO: Começamos com o valor do seu slider 'customFov' em vez de usar o do Minecraft.
        // Isso ignora totalmente o "Dynamic FOV" do menu de opções.
        float base = customFov.getValueFloat();
        
        EntityPlayer entity = event.getEntity();
        ItemStack item = entity.getItemInUse();
        int useDuration = entity.getItemInUseDuration();
        
        // 4. Se os sliders abaixo estiverem em 0, o FOV ficará estático.
        if(entity.isSprinting()) {
            base += 0.15F * sprintingSetting.getValueFloat();
        }
        
        if(item != null && item.getItem() == Items.bow) {
            // Lógica suave de zoom do arco
            float duration = (float)useDuration / 20.0F;
            duration = (duration > 1.0F) ? 1.0F : duration * duration;
            base -= (duration * 0.15F) * bowSetting.getValueFloat();
        }
        
        Collection<PotionEffect> effects = entity.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            int potionID = effect.getPotionID();
            // Speed
            if (potionID == 1) {
                base += 0.1F * (effect.getAmplifier() + 1) * speedSetting.getValueFloat();
            }
            // Slowness
            if (potionID == 2) {
                base += -0.075F * (effect.getAmplifier() + 1) * slownessSetting.getValueFloat();
            }
        }
        
        // 5. Forçamos o nosso valor calculado no evento.
        event.setFov(base);
    }
}