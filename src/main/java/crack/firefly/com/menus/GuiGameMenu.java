package crack.firefly.com.menus;

import java.io.IOException;
import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.easing.EaseLiner;
import crack.firefly.com.Support.buffer.ScreenAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.Support.OptifineUtils;
import crack.firefly.com.Support.render.BlurUtils; // IMPORT DO BLUR
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Providers.CosmeticsFamily;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiGameMenu extends GuiScreen {

    private static final ResourceLocation LOGO_TEXTURE = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");
    private final float LOGO_WIDTH_ORIGINAL = 1987.0F;
    private final float LOGO_HEIGHT_ORIGINAL = 457.0F;

    private Animation introAnimation;
    private final ScreenAnimation screenAnimation;

    private int x, y, width, height;
    private int centre, scaledWidth, scaledHeight;
    private final float buttonHeight = 24F; 
    private final float standardSpacing = 28F;
    private final float initialOffset = 55F;

    private boolean wasFastRenderEnabled;

    public GuiGameMenu() {
        this.screenAnimation = new ScreenAnimation();
    }

    @Override
    public void initGui() {
        // Anti-bug Fast Render
        wasFastRenderEnabled = OptifineUtils.isFastRenderEnabled();
        if (wasFastRenderEnabled) OptifineUtils.disableFastRender();

        ScaledResolution sr = new ScaledResolution(mc);
        scaledWidth = sr.getScaledWidth();
        scaledHeight = sr.getScaledHeight();
        centre = scaledWidth / 2;
        width = 220;
        x = centre - width / 2;
        y = scaledHeight / 2 - 130;
        height = 260;

        introAnimation = new EaseLiner(80, 1.0F);
        introAnimation.setDirection(Direction.FORWARDS);
        Firefly.getInstance().getNanoVGManager().loadImage(LOGO_TEXTURE);
    }

    @Override
    public void onGuiClosed() {
        if (wasFastRenderEnabled) OptifineUtils.enableFastRender();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // --- ADICIONADO O BLUR IGUAL AO SOAR CLIENT ---
        BlurUtils.drawBlurScreen(20); 

        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        screenAnimation.wrap(() -> drawNanoVG(nvg, mouseX, mouseY),
                x, y, width, height,
                2 - introAnimation.getValueFloat(),
                Math.min(introAnimation.getValueFloat(), 1),
                false);

        if (introAnimation.isDone(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawNanoVG(NanoVGManager nvg, int mouseX, int mouseY) {
        nvg.drawRect(-5, -5, scaledWidth + 10, scaledHeight + 10, new Color(0,0,0,100));

        float logoScaleFactor = width / LOGO_WIDTH_ORIGINAL;
        float logoHeight = LOGO_HEIGHT_ORIGINAL * logoScaleFactor;
        float logoY = y + initialOffset / 2.0F - logoHeight / 2.0F - 10; 
        nvg.drawImage(LOGO_TEXTURE, x, logoY, width, logoHeight);

        float offset = initialOffset;
        drawButton(nvg, I18n.format("menu.returnToGame"), x, y + offset, width, buttonHeight, mouseX, mouseY);
        offset += standardSpacing;

        drawSideBySideButtons(nvg, "Emotes", "Cosmetics", offset, mouseX, mouseY);
        offset += standardSpacing;

        drawButton(nvg, TranslateText.OPEN_MOD_MENU.getText(), x, y + offset, width, buttonHeight, mouseX, mouseY);
        offset += standardSpacing;

        String shareOrHUD = (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic())
                ? I18n.format("menu.shareToLan") : TranslateText.EDIT_HUD.getText();
        drawSideBySideButtons(nvg, I18n.format("menu.multiplayer"), shareOrHUD, offset, mouseX, mouseY);
        offset += standardSpacing;

        drawSideBySideButtons(nvg, I18n.format("menu.options"), I18n.format("gui.stats"), offset, mouseX, mouseY);
        offset += standardSpacing;

        String exitText = mc.isIntegratedServerRunning() ? TranslateText.EXIT_WORLD_SINGLEPLAYER.getText() : I18n.format("menu.disconnect");
        drawButton(nvg, exitText, x, y + offset, width, buttonHeight, mouseX, mouseY);
    }

    private void drawButton(NanoVGManager nvg, String text, float bx, float by, float bw, float bh, int mouseX, int mouseY) {
        boolean isHovered = MouseUtils.isInside(mouseX, mouseY, bx, by, bw, bh);
        nvg.drawRoundedRect(bx, by, bw, bh, 4, isHovered ? new Color(30, 30, 30, 200) : new Color(0, 0, 0, 160));
        nvg.drawOutlineRoundedRect(bx, by, bw, bh, 4, 1.0f, isHovered ? Color.WHITE : new Color(255, 255, 255, 80));
        nvg.drawCenteredText(text, bx + bw / 2, by + bh / 2 - 4, isHovered ? Color.WHITE : new Color(220, 220, 220), 10F, Fonts.MEDIUM);
    }

    private void drawSideBySideButtons(NanoVGManager nvg, String s1, String s2, float offset, int mouseX, int mouseY) {
        float gap = 6;
        float colWidth = (width - gap) / 2F;
        drawButton(nvg, s1, x, y + offset, colWidth, buttonHeight, mouseX, mouseY);
        drawButton(nvg, s2, x + colWidth + gap, y + offset, colWidth, buttonHeight, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            float gap = 6;
            float colWidth = (width - gap) / 2F;
            float offset = initialOffset;

            if (inside(mouseX, mouseY, x, y + offset)) introAnimation.setDirection(Direction.BACKWARDS);
            offset += standardSpacing;

            if (inside(mouseX, mouseY, x, y + offset, colWidth)) System.out.println("Clicked Emotes");
            if (inside(mouseX, mouseY, x + colWidth + gap, y + offset, colWidth)) {
                GuiModMenu modMenu = Firefly.getInstance().getModMenu();
                modMenu.selectCategory(CosmeticsFamily.class);
                mc.displayGuiScreen(modMenu);
            }
            offset += standardSpacing;

            if (inside(mouseX, mouseY, x, y + offset)) mc.displayGuiScreen(Firefly.getInstance().getModMenu());
            offset += standardSpacing;

            if (inside(mouseX, mouseY, x, y + offset, colWidth)) mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            
            if (inside(mouseX, mouseY, x + colWidth + gap, y + offset, colWidth)) {
                if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
                    mc.displayGuiScreen(new GuiShareToLan(this));
                } else {
                    mc.displayGuiScreen(new GuiEditHUD(false)); 
                }
            }
            offset += standardSpacing;

            if (inside(mouseX, mouseY, x, y + offset, colWidth)) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            offset += standardSpacing;

            if (inside(mouseX, mouseY, x, y + offset)) {
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                mc.displayGuiScreen(new GuiMainMenu());
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean inside(int mx, int my, float bx, float by) { return MouseUtils.isInside(mx, my, bx, by, width, buttonHeight); }
    private boolean inside(int mx, int my, float bx, float by, float bw) { return MouseUtils.isInside(mx, my, bx, by, bw, buttonHeight); }
}