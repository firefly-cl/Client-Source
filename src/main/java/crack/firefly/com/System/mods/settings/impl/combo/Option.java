package crack.firefly.com.System.mods.settings.impl.combo;

import crack.firefly.com.System.language.TranslateText;

public class Option {

	private TranslateText nameTranslate;
	
	public Option(TranslateText nameTranslate) {
		this.nameTranslate = nameTranslate;
	}

	public String getName() {
		return nameTranslate.getText();
	}

	public String getNameKey() {
		return nameTranslate.getKey();
	}
	
	public TranslateText getTranslate() {
		return nameTranslate;
	}
}
