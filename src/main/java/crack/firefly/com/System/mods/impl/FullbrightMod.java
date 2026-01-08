package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventGamma;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class FullbrightMod extends Mod {

	public FullbrightMod() {
		super(TranslateText.FULLBRIGHT, TranslateText.FULLBRIGHT_DESCRIPTION, ModCategory.PLAYER);
	}

	@EventTarget
	public void onGamma(EventGamma event) {
		event.setGamma(20F);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
        mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
        mc.renderGlobal.loadRenderers();
	}
}
