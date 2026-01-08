package crack.firefly.com.menus.mod.Family.Providers;

import java.awt.Color;
import java.io.File;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.System.cape.CapeManager;
import crack.firefly.com.System.cape.impl.Cape;
import crack.firefly.com.System.cape.impl.NormalCape;
import crack.firefly.com.System.skin.SkinManager;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.System.config.CosmeticsConfig;
import net.minecraft.util.ResourceLocation;

public class CosmeticsFamily extends Category {

    private final GuiModMenu modMenu;
    private String currentSubTab = "Capes";
    private final String[] tabs = {"Capes", "Skins", "Shields", "Auras"};

    public static boolean isShieldActive = false;
    private CosmeticsConfig config;

    private final Color SIDEBAR_BG = new Color(0, 0, 0, 80);
    private final Color BORDER_COLOR = new Color(255, 255, 255, 15);
    private final Color ACCENT = GuiModMenu.ACCENT_COLOR;

    private final float CARD_SIZE = 85F;
    private final float GAP = 20F;

    public CosmeticsFamily(GuiModMenu parent) {
        super(parent, TranslateText.COSMETICS, LegacyIcon.SHOPPING, false, true);
        this.modMenu = parent;
    }

    // ================= LOAD =================
    @Override
    public void initCategory() {
        scroll.resetAll();
        config = CosmeticsConfig.load();

        // shield
        isShieldActive = config.shield;

        // cape
        if (!config.cape.isEmpty()) {
            CapeManager cm = Firefly.getInstance().getCapeManager();
            for (Cape cape : cm.getCapes()) {
                if (cape.getName().equalsIgnoreCase(config.cape)) {
                    cm.setCurrentCape(cape);
                    break;
                }
            }
        }

        // skin
        SkinManager sm = SkinManager.getInstance();
        sm.setSlimModel(config.slimModel);

        if (!config.skinFile.isEmpty()) {
            for (File skin : sm.getSkinFiles()) {
                if (skin.getName().equalsIgnoreCase(config.skinFile)) {
                    sm.loadLocalSkin(skin);
                    break;
                }
            }
        }
    }

    // ================= DRAW =================
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();

        float x = modMenu.getX();
        float y = modMenu.getY();
        float width = modMenu.getWidth();
        float height = modMenu.getHeight();
        float sidebarW = modMenu.getSidebarWidth();
        float contentX = x + sidebarW;

        // sidebar
        nvg.drawRect(contentX, y, 90, height, SIDEBAR_BG);
        nvg.drawRect(contentX + 90, y, 1, height, BORDER_COLOR);

        float tY = y + 45;
        for (String tab : tabs) {
            boolean sel = currentSubTab.equals(tab);
            boolean hov = MouseUtils.isInside(mouseX, mouseY, contentX, tY - 15, 90, 30);

            if (sel) {
                nvg.drawRect(contentX, tY - 15, 2, 30, ACCENT);
                nvg.drawText(tab, contentX + 15, tY + 5, Color.WHITE, 10, Fonts.SEMIBOLD);
            } else {
                nvg.drawText(tab, contentX + 15, tY + 5,
                        hov ? Color.WHITE : new Color(180, 180, 180, 100),
                        9.5f, Fonts.MEDIUM);
            }
            tY += 35;
        }

        float mainContentX = contentX + 91;
        float mainContentW = width - (sidebarW + 91);

        nvg.drawText(currentSubTab.toUpperCase(),
                mainContentX + 25, y + 35,
                Color.WHITE, 16, Fonts.SEMIBOLD);

        if (currentSubTab.equals("Skins")) {
            SkinManager sm = SkinManager.getInstance();
            float btnX = x + width - 150;
            drawSmallBtn(nvg, btnX, y + 22, 65,
                    sm.isSlimModel() ? "Model: Slim" : "Model: Classic",
                    mouseX, mouseY);
            drawSmallBtn(nvg, btnX + 70, y + 22, 55,
                    "RESET", mouseX, mouseY);
        }

        float gridY = y + 60;
        float scrollAreaH = height - 75;

        if (MouseUtils.isInside(mouseX, mouseY, mainContentX, gridY, mainContentW, scrollAreaH)) {
            scroll.onScroll();
        }
        scroll.onAnimation();

        nvg.save();
        nvg.scissor(mainContentX, gridY, mainContentW, scrollAreaH);
        nvg.translate(0, scroll.getValue());

        drawItemsGrid(nvg, mainContentX, gridY, mainContentW, mouseX, mouseY);

        nvg.restore();
    }

    // ================= GRID (NADA REMOVIDO) =================
    private void drawItemsGrid(NanoVGManager nvg, float startX, float startY, float width, int mx, int my) {
        int col = 0, row = 0;
        int columns = 2;

        float totalGridW = (columns * CARD_SIZE) + ((columns - 1) * GAP);
        float xOffset = startX + (width - totalGridW) / 2;

        if (currentSubTab.equals("Capes")) {
            CapeManager cm = Firefly.getInstance().getCapeManager();
            for (Cape cape : cm.getCapes()) {
                float cX = xOffset + col * (CARD_SIZE + GAP);
                float cY = startY + row * (CARD_SIZE + GAP);

                boolean sel = cape.equals(cm.getCurrentCape());
                boolean hov = MouseUtils.isInside(mx, my, cX, cY + scroll.getValue(), CARD_SIZE, CARD_SIZE);

                drawCard(nvg, cX, cY, cape.getName(), sel, hov);

                if (cape instanceof NormalCape) {
                    ResourceLocation rl = ((NormalCape) cape).getSample();
                    if (rl != null)
                        nvg.drawImage(rl, cX + (CARD_SIZE - 32) / 2, cY + 12, 32, 52);
                }

                col++;
                if (col >= columns) { col = 0; row++; }
            }
        }
        else if (currentSubTab.equals("Skins")) {
            SkinManager sm = SkinManager.getInstance();
            for (File skin : sm.getSkinFiles()) {
                float cX = xOffset + col * (CARD_SIZE + GAP);
                float cY = startY + row * (CARD_SIZE + GAP);

                boolean sel = sm.getCurrentSkinName() != null
                        && sm.getCurrentSkinName().equals(skin.getName());
                boolean hov = MouseUtils.isInside(mx, my, cX, cY + scroll.getValue(), CARD_SIZE, CARD_SIZE);

                drawCard(nvg, cX, cY, skin.getName().replace(".png", ""), sel, hov);
                nvg.drawCenteredText(LegacyIcon.USER,
                        cX + CARD_SIZE / 2, cY + 38,
                        sel ? ACCENT : Color.WHITE,
                        20, Fonts.LEGACYICON);

                col++;
                if (col >= columns) { col = 0; row++; }
            }
        }
        else if (currentSubTab.equals("Shields")) {
            float cX = xOffset;
            float cY = startY;

            boolean hov = MouseUtils.isInside(mx, my, cX, cY + scroll.getValue(), CARD_SIZE, CARD_SIZE);
            drawCard(nvg, cX, cY, "White Fenix", isShieldActive, hov);

            nvg.drawImage(new ResourceLocation("Firefly/cosmetics/shield_white.png"),
                    cX + (CARD_SIZE - 50) / 2, cY + 12, 50, 50);

            row = 1;
        }

        scroll.setMaxScroll(Math.max(0,
                (row + 1) * (CARD_SIZE + GAP) - (modMenu.getHeight() - 110)));
    }

    // ================= CLICK =================
    @Override
    public void mouseClicked(int mx, int my, int mb) {
        if (mb != 0) return;

        float x = modMenu.getX() + modMenu.getSidebarWidth();
        float y = modMenu.getY();
        float width = modMenu.getWidth() - modMenu.getSidebarWidth();

        float tY = y + 45;
        for (String tab : tabs) {
            if (MouseUtils.isInside(mx, my, x, tY - 15, 90, 30)) {
                currentSubTab = tab;
                scroll.resetAll();
                return;
            }
            tY += 35;
        }

        if (currentSubTab.equals("Skins")) {
            float btnX = modMenu.getX() + modMenu.getWidth() - 150;
            SkinManager sm = SkinManager.getInstance();

            if (MouseUtils.isInside(mx, my, btnX, y + 22, 65, 16)) {
                sm.setSlimModel(!sm.isSlimModel());
                config.slimModel = sm.isSlimModel();
                config.save();
                return;
            }

            if (MouseUtils.isInside(mx, my, btnX + 70, y + 22, 55, 16)) {
                sm.resetSkin();
                config.skinFile = "";
                config.save();
                return;
            }
        }

        float mainContentX = x + 91;
        float gridY = y + 60;
        int cols = 2;

        float totalGridW = (cols * CARD_SIZE) + ((cols - 1) * GAP);
        float xOffset = mainContentX + ((width - 91) - totalGridW) / 2;

        if (currentSubTab.equals("Capes")) {
            CapeManager cm = Firefly.getInstance().getCapeManager();
            int c = 0, r = 0;

            for (Cape cape : cm.getCapes()) {
                float cX = xOffset + c * (CARD_SIZE + GAP);
                float cY = gridY + r * (CARD_SIZE + GAP) + scroll.getValue();

                if (MouseUtils.isInside(mx, my, cX, cY, CARD_SIZE, CARD_SIZE)) {
                    cm.setCurrentCape(cape);
                    config.cape = cape.getName();
                    config.save();
                    return;
                }

                c++;
                if (c >= cols) { c = 0; r++; }
            }
        }
        else if (currentSubTab.equals("Skins")) {
            SkinManager sm = SkinManager.getInstance();
            int c = 0, r = 0;

            for (File skin : sm.getSkinFiles()) {
                float cX = xOffset + c * (CARD_SIZE + GAP);
                float cY = gridY + r * (CARD_SIZE + GAP) + scroll.getValue();

                if (MouseUtils.isInside(mx, my, cX, cY, CARD_SIZE, CARD_SIZE)) {
                    sm.loadLocalSkin(skin);
                    config.skinFile = skin.getName();
                    config.save();
                    return;
                }

                c++;
                if (c >= cols) { c = 0; r++; }
            }
        }
        else if (currentSubTab.equals("Shields")) {
            float cX = xOffset;
            float cY = gridY + scroll.getValue();

            if (MouseUtils.isInside(mx, my, cX, cY, CARD_SIZE, CARD_SIZE)) {
                isShieldActive = !isShieldActive;
                config.shield = isShieldActive;
                config.save();
            }
        }
    }

    // ================= HELPERS =================
    private void drawCard(NanoVGManager nvg, float x, float y, String name, boolean sel, boolean hov) {
        nvg.drawRoundedRect(x, y, CARD_SIZE, CARD_SIZE, 5,
                hov ? new Color(255, 255, 255, 12) : new Color(0, 0, 0, 45));

        nvg.drawOutlineRoundedRect(x, y, CARD_SIZE, CARD_SIZE, 5, 1.2f,
                sel ? ACCENT : (hov ? new Color(255, 255, 255, 25) : BORDER_COLOR));

        String shortName = name.length() > 12 ? name.substring(0, 10) + ".." : name;
        nvg.drawCenteredText(shortName,
                x + CARD_SIZE / 2, y + CARD_SIZE - 12,
                Color.WHITE, 7.5f, Fonts.SEMIBOLD);

        if (sel) {
            nvg.drawCenteredText(LegacyIcon.CHECK,
                    x + CARD_SIZE - 10, y + 10,
                    ACCENT, 6, Fonts.LEGACYICON);
        }
    }

    private void drawSmallBtn(NanoVGManager nvg, float x, float y, float w, String txt, int mx, int my) {
        boolean h = MouseUtils.isInside(mx, my, x, y, w, 16);
        nvg.drawRoundedRect(x, y, w, 16, 4,
                h ? new Color(255, 255, 255, 15) : new Color(255, 255, 255, 8));
        nvg.drawCenteredText(txt,
                x + w / 2, y + 10,
                Color.WHITE, 6.5f, Fonts.SEMIBOLD);
    }

    @Override public void initGui() {}
    @Override public void keyTyped(char tc, int kc) { scroll.onKey(kc); }
}
