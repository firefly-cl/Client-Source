package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;

public class TaplookMod extends Mod {

	private boolean active;
	private int prevPerspective;
	
	private ComboSetting modeSetting = new ComboSetting(TranslateText.PERSPECTIVE, this, TranslateText.FRONT, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.FRONT), new Option(TranslateText.BEHIND))));
	
	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_P);
	
	public TaplookMod() {
		super(TranslateText.TAPLOOK, TranslateText.TAPLOOK_DESCRIPTION, ModCategory.PLAYER);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if(keybindSetting.isKeyDown()) {
			if(!active) {
				this.start();
			}
		}else if(active) {
			this.stop();
		}
	}
	
	private void start() {
		
		Option option = modeSetting.getOption();
		int perspective = option.getTranslate().equals(TranslateText.FRONT) ? 2 : 1;
		
		active = true;
		prevPerspective = mc.gameSettings.thirdPersonView;
		mc.gameSettings.thirdPersonView = perspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}
	
	private void stop() {
		active = false;
		mc.gameSettings.thirdPersonView = prevPerspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}
}
