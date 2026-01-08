package crack.firefly.com.Support.search;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class SearchBox {

    private float x, y, width, height;
    private String text = "";
    private boolean focused;
    
    private SimpleAnimation focusAnimation = new SimpleAnimation(0.0F);

    public SearchBox() {}

    public void setPosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        focusAnimation.setAnimation(focused ? 1.0F : 0.0F, 14);
        float anim = focusAnimation.getValue();
        
        // Cores
        int alphaBg = (int) (80 + (60 * anim)); 
        Color bgColor = new Color(0, 0, 0, alphaBg);
        
        int alphaBorder = (int) (30 + (120 * anim));
        Color borderColor = new Color(255, 255, 255, alphaBorder);

        // Fundo e Borda (Raio 4 = Quadrado suave)
        nvg.drawRoundedRect(x, y, width, height, 4, bgColor);
        nvg.drawOutlineRoundedRect(x, y, width, height, 4, 1.0F, borderColor);

        float iconSize = 12; 
        float fontSize = 10; 
        
        // --- CORREÇÃO DE ALINHAMENTO VERTICAL ---
        // Centraliza baseado no topo (Y) + metade da altura da caixa - metade da altura do texto
        float iconY = y + (height - iconSize) / 2f + 1.0f;
        float textY = y + (height - fontSize) / 2f + 1.5f; 
        
        Color iconColor = new Color(200, 200, 200, (int)(150 + (105 * anim)));
        
        // Ícone de Lupa
        try {
            nvg.drawText(LegacyIcon.SEARCH, x + 8, iconY, iconColor, iconSize, Fonts.LEGACYICON);
        } catch (Exception e) {
            // Fallback se não tiver ícone
            nvg.drawText("Q", x + 8, iconY, iconColor, iconSize, Fonts.SEMIBOLD);
        }

        float textX = x + 26; 
        float maxTextWidth = width - 34;

        nvg.save();
        nvg.scissor(textX, y, maxTextWidth, height);

        if (text.isEmpty() && !focused) {
            nvg.drawText("Search...", textX, textY, new Color(150, 150, 150, 150), fontSize, Fonts.REGULAR);
        } else {
            float textWidth = nvg.getTextWidth(text, fontSize, Fonts.REGULAR);
            float renderX = textX;
            
            // Scroll do texto para a esquerda se for muito longo
            if (focused && textWidth > maxTextWidth) {
                renderX = textX - (textWidth - maxTextWidth);
            }

            nvg.drawText(text, renderX, textY, Color.WHITE, fontSize, Fonts.REGULAR);
            
            // Cursor piscante
            if (focused && (System.currentTimeMillis() % 1000 > 500)) {
                float cursorX = renderX + textWidth + 1;
                float cursorH = 8;
                float cursorY = y + (height - cursorH) / 2f;
                nvg.drawRect(cursorX, cursorY, 1, cursorH, Color.WHITE);
            }
        }
        nvg.restore();
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height)) {
            this.focused = true;
        } else {
            this.focused = false;
        }
    }
    
    public void keyTyped(char typedChar, int keyCode) {
        if (!isFocused()) return;
        
        if (keyCode == Keyboard.KEY_V && GuiScreen.isCtrlKeyDown()) {
            try {
                String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if (clipboard != null && !clipboard.isEmpty()) this.text += clipboard;
            } catch (Exception e) {}
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) { 
            this.focused = false; return;
        }

        if (keyCode == Keyboard.KEY_BACK) { 
            if (text.length() > 0) text = text.substring(0, text.length() - 1);
            return;
        }

        if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
             this.text += typedChar;
        }
    }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) { this.focused = focused; }
}