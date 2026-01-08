package crack.firefly.com.System.mods.impl;

import java.awt.Color;

import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.ServerUtils;

public class PingDisplayMod extends HUDMod {

    // Configurações padronizadas para não conflitar
    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);
    
    // Opção para mostrar o "ms" ou apenas o número
    private BooleanSetting label = new BooleanSetting(TranslateText.TEXT, this, true);

    public PingDisplayMod() {
        super(TranslateText.PING_DISPLAY, TranslateText.PING_DISPLAY_DESCRIPTION);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        // Pega o ping
        int ping = ServerUtils.getPing();
        
        // Formata o texto (ex: "50 ms" ou "50")
        String text = label.isToggled() ? ping + " ms" : String.valueOf(ping);
        
        // Pega o scale (Tamanho definido no Edit HUD)
        float scale = this.getScale();
        
        // Cálculos de tamanho base
        int baseStringWidth = mc.fontRendererObj.getStringWidth(text);
        int baseFontHeight = mc.fontRendererObj.FONT_HEIGHT;
        float paddingX = 6f;
        float paddingY = 4f;
        
        // Tamanho final com scale aplicado
        float w = (baseStringWidth + (paddingX * 2)) * scale;
        float h = (baseFontHeight + (paddingY * 2)) * scale;
        
        // Atualiza tamanho do módulo
        this.setWidth((int) w);
        this.setHeight((int) h);
        
        float x = this.getX();
        float y = this.getY();
        
        // 1. Desenha o fundo
        nvg.setupAndDraw(() -> {
            if (background.isToggled()) {
                nvg.drawRoundedRect(x, y, w, h, 4, bgColor.getColor());
            }
        });

        // 2. Desenha o texto com Scale e Fonte do Minecraft
        GL11.glPushMatrix();
        
        // Move para o centro do retângulo
        float centerX = x + (w / 2f);
        float centerY = y + (h / 2f);
        GL11.glTranslatef(centerX, centerY, 0);
        
        // Aplica o redimensionamento
        GL11.glScalef(scale, scale, 1f);
        
        // Desenha centralizado (Subtrai metade do tamanho original para alinhar no 0,0)
        mc.fontRendererObj.drawStringWithShadow(text, -baseStringWidth / 2f, -(baseFontHeight / 2f) + 1f, textColor.getColor().getRGB());
        
        GL11.glPopMatrix();
    }
}