package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.other.SmoothStepAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompToggleButton extends Comp {

    private BooleanSetting setting;
    private Animation animation;

    public CompToggleButton(BooleanSetting setting) {
        super(0,0);
        this.setting = setting;
        // Animação suave de 150ms
        this.animation = new SmoothStepAnimation(150, 1.0);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        float w = 32;
        float h = 14;
        
        // Controla a animação (0.0 = Desligado, 1.0 = Ligado)
        animation.setDirection(setting.isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);
        float anim = animation.getValueFloat();

        // Cores estilo Badlion (Cinza Escuro -> Accent Color)
        Color offColor = new Color(40, 40, 40, 255);
        Color onColor = GuiModMenu.ACCENT_COLOR;
        
        // Mistura as cores baseada na animação
        int r = (int) (offColor.getRed() + (onColor.getRed() - offColor.getRed()) * anim);
        int g = (int) (offColor.getGreen() + (onColor.getGreen() - offColor.getGreen()) * anim);
        int b = (int) (offColor.getBlue() + (onColor.getBlue() - offColor.getBlue()) * anim);
        int a = (int) (offColor.getAlpha() + (onColor.getAlpha() - offColor.getAlpha()) * anim);
        
        // Fundo (Raio 4 = Quadrado suave)
        nvg.drawRoundedRect(getX(), getY(), w, h, 4, new Color(r, g, b, a));
        
        // Círculo/Quadrado indicador
        float knobSize = 8;
        // O indicador move do início ao fim do switch
        float knobX = getX() + 3 + ((w - 6 - knobSize) * anim);
        float knobY = getY() + (h - knobSize) / 2;
        
        // Indicador Branco (Raio 3 = Quase quadrado)
        nvg.drawRoundedRect(knobX, knobY, knobSize, knobSize, 3, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Área de clique fixa 32x14
        if (MouseUtils.isInside(mouseX, mouseY, getX(), getY(), 32, 14) && mouseButton == 0) {
            setting.setToggled(!setting.isToggled());
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}
}