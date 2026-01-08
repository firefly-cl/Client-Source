package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import net.minecraft.util.ResourceLocation;

public class TabEditorMod extends Mod {

	private static TabEditorMod instance;
	
	private BooleanSetting backgroundSetting = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private BooleanSetting headSetting = new BooleanSetting(TranslateText.HEAD, this, true);
    
    // Opção no menus (aparecerá como "Icon")
    private BooleanSetting showClientLogo = new BooleanSetting(TranslateText.ICON, this, true);
    
    // CORREÇÃO:
    // 1. Mudamos "Firefly" para "firefly" (minúsculo)
    // 2. Mudamos "tab_icon.png" para "icontab.png" (o nome que você salvou)
    private static final ResourceLocation CLIENT_ICON = new ResourceLocation("Firefly/logo/icontab.png");

	public TabEditorMod() {
		super(TranslateText.TAB_EDITOR, TranslateText.TAB_EDITOR_DESCRIPTION, ModCategory.RENDER);
		instance = this;
	}

	public static TabEditorMod getInstance() {
		return instance;
	}

	public BooleanSetting getBackgroundSetting() {
		return backgroundSetting;
	}

	public BooleanSetting getHeadSetting() {
		return headSetting;
	}
	
	public BooleanSetting getShowClientLogo() {
		return showClientLogo;
	}
	
	public ResourceLocation getClientIcon() {
		return CLIENT_ICON;
	}
}