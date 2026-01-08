package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class ToggleSneakMod extends Mod {

	private boolean toggle;
	
	public ToggleSneakMod() {
		super(TranslateText.TOGGLE_SNEAK, TranslateText.TOGGLE_SNEAK_DESCRIPTION, ModCategory.PLAYER);
	}

	@Override
	public void setup() {
		toggle = false;
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKeyCode() == mc.gameSettings.keyBindSneak.getKeyCode()) {
			toggle = !toggle;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.currentScreen instanceof Gui) {
			setSneak(false);
		}else {
			setSneak(toggle);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		toggle = false;
		setSneak(false);
	}
	
	private void setSneak(boolean state) {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), state);
	}
}
