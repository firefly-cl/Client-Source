package crack.firefly.com.menus.mod;

import java.awt.Color;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.emote.EmoteManager;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.client.gui.GuiScreen;

public class GuiEmoteWheel extends GuiScreen {

    private final float RADIUS = 110f;
    private final float INNER_RADIUS = 35f;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        EmoteManager em = EmoteManager.getInstance();
        
        float cx = width / 2f;
        float cy = height / 2f;

        // Fundo escurecido
        nvg.drawRect(0, 0, width, height, new Color(0, 0, 0, 100));

        int slices = em.emotes.length;
        float step = 360f / slices;

        double diffX = mouseX - cx;
        double diffY = mouseY - cy;
        double angle = Math.toDegrees(Math.atan2(diffY, diffX)) + 90;
        if (angle < 0) angle += 360;
        
        double dist = Math.sqrt(diffX * diffX + diffY * diffY);
        int hovered = (dist > INNER_RADIUS && dist < RADIUS + 40) ? (int)(angle / step) : -1;

        for (int i = 0; i < slices; i++) {
            boolean isHover = (i == hovered);
            float startDeg = i * step;
            
            float textAngle = (float) Math.toRadians(startDeg + (step / 2) - 90);
            float tx = cx + (float)Math.cos(textAngle) * (RADIUS - 20);
            float ty = cy + (float)Math.sin(textAngle) * (RADIUS - 20);

            if (isHover) {
                nvg.drawCircle(tx, ty, 32, new Color(0, 235, 130, 200)); 
            } else {
                nvg.drawCircle(tx, ty, 28, new Color(0, 0, 0, 150));
                // Simulando contorno de círculo usando rounded rect (que sabemos que seu client tem)
                nvg.drawOutlineRoundedRect(tx - 28, ty - 28, 56, 56, 28, 1.2f, new Color(255, 255, 255, 50));
            }
            
            nvg.drawCenteredText(em.emotes[i], tx, ty + 4, Color.WHITE, 8, Fonts.SEMIBOLD);
        }

        // Círculo Central
        nvg.drawCircle(cx, cy, INNER_RADIUS, new Color(15, 15, 15, 255));
        nvg.drawOutlineRoundedRect(cx - INNER_RADIUS, cy - INNER_RADIUS, INNER_RADIUS * 2, INNER_RADIUS * 2, INNER_RADIUS, 2f, Color.WHITE);
        nvg.drawCenteredText("FIREFLY", cx, cy + 3, Color.WHITE, 7, Fonts.SEMIBOLD);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            EmoteManager em = EmoteManager.getInstance();
            double diffX = mouseX - (width / 2f);
            double diffY = mouseY - (height / 2f);
            double dist = Math.sqrt(diffX * diffX + diffY * diffY);
            
            if (dist > INNER_RADIUS) {
                double angle = Math.toDegrees(Math.atan2(diffY, diffX)) + 90;
                if (angle < 0) angle += 360;
                int slice = (int)(angle / (360f / em.emotes.length));
                
                if (slice >= 0 && slice < em.emotes.length) {
                    String selected = em.emotes[slice];
                    if (selected.equals("STOP")) em.stopEmote();
                    else em.playEmote(selected);
                    mc.displayGuiScreen(null);
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}