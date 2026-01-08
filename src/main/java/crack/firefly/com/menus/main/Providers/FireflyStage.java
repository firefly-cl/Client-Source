package crack.firefly.com.menus.main.Providers;

import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import crack.firefly.com.Firefly;
import org.lwjgl.opengl.GL11;
import crack.firefly.com.menus.main.FireflyUI;
import crack.firefly.com.menus.main.LandingView;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.Support.SkinUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FireflyStage extends LandingView {

    // Controle Global do Tema (Mantido falso por padrão já que o botão sumiu)
    public static boolean isChristmas = false;

    private final ResourceLocation horizontalLogoTexture = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");
    private final ResourceLocation singleplayerTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 3.png");
    private final ResourceLocation multiplayerTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 4.png");
    private final ResourceLocation quitTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 5.png");
    
    private final ResourceLocation storeIconTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 6.png");
    private final ResourceLocation settingsIconTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 7.png");
    private final ResourceLocation shirtIconTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 8.png");
    private final ResourceLocation discordIconTexture = new ResourceLocation("Firefly/gui/main/menu/Ativo 9.png");
    
    private final float LOGO_W = 240f; 
    private final float LOGO_H = 55f; 
    private final float BTN_W = 200f; 
    private final float BTN_H = 24f; 
    private final float BTN_GAP = 8f; 
    private final float LOGO_GAP = 30f; 
    private final float ICON_SIZE = 24f;
    private final float ICON_GAP = 12f;

    // Sistema de Neve
    private final List<Snowflake> snowflakes = new ArrayList<>();
    private final Random random = new Random();
    
    public FireflyStage(FireflyUI parent) {
        super(parent);
        for (int i = 0; i < 120; i++) {
            snowflakes.add(new Snowflake());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (mc.getFramebuffer() != null) mc.getFramebuffer().bindFramebuffer(false);

        nvg.setupAndDraw(() -> {
            render(nvg, mouseX, mouseY);
        });
    }

    private void render(NanoVGManager nvg, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        float sw = sr.getScaledWidth();
        float sh = sr.getScaledHeight();

        // --- DESENHAR NEVE ---
        if (isChristmas) {
            for (Snowflake flake : snowflakes) {
                flake.update(sw, sh);
                nvg.drawCircle(flake.x, flake.y, flake.size, new Color(255, 255, 255, 180));
            }
        }

        // Logo e Botões Centrais
        float totalH = LOGO_H + LOGO_GAP + (3 * BTN_H) + (2 * BTN_GAP);
        float startY = (sh - totalH) / 2f;
        float centerX = sw / 2f;
        nvg.drawImage(horizontalLogoTexture, centerX - (LOGO_W / 2f), startY, LOGO_W, LOGO_H);

        float btnStartY = startY + LOGO_H + LOGO_GAP;
        ResourceLocation[] buttons = { singleplayerTexture, multiplayerTexture, quitTexture };
        for (int i = 0; i < buttons.length; i++) {
            float btnY = btnStartY + (i * (BTN_H + BTN_GAP));
            float btnX = centerX - (BTN_W / 2f);
            if (MouseUtils.isInside(mouseX, mouseY, btnX, btnY, BTN_W, BTN_H)) {
                nvg.drawRoundedRect(btnX, btnY, BTN_W, BTN_H, 5, new Color(255, 255, 255, 20));
            }
            nvg.drawImage(buttons[i], btnX, btnY, BTN_W, BTN_H);
        }
        
        // Ícones Sociais (Agora com 4 ícones)
        float quitButtonBottomY = btnStartY + (2 * (BTN_H + BTN_GAP)) + BTN_H;
        float finalSocialY = Math.max(sh - 50f, quitButtonBottomY + 25f);
        
        // Removido o natalIconTexture da lista
        ResourceLocation[] icons = { storeIconTexture, settingsIconTexture, shirtIconTexture, discordIconTexture };
        float totalIconW = (ICON_SIZE * icons.length) + (ICON_GAP * (icons.length - 1));
        float iconStartX = (sw - totalIconW) / 2f;
        
        for (int i = 0; i < icons.length; i++) {
            float iconX = iconStartX + i * (ICON_SIZE + ICON_GAP);
            float drawY = finalSocialY - (ICON_SIZE / 2f);
            
            boolean hovered = MouseUtils.isInside(mouseX, mouseY, iconX, drawY, ICON_SIZE, ICON_SIZE);
            if (hovered) {
                 nvg.drawRoundedRect(iconX, drawY, ICON_SIZE, ICON_SIZE, 5, new Color(255, 255, 255, 30));
            }

            nvg.drawImage(icons[i], iconX, drawY, ICON_SIZE, ICON_SIZE);
        }

        // --- CONTA ---
        String username = mc.getSession().getUsername();
        ResourceLocation face = SkinUtils.getAvatar(username);
        float nameWidth = nvg.getTextWidth(username, 10, Fonts.MEDIUM);
        float accW = 40 + nameWidth + 20; 
        float accH = 28;
        float accX = sw - accW - 15; 
        float accY = 15; 

        boolean accHovered = MouseUtils.isInside(mouseX, mouseY, accX, accY, accW, accH);
        nvg.drawRoundedRect(accX, accY, accW, accH, 8, accHovered ? new Color(255, 255, 255, 30) : new Color(0, 0, 0, 130));
        nvg.drawOutlineRoundedRect(accX, accY, accW, accH, 8, 1f, new Color(255, 255, 255, 20));

        nvg.drawRoundedImage(face, accX + 6, accY + 6, 16, 16, 4);
        nvg.drawText(username, accX + 30, accY + 10, Color.WHITE, 10, Fonts.MEDIUM);
        nvg.drawText(LegacyIcon.CHEVRON_DOWN, accX + accW - 15, accY + 11, new Color(200, 200, 200, 150), 7, Fonts.LEGACYICON);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0) return;
        ScaledResolution sr = new ScaledResolution(mc);
        float sw = sr.getScaledWidth();
        float sh = sr.getScaledHeight();

        float btnStartY = ((sh - (LOGO_H + LOGO_GAP + (3 * BTN_H) + (2 * BTN_GAP))) / 2f) + LOGO_H + LOGO_GAP;
        float btnStartX = (sw / 2f) - (BTN_W / 2f);

        if (MouseUtils.isInside(mouseX, mouseY, btnStartX, btnStartY, BTN_W, BTN_H)) {
            mc.displayGuiScreen(new GuiSelectWorld(getParent()));
        } else if (MouseUtils.isInside(mouseX, mouseY, btnStartX, btnStartY + BTN_H + BTN_GAP, BTN_W, BTN_H)) {
            mc.displayGuiScreen(new GuiMultiplayer(getParent()));
        } else if (MouseUtils.isInside(mouseX, mouseY, btnStartX, btnStartY + 2 * (BTN_H + BTN_GAP), BTN_W, BTN_H)) {
            mc.shutdown();
        }

        // Cliques nos Ícones (Ajustado para 4 ícones)
        float finalSocialY = Math.max(sh - 50f, (btnStartY + 3 * BTN_H + 2 * BTN_GAP) + 25f);
        float totalIconW = (ICON_SIZE * 4) + (ICON_GAP * 3);
        float iconStartX = (sw - totalIconW) / 2f;

        for (int i = 0; i < 4; i++) {
            float iconX = iconStartX + i * (ICON_SIZE + ICON_GAP);
            if (MouseUtils.isInside(mouseX, mouseY, iconX, finalSocialY - (ICON_SIZE/2f), ICON_SIZE, ICON_SIZE)) {
                switch (i) {
                    case 0: openUrl("https://store.fireflyclient.com"); break;
                    case 1: mc.displayGuiScreen(new GuiOptions(getParent(), mc.gameSettings)); break;
                    case 2: mc.displayGuiScreen(Firefly.getInstance().getModMenu()); break;
                    case 3: openUrl("https://discord.gg/firefly"); break;
                }
            }
        }
    }
    
    private void openUrl(String url) {
        try { Desktop.getDesktop().browse(new URI(url)); } catch (Exception ignored) {}
    }

    private class Snowflake {
        float x, y, size, speed;
        Snowflake() { reset(true); }
        void reset(boolean first) {
            this.x = random.nextFloat() * new ScaledResolution(mc).getScaledWidth();
            this.y = first ? random.nextFloat() * 500 : -10;
            this.size = 1.0f + random.nextFloat() * 2.0f;
            this.speed = 0.5f + random.nextFloat() * 1.2f;
        }
        void update(float sw, float sh) {
            y += speed;
            x += Math.sin(y / 15f) * 0.4f;
            if (y > sh) reset(false);
        }
    }
}