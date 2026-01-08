package crack.firefly.com.System.mods.impl;

import org.lwjgl.input.Keyboard;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.menus.mod.GuiEmoteWheel;

public class EmoteMod extends Mod {

    public EmoteMod() {
        // Corrigido: usando Enums do seu TranslateText.java
        super(TranslateText.COSMETICS, TranslateText.NONE, ModCategory.PLAYER, "emote_wheel");
        this.setToggled(true);
    }

    @EventTarget
    public void onKey(EventKey event) {
        if (event.getKeyCode() == Keyboard.KEY_B && mc.currentScreen == null) {
            mc.displayGuiScreen(new GuiEmoteWheel());
        }
    }
}