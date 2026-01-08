package crack.firefly.com.System.mods.settings.impl;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class KeybindSetting extends Setting {

	private int defaultKeyCode, keyCode;
	
	public KeybindSetting(TranslateText text, Mod parent, int keyCode) {
		super(text, parent);
		this.defaultKeyCode = keyCode;
		this.keyCode = keyCode;
		
		Firefly.getInstance().getModManager().addSettings(this);
	}

	@Override
	public void reset() {
		this.keyCode = defaultKeyCode;
	}
	
	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}
	
	public boolean isKeyDown() {
		return Keyboard.isKeyDown(keyCode) && !(Minecraft.getMinecraft().currentScreen instanceof Gui);
	}
}