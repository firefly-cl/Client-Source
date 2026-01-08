package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompComboBox extends Comp {

    private ComboSetting setting;
    private boolean expanded;
    
    public CompComboBox(int id, ComboSetting setting) {
        super(0,0);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        float w = 100;
        float h = 18;
        
        // Fundo do Botão (Cinza Escuro, Raio 3)
        nvg.drawRoundedRect(getX(), getY(), w, h, 3, new Color(30, 30, 30, 200));
        nvg.drawOutlineRoundedRect(getX(), getY(), w, h, 3, 1f, new Color(255, 255, 255, 20));
        
        // Texto Selecionado (Usa getName() direto, pois sabemos que existe)
        nvg.drawText(setting.getOption().getName(), getX() + 6, getY() + 11.5f, Color.WHITE, 9, Fonts.MEDIUM);
        
        // Ícone Seta (Texto simples para evitar erro de ícone)
        String arrow = expanded ? "^" : "v";
        nvg.drawCenteredText(arrow, getX() + w - 10, getY() + 12f, Color.GRAY, 8, Fonts.REGULAR);
        
        // --- LISTA EXPANDIDA ---
        if (expanded) {
            float dropY = getY() + h + 2;
            float itemHeight = 16;
            
            // Usamos o tamanho da lista
            int size = setting.getOptions().size();
            float totalH = size * itemHeight;
            
            // Fundo da Lista
            nvg.drawRoundedRect(getX(), dropY, w, totalH, 3, new Color(25, 25, 25, 250));
            nvg.drawOutlineRoundedRect(getX(), dropY, w, totalH, 3, 1f, new Color(255, 255, 255, 20));
            
            // Loop por índice para evitar erros de Tipo/Importação
            for (int i = 0; i < size; i++) {
                // Ao acessar pelo get(i), o compilador infere o tipo Option corretamente
                String modeName = setting.getOptions().get(i).getName();
                boolean isSelected = setting.getOptions().get(i).equals(setting.getOption());
                
                boolean hover = MouseUtils.isInside(mouseX, mouseY, getX(), dropY, w, itemHeight);
                
                if (isSelected) {
                    nvg.drawRoundedRect(getX() + 2, dropY, w - 4, itemHeight, 2, GuiModMenu.ACCENT_COLOR);
                } else if (hover) {
                    nvg.drawRoundedRect(getX() + 2, dropY, w - 4, itemHeight, 2, new Color(255, 255, 255, 20));
                }
                
                Color textColor = isSelected ? Color.BLACK : (hover ? Color.WHITE : Color.GRAY);
                nvg.drawCenteredText(modeName, getX() + w/2, dropY + 10.5f, textColor, 9, Fonts.MEDIUM);
                
                dropY += itemHeight;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float w = 100;
        float h = 18;
        
        if (MouseUtils.isInside(mouseX, mouseY, getX(), getY(), w, h) && mouseButton == 0) {
            expanded = !expanded;
            return;
        }
        
        if (expanded) {
            float dropY = getY() + h + 2;
            float itemHeight = 16;
            int size = setting.getOptions().size();
            
            for (int i = 0; i < size; i++) {
                if (MouseUtils.isInside(mouseX, mouseY, getX(), dropY, w, itemHeight) && mouseButton == 0) {
                    // Define a opção pelo índice
                    setting.setOption(setting.getOptions().get(i));
                    expanded = false;
                    return;
                }
                dropY += itemHeight;
            }
            
            float totalListH = size * itemHeight;
            if (!MouseUtils.isInside(mouseX, mouseY, getX(), dropY - totalListH, w, totalListH)) {
               expanded = false;
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}
}