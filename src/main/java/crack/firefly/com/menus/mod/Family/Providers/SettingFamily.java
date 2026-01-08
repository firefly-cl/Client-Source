package crack.firefly.com.menus.mod.Family.Providers;

import java.awt.Color;
import java.util.ArrayList;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.impl.InternalSettingsMod;
import crack.firefly.com.System.mods.settings.Setting;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.ui.comp.Providers.CompComboBox;
import crack.firefly.com.ui.comp.Providers.CompKeybind;
import crack.firefly.com.ui.comp.Providers.CompToggleButton;
import crack.firefly.com.Support.mouse.MouseUtils;

public class SettingFamily extends Category {

    private final GuiModMenu modMenu;
    private final ArrayList<MenuElement> elements = new ArrayList<>();

    // Cores estilo Badlion/Clean
    private final Color TEXT_HEADER = new Color(160, 160, 160); // Cinza claro para títulos
    private final Color TEXT_PRIMARY = new Color(220, 220, 220); // Branco fumaça para opções
    private final Color LINE_COLOR = new Color(255, 255, 255, 20);

    private float listX, listY, listW, listH;

    public SettingFamily(GuiModMenu parent) {
        super(parent, TranslateText.SETTINGS, LegacyIcon.SETTINGS, false, false);
        this.modMenu = parent;
    }

    @Override
    public void initGui() {
        elements.clear();
        InternalSettingsMod internal = InternalSettingsMod.getInstance();

        // --- GRUPO: MENUS ---
        addHeader("Menus");
        addSetting(internal.getModMenuKeybindSetting());
        addSetting(internal.getBlurSetting());
        addSetting(internal.getSoundsUISetting());
        addSetting(internal.getGlobalFontSetting());

        // --- GRUPO: VISUAL / GAMEPLAY ---
        addHeader("Visual & Gameplay");
        addSetting(internal.getClickEffectsSetting());
        addSetting(internal.getBorderlessSetting());
        addSetting(internal.getFastRenderSetting());
        addSetting(internal.getMinDamageShakeSetting());
        addSetting(internal.getHotbarScrollSetting());
        addSetting(internal.getMinViewBobbingSetting());
    }
    
    private void addHeader(String title) {
        elements.add(new MenuElement(title));
    }

    private void addSetting(Setting s) {
        if(s == null) return;
        Comp comp = null;
        if (s instanceof BooleanSetting) comp = new CompToggleButton((BooleanSetting) s);
        else if (s instanceof KeybindSetting) comp = new CompKeybind(0, (KeybindSetting) s);
        else if (s instanceof ComboSetting) comp = new CompComboBox(0, (ComboSetting) s);
        
        if (comp != null) elements.add(new MenuElement(s, comp));
    }

    @Override
    public void initCategory() {
        scroll.resetAll();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();

        float padding = 20;
        float sidebarW = modMenu.getSidebarWidth();
        
        this.listX = modMenu.getX() + sidebarW + padding;
        this.listY = modMenu.getY() + padding;
        this.listW = modMenu.getWidth() - sidebarW - (padding * 2);
        this.listH = modMenu.getHeight() - (padding * 2);

        if (MouseUtils.isInside(mouseX, mouseY, listX, listY, listW, listH)) {
            scroll.onScroll();
        }
        scroll.onAnimation();
        
        nvg.save();
        nvg.scissor(listX, listY, listW, listH);
        
        float currentY = listY + scroll.getValue();
        float itemHeight = 25; // Altura mais compacta igual ao Badlion
        float headerHeight = 35;

        for (MenuElement el : elements) {
            if (el.isHeader) {
                // Desenha o Título da Seção
                nvg.drawText(el.title, listX, currentY + 20, TEXT_HEADER, 11, Fonts.SEMIBOLD);
                currentY += headerHeight;
            } else {
                // Efeito de Hover (Opcional, bem sutil)
                if (MouseUtils.isInside(mouseX, mouseY, listX, currentY, listW, itemHeight)) {
                    nvg.drawRect(listX - 5, currentY, listW + 10, itemHeight, new Color(255, 255, 255, 5));
                }

                // Desenha o Nome da Configuração (Esquerda)
                nvg.drawText(el.setting.getName(), listX, currentY + 16, TEXT_PRIMARY, 10, Fonts.REGULAR);
                
                // Configura e Desenha o Componente (Direita)
                float compW = (el.comp instanceof CompComboBox) ? 85 : (el.comp instanceof CompToggleButton) ? 30 : 60;
                el.comp.setX(listX + listW - compW);
                el.comp.setY(currentY + (itemHeight - 14) / 2f);
                el.comp.draw(mouseX, mouseY, partialTicks);
                
                currentY += itemHeight;
            }
        }
        
        // Atualiza o limite do scroll baseado no Y final
        scroll.setMaxScroll(Math.max(0, (currentY - scroll.getValue()) - (listY + listH)));

        nvg.restore(); 
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtils.isInside(mouseX, mouseY, listX, listY, listW, listH)) {
            for (MenuElement el : elements) {
                if (!el.isHeader) {
                    el.comp.mouseClicked(mouseX, (int)(mouseY - scroll.getValue()), mouseButton);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (MenuElement el : elements) {
            if (!el.isHeader) el.comp.keyTyped(typedChar, keyCode);
        }
    }

    // Classe auxiliar para suportar tanto Headers quanto Settings na mesma lista
    private static class MenuElement {
        String title;
        Setting setting;
        Comp comp;
        boolean isHeader;

        // Construtor para Header
        public MenuElement(String title) {
            this.title = title;
            this.isHeader = true;
        }

        // Construtor para Setting
        public MenuElement(Setting setting, Comp comp) {
            this.setting = setting;
            this.comp = comp;
            this.isHeader = false;
        }
    }
}