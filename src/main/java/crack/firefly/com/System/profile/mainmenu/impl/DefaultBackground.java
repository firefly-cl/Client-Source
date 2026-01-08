package crack.firefly.com.System.profile.mainmenu.impl;

import crack.firefly.com.System.language.TranslateText;
import net.minecraft.util.ResourceLocation;

public class DefaultBackground extends Background {

	private TranslateText nameTranslate;
	private ResourceLocation image;
	
	public DefaultBackground(int id, TranslateText nameTranslate, ResourceLocation image) {
		super(id, nameTranslate.getText());
		this.nameTranslate = nameTranslate;
		this.image = image;
	}
	
	@Override
	public String getName() {
		return nameTranslate.getText();
	}

	public String getNameKey() {
		return nameTranslate.getKey();
	}

	public ResourceLocation getImage() {
		return image;
	}
}