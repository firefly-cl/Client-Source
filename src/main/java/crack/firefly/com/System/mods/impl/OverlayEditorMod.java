package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventFireOverlay;
import crack.firefly.com.System.event.impl.EventRenderPumpkinOverlay;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;

public class OverlayEditorMod extends Mod {

	private BooleanSetting hidePumpkinSetting = new BooleanSetting(TranslateText.HIDE_PUMPKIN, this, false);
	private BooleanSetting hideFireSetting = new BooleanSetting(TranslateText.HIDE_FIRE, this, false);
	
	public OverlayEditorMod() {
		super(TranslateText.OVERLAY_EDITOR, TranslateText.OVERLAY_EDITOR_DESCRIPTION, ModCategory.RENDER);
	}
	
	@EventTarget
	public void onRenderPumpkinOverlay(EventRenderPumpkinOverlay event) {
		event.setCancelled(hidePumpkinSetting.isToggled());
	}
	
	@EventTarget
	public void onFireOverlay(EventFireOverlay event) {
		event.setCancelled(hideFireSetting.isToggled());
	}
}
