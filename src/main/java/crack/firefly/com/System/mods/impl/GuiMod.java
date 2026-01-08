package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;

public class GuiMod extends Mod {

    private static GuiMod instance;

    // Usando TranslateText.INVENTORY_SCALE
    private final NumberSetting inventoryScale = new NumberSetting(TranslateText.INVENTORY_SCALE, this, 1.0, 0.5, 1.5, false);
    
    // Usando TranslateText.HOTBAR_SCALE
    private final NumberSetting hotbarScale = new NumberSetting(TranslateText.HOTBAR_SCALE, this, 1.0, 0.5, 1.5, false);

    public GuiMod() {
        // Usando TranslateText.GUI e GUI_DESCRIPTION
        super(TranslateText.GUI, TranslateText.GUI_DESCRIPTION, ModCategory.RENDER);
        instance = this;
    }

    public static GuiMod getInstance() {
        return instance;
    }

    public float getInventoryScale() {
        return (float) inventoryScale.getValue();
    }

    public float getHotbarScale() {
        return (float) hotbarScale.getValue();
    }
}