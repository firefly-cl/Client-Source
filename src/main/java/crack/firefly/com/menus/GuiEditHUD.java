package crack.firefly.com.menus;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.easing.EaseBackIn;
import crack.firefly.com.Support.mouse.MouseUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.nanovg.NanoVG;

public class GuiEditHUD extends GuiScreen {

    private Animation introAnimation;
    private boolean fromModMenu;
    private ArrayList<HUDMod> mods;
    
    private HUDMod resizingMod = null;
    private float startScale = 1.0f;
    private int startMouseX = 0;

    public GuiEditHUD(boolean fromModMenu) {
        this.fromModMenu = fromModMenu;
        this.mods = Firefly.getInstance().getModManager().getHudMods();
    }

    @Override
    public void initGui() {
        resizingMod = null;
        introAnimation = new EaseBackIn(400, 1.0F, 0F);
        introAnimation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        if(introAnimation.isDone(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        nvg.setupAndDraw(() -> {
            NanoVG.nvgGlobalAlpha(nvg.getContext(), (float) introAnimation.getValue());

            for(HUDMod m : mods) {
                if(!m.isToggled()) continue;
                
                // RESIZE UNIFORME USANDO ESCALA
                if (resizingMod != null && resizingMod.equals(m)) {
                    int deltaX = mouseX - startMouseX;
                    float scaleFactor = (float) deltaX / Math.max(m.getBaseWidth(), 10);
                    m.setScale(startScale + scaleFactor);
                }

                if(m.isDragging() && resizingMod == null && m.isDraggable()) {
                    m.setX(mouseX + m.getDraggingX());
                    m.setY(mouseY + m.getDraggingY());
                }

                float mx = m.getX(), my = m.getY(), mw = m.getWidth(), mh = m.getHeight();
                boolean isOver = MouseUtils.isInside(mouseX, mouseY, mx, my, mw, mh);

                // Bordas exatas
                Color borderCol = (isOver || m == resizingMod) ? Color.WHITE : new Color(255, 255, 255, 60);
                nvg.drawRect(mx, my, mw, 0.5f, borderCol);
                nvg.drawRect(mx, my + mh, mw, 0.5f, borderCol);
                nvg.drawRect(mx, my, 0.5f, mh, borderCol);
                nvg.drawRect(mx + mw, my, 0.5f, mh, borderCol);

                // Bolinha Badlion
                if (isOver || m == resizingMod) {
                    float dot = 3.0f;
                    nvg.drawRoundedRect(mx + mw - dot, my + mh - dot, dot * 2, dot * 2, dot, Color.WHITE);
                }
            }
        });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(button == 0) {
            for(int i = mods.size() - 1; i >= 0; i--) {
                HUDMod m = mods.get(i);
                if(!m.isToggled()) continue;

                // 1. Bolinha de Resize (Canto inferior direito)
                if (MouseUtils.isInside(mouseX, mouseY, m.getX() + m.getWidth() - 10, m.getY() + m.getHeight() - 10, 20, 20)) {
                    resizingMod = m;
                    startScale = m.getScale();
                    startMouseX = mouseX;
                    return;
                }

                // 2. Mod (Arraste)
                if (m.isDraggable() && MouseUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth(), m.getHeight())) {
                    m.setDragging(true);
                    m.setDraggingX(m.getX() - mouseX);
                    m.setDraggingY(m.getY() - mouseY);
                    return;
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mods.forEach(m -> m.setDragging(false));
        resizingMod = null;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            if(fromModMenu) mc.displayGuiScreen(Firefly.getInstance().getModMenu());
            else introAnimation.setDirection(Direction.BACKWARDS);
        }
    }
}