package crack.firefly.com.System.mods.settings.impl;

import java.io.File;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.settings.Setting;

public class ImageSetting extends Setting {

	private File image;
	
	public ImageSetting(TranslateText nameTranslate, Mod parent) {
		super(nameTranslate, parent);
		
		this.image = null;
		
		Firefly.getInstance().getModManager().addSettings(this);
	}

	@Override
	public void reset() {
		this.image = null;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}
}
