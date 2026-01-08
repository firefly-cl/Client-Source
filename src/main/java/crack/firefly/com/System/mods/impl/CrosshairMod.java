package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRenderCrosshair;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class CrosshairMod extends Mod {

    // Configurações Gerais
    private BooleanSetting hideThirdPersonViewSetting = new BooleanSetting(TranslateText.HIDE_THIRD_PERSON_VIEW, this, true);
    
    // TIPO DE MIRA (Substituído por Slider para corrigir erro de compilação)
    // 0 = Cross, 1 = Circle, 2 = Square
    // Estou usando TranslateText.TYPE (se não existir, use TranslateText.CROSSHAIR)
    private NumberSetting typeSetting = new NumberSetting(TranslateText.TYPE, this, 0, 0, 2, true);
    
    // Dimensões (Agora usando os Enums corretos)
    private NumberSetting widthSetting = new NumberSetting(TranslateText.WIDTH, this, 5, 0.5, 20, false);
    private NumberSetting heightSetting = new NumberSetting(TranslateText.HEIGHT, this, 5, 0.5, 20, false);
    private NumberSetting gapSetting = new NumberSetting(TranslateText.GAP, this, 2, 0, 15, false);
    private NumberSetting thicknessSetting = new NumberSetting(TranslateText.THICKNESS, this, 1, 0.5, 5, false);
    
    // Ponto Central (Dot)
    private BooleanSetting dotSetting = new BooleanSetting(TranslateText.DOT, this, false);
    
    // Outline (Borda preta)
    private BooleanSetting outlineSetting = new BooleanSetting(TranslateText.OUTLINE, this, true);
    
    // Cores e Highlight (Adicionado 'false' no final conforme exigido pelo construtor)
    private ColorSetting colorSetting = new ColorSetting(TranslateText.COLOR, this, new Color(0, 255, 255), false);
    
    private BooleanSetting highlightPlayers = new BooleanSetting(TranslateText.HIGHLIGHT_PLAYERS, this, true);
    private BooleanSetting highlightHostiles = new BooleanSetting(TranslateText.HIGHLIGHT_HOSTILES, this, true);
    private BooleanSetting highlightPassives = new BooleanSetting(TranslateText.HIGHLIGHT_PASSIVES, this, false);
    
    private ColorSetting playerColor = new ColorSetting(TranslateText.PLAYER_COLOR, this, new Color(255, 50, 50), false);
    private ColorSetting mobColor = new ColorSetting(TranslateText.MOB_COLOR, this, new Color(255, 150, 50), false);
    private ColorSetting passiveColor = new ColorSetting(TranslateText.PASSIVE_COLOR, this, new Color(50, 255, 50), false);

    public CrosshairMod() {
        super(TranslateText.CROSSHAIR, TranslateText.CROSSHAIR_DESCRIPTION, ModCategory.RENDER);
    }

    @EventTarget
    public void onRenderCrosshair(EventRenderCrosshair event) {
        // Cancela a crosshair original do Minecraft
        event.setCancelled(true);
        
        // Verifica terceira pessoa
        if (hideThirdPersonViewSetting.isToggled() && mc.gameSettings.thirdPersonView != 0) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float centerX = sr.getScaledWidth() / 2.0f;
        float centerY = sr.getScaledHeight() / 2.0f;

        // Determina a cor baseada no que estamos olhando
        Color currentColor = colorSetting.getColor();
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
            Entity entity = mc.objectMouseOver.entityHit;
            
            if (highlightPlayers.isToggled() && entity instanceof EntityPlayer) {
                currentColor = playerColor.getColor();
            } else if (highlightHostiles.isToggled() && entity instanceof EntityMob) {
                currentColor = mobColor.getColor();
            } else if (highlightPassives.isToggled() && entity instanceof EntityAnimal) {
                currentColor = passiveColor.getColor();
            }
        }

        // Prepara OpenGL
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D(); // Importante para desenhar formas
        
        // Pega o valor inteiro do Slider (0, 1 ou 2)
        int type = (int) typeSetting.getValue();
        
        double gap = gapSetting.getValue();
        double width = widthSetting.getValue();
        double height = heightSetting.getValue();
        double thickness = thicknessSetting.getValue();
        boolean outline = outlineSetting.isToggled();

        // 1. Desenhar Outline (Preto atrás) se ativado
        if (outline) {
            drawShape(type, centerX, centerY, gap, width, height, thickness + 1.0, Color.BLACK);
        }

        // 2. Desenhar Crosshair Colorida
        drawShape(type, centerX, centerY, gap, width, height, thickness, currentColor);

        // 3. Desenhar Ponto (Dot)
        if (dotSetting.isToggled()) {
            if (outline) {
                drawRect(centerX - thickness/2 - 0.5, centerY - thickness/2 - 0.5, 
                         centerX + thickness/2 + 0.5, centerY + thickness/2 + 0.5, Color.BLACK.getRGB());
            }
            drawRect(centerX - thickness/2, centerY - thickness/2, 
                     centerX + thickness/2, centerY + thickness/2, currentColor.getRGB());
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawShape(int type, float cx, float cy, double gap, double w, double h, double t, Color color) {
        int c = color.getRGB();
        double tHalf = t / 2.0;

        switch (type) {
            case 0: // Cross
                // Top
                drawRect(cx - tHalf, cy - gap - h, cx + tHalf, cy - gap, c);
                // Bottom
                drawRect(cx - tHalf, cy + gap, cx + tHalf, cy + gap + h, c);
                // Left
                drawRect(cx - gap - w, cy - tHalf, cx - gap, cy + tHalf, c);
                // Right
                drawRect(cx + gap, cy - tHalf, cx + gap + w, cy + tHalf, c);
                break;

            case 1: // Circle
                drawHollowCircle(cx, cy, (float)(gap + 5), (float)t, c);
                break;
                
            case 2: // Square
                // Desenha um quadrado vazado (Box)
                double size = gap + w;
                // Top
                drawRect(cx - size - tHalf, cy - size - tHalf, cx + size + tHalf, cy - size + tHalf, c);
                // Bottom
                drawRect(cx - size - tHalf, cy + size - tHalf, cx + size + tHalf, cy + size + tHalf, c);
                // Left
                drawRect(cx - size - tHalf, cy - size, cx - size + tHalf, cy + size, c);
                // Right
                drawRect(cx + size - tHalf, cy - size, cx + size + tHalf, cy + size, c);
                break;
        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) { double i = left; left = right; right = i; }
        if (top < bottom) { double j = top; top = bottom; bottom = j; }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        GlStateManager.color(f, f1, f2, f3);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
    }

    public static void drawHollowCircle(float cx, float cy, float radius, float lineWidth, int color) {
        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        GlStateManager.color(f, f1, f2, f3);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= 360; i += 5) { 
            double x = Math.sin((i * Math.PI) / 180) * radius;
            double y = Math.cos((i * Math.PI) / 180) * radius;
            GL11.glVertex2d(cx + x, cy + y);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}