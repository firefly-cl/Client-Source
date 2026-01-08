package crack.firefly.com.System.mods.impl;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.menus.GuiQuickPlay;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;

public class HypixelQuickPlayMod extends Mod {

	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_N);
	
	public HypixelQuickPlayMod() {
		super(TranslateText.HYPIXEL_QUICK_PLAY, TranslateText.HYPIXEL_QUICK_PLAY_DESCRIPTION, ModCategory.PLAYER);
	}

	@EventTarget
	public void onKey(EventKey event) {
		
		if(event.getKeyCode() == keybindSetting.getKeyCode()) {
			mc.displayGuiScreen(new GuiQuickPlay());
		}
	}
}
