package crack.firefly.com.System.mods.settings.impl;

import java.io.File;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;

public class SoundSetting extends Setting {

	private File sound;
	
	public SoundSetting(TranslateText nameTranslate, Mod parent) {
		super(nameTranslate, parent);
		
		this.sound = null;
		
		Firefly.getInstance().getModManager().addSettings(this);
	}

	@Override
	public void reset() {
		this.sound = null;
	}

	public File getSound() {
		return sound;
	}

	public void setSound(File sound) {
		this.sound = sound;
	}
}
