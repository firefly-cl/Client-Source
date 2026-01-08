package crack.firefly.com.System.cape;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.Support.animation.ColorAnimation;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;

public enum CapeCategory {
	ALL(TranslateText.ALL.getText()), MINECON("Minecon"), FLAG("Flags"), SOAR("Soar"), CARTOON("Cartoon"), MISC("Misc"), CUSTOM("Custom");
	
	private String name;
	private SimpleAnimation backgroundAnimation = new SimpleAnimation();
	private ColorAnimation textColorAnimation = new ColorAnimation();
	
	private CapeCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SimpleAnimation getBackgroundAnimation() {
		return backgroundAnimation;
	}

	public ColorAnimation getTextColorAnimation() {
		return textColorAnimation;
	}
}
