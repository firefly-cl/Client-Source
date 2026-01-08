package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;
import org.lwjgl.input.Keyboard;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompKeybind extends Comp {

    private KeybindSetting setting;
    private boolean binding;

    public CompKeybind(int x, KeybindSetting setting) {
        super(0,0);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        float w = 60;
        float h = 16;
        
        boolean hover = MouseUtils.isInside(mouseX, mouseY, getX(), getY(), w, h);
        
        // Fundo (Raio 3)
        // Se estiver bindando = cor de destaque. Se não = cinza.
        Color bg = binding ? GuiModMenu.ACCENT_COLOR : (hover ? new Color(50,50,50) : new Color(30,30,30, 200));
        
        nvg.drawRoundedRect(getX(), getY(), w, h, 3, bg);
        
        // Borda sutil quando não estiver bindando
        if (!binding) {
            nvg.drawOutlineRoundedRect(getX(), getY(), w, h, 3, 1f, new Color(255,255,255, 20));
        }
        
        String keyName = binding ? "Press..." : Keyboard.getKeyName(setting.getKeyCode());
        if (keyName == null) keyName = "NONE";
        
        // Texto
        Color txtColor = binding ? Color.BLACK : Color.WHITE;
        nvg.drawCenteredText(keyName, getX() + w/2, getY() + 10.5f, txtColor, 9, Fonts.SEMIBOLD);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtils.isInside(mouseX, mouseY, getX(), getY(), 60, 16)) {
            if (mouseButton == 0) { // Clique esquerdo ativa
                binding = !binding;
            } else if (mouseButton == 1 || mouseButton == 2) { // Direito/Meio reseta
                setting.setKeyCode(0);
                binding = false;
            }
        } else if (binding) {
            binding = false; // Clicar fora cancela
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (binding) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                setting.setKeyCode(0);
            } else {
                setting.setKeyCode(keyCode);
            }
            binding = false;
        }
    }
    
    public boolean isBinding() { return binding; }
}