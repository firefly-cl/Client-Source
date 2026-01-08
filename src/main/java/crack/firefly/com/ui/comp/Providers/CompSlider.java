package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompSlider extends Comp {

    private NumberSetting setting;
    private boolean dragging;

    public CompSlider(NumberSetting setting) {
        super(0,0);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        // CORREÇÃO: Usando getMinValue e getMaxValue
        double min = setting.getMinValue();
        double max = setting.getMaxValue();
        double current = setting.getValue();
        
        float w = 100;
        float h = 4;
        
        if (dragging) {
            double diff = Math.min(w, Math.max(0, mouseX - getX()));
            double newValue = min + (diff / w) * (max - min);
            
            BigDecimal bd = new BigDecimal(newValue);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            setting.setValue(bd.doubleValue());
            current = setting.getValue();
        }
        
        float sliderFillW = (float) ((current - min) / (max - min) * w);
        
        // Fundo (Cinza)
        nvg.drawRoundedRect(getX(), getY() + 6, w, h, 2, new Color(40, 40, 40));
        
        // Preenchimento (Cor do Client)
        nvg.drawRoundedRect(getX(), getY() + 6, sliderFillW, h, 2, GuiModMenu.ACCENT_COLOR);
        
        // Indicador (Quadrado Branco)
        nvg.drawRoundedRect(getX() + sliderFillW - 4, getY() + 3, 8, 10, 3, Color.WHITE);
        
        String valString = String.valueOf(current);
        if(valString.endsWith(".0")) valString = valString.replace(".0", "");
        
        nvg.drawText(valString, getX() + w + 8, getY() + 10, Color.WHITE, 9, Fonts.REGULAR);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtils.isInside(mouseX, mouseY, getX(), getY(), 100, 16) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}
}