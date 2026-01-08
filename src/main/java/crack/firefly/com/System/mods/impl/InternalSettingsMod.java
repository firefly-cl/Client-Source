package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.*;
import crack.firefly.com.System.mods.settings.impl.combo.Option;

public class InternalSettingsMod extends Mod {

    private static InternalSettingsMod instance;

    private final ComboSetting modThemeSetting = new ComboSetting(TranslateText.HUD_THEME, this, TranslateText.NORMAL, new ArrayList<Option>(Arrays.asList(
            new Option(TranslateText.NORMAL), new Option(TranslateText.GLOW), new Option(TranslateText.OUTLINE), new Option(TranslateText.VANILLA),
            new Option(TranslateText.OUTLINE_GLOW), new Option(TranslateText.VANILLA_GLOW), new Option(TranslateText.SHADOW),
            new Option(TranslateText.DARK), new Option(TranslateText.LIGHT), new Option(TranslateText.RECT), new Option(TranslateText.MODERN),
            new Option(TranslateText.TEXT), new Option(TranslateText.GRADIENT_SIMPLE))));

    private final BooleanSetting blurSetting = new BooleanSetting(TranslateText.UI_BLUR, this, false);
    private final BooleanSetting mcFontSetting = new BooleanSetting(TranslateText.MC_FONT, this, false);
    private final NumberSetting volumeSetting = new NumberSetting(TranslateText.VOLUME, this, 0.8, 0, 1, false);
    private final KeybindSetting modMenuKeybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_RSHIFT);
    private final TextSetting capeNameSetting = new TextSetting(TranslateText.CUSTOM_CAPE, this, "None");
    private final BooleanSetting clickEffectsSetting = new BooleanSetting(TranslateText.CLICK_EFFECT, this, true);
    private final BooleanSetting soundsUISetting = new BooleanSetting(TranslateText.UI_SOUNDS, this, true);

    // --- NOVAS CONFIGURAÇÕES ---
    private final BooleanSetting borderless = new BooleanSetting(TranslateText.BORDERLESS, this, true);
    private final BooleanSetting minDamageShake = new BooleanSetting(TranslateText.MIN_DAMAGE, this, false);
    private final BooleanSetting minViewBobbing = new BooleanSetting(TranslateText.MIN_BOBBING, this, false);
    private final BooleanSetting hotbarScroll = new BooleanSetting(TranslateText.HOTBAR_SCROLL, this, true);
    private final BooleanSetting fastRender = new BooleanSetting(TranslateText.FAST_RENDER, this, false);
    
    // --- FONTE GLOBAL (Novo) ---
    // Usando TranslateText.FONT que adicionamos anteriormente
    private final BooleanSetting globalFontSetting = new BooleanSetting(TranslateText.FONT, this, false);

    public InternalSettingsMod() {
        super(TranslateText.NONE, TranslateText.NONE, ModCategory.OTHER);
        instance = this;
    }

    @Override
    public void setup() {
        this.setHide(true);
        this.setToggled(true);
    }

    @EventTarget
    public void onKey(EventKey event) {
        if(event.getKeyCode() == modMenuKeybindSetting.getKeyCode()) {
            mc.displayGuiScreen(Firefly.getInstance().getModMenu());
        }
    }

    public static InternalSettingsMod getInstance() {return instance;}

    public BooleanSetting getClickEffectsSetting(){return clickEffectsSetting;}
    public BooleanSetting getSoundsUISetting(){return soundsUISetting;}
    public NumberSetting getVolumeSetting() {return volumeSetting;}
    public ComboSetting getModThemeSetting() {return modThemeSetting;}
    public BooleanSetting getBlurSetting() {return blurSetting;}
    public KeybindSetting getModMenuKeybindSetting() {return modMenuKeybindSetting;}
    public BooleanSetting getMCHUDFont() {return mcFontSetting;}
    public String getCapeConfigName(){return capeNameSetting.getText();}
    public void setCapeConfigName(String a){capeNameSetting.setText(a);}

    public BooleanSetting getBorderlessSetting() { return borderless; }
    public BooleanSetting getMinDamageShakeSetting() { return minDamageShake; }
    public BooleanSetting getMinViewBobbingSetting() { return minViewBobbing; }
    public BooleanSetting getHotbarScrollSetting() { return hotbarScroll; }
    public BooleanSetting getFastRenderSetting() { return fastRender; }
    
    // Getter para a nova opção
    public BooleanSetting getGlobalFontSetting() { return globalFontSetting; }
}