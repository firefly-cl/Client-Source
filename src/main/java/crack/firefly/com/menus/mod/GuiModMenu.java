package crack.firefly.com.menus.mod;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.GuiEditHUD;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.menus.mod.Family.Providers.*;
import crack.firefly.com.System.event.impl.EventRenderNotification;
import crack.firefly.com.System.mods.impl.InternalSettingsMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.easing.EaseOutBack;
import crack.firefly.com.Support.buffer.ScreenAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.Support.mouse.Scroll;
import crack.firefly.com.Support.render.BlurUtils;
import crack.firefly.com.Support.search.SearchBox;
import crack.firefly.com.Support.OptifineUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiModMenu extends GuiScreen {

    private Animation introAnimation;
    private float x, y;
    private int width, height;

    private ArrayList<Category> categories = new ArrayList<>();
    private Category currentCategory;

    private ScreenAnimation screenAnimation = new ScreenAnimation();
    private Scroll scroll = new Scroll();
    private SearchBox searchBox = new SearchBox();

    private boolean toEditHUD, canClose;
    private boolean wasFastRenderEnabled;

    public static final Color ACCENT_COLOR = new Color(0, 235, 130); 
    public static final Color FIREFLY_GREEN = ACCENT_COLOR; 
    public static final Color BG_COLOR = new Color(0, 0, 0, 140); 
    public static final Color SIDEBAR_BG = new Color(0, 0, 0, 80); 
    public static final Color TEXT_INACTIVE = new Color(180, 180, 180);
    
    private final int SIDEBAR_WIDTH = 130;
    private static final ResourceLocation LOGO = new ResourceLocation("minecraft", "Firefly/gui/main/menu/Horizontal_branca.png");

    public GuiModMenu() {
        categories.add(new ModFamily(this));
        categories.add(new SettingFamily(this));
        categories.add(new ProfileFamily(this));
        categories.add(new CosmeticsFamily(this));
        currentCategory = getCategoryByClass(ModFamily.class);
    }

    public boolean isCanClose() { return canClose; }
    public void setCanClose(boolean b) { this.canClose = b; }

    public void selectCategory(Class<?> clazz) {
        Category c = getCategoryByClass(clazz);
        if (c != null) {
            this.currentCategory = c;
            this.scroll.resetAll();
        }
    }

    public Category getCategoryByClass(Class<?> clazz) {
        for (Category c : categories) if (c.getClass().equals(clazz)) return c;
        return null;
    }

    @Override
    public void initGui() {
        wasFastRenderEnabled = OptifineUtils.isFastRenderEnabled();
        if (wasFastRenderEnabled) OptifineUtils.disableFastRender();

        ScaledResolution sr = new ScaledResolution(mc);
        
        // --- VALORES FIXOS COMPACTOS (ESTILO BADLION) ---
        this.width = 450;
        this.height = 240; // Altura bem menor para não ficar "alto"

        this.x = (sr.getScaledWidth() - this.width) / 2.0f;
        this.y = (sr.getScaledHeight() - this.height) / 2.0f;

        introAnimation = new EaseOutBack(450, 1.0, 1.2f); 
        introAnimation.setDirection(Direction.FORWARDS);

        for (Category c : categories) {
            c.initGui();
        }
        scroll.resetAll();
        toEditHUD = false;
        canClose = true;
    }

    @Override
    public void onGuiClosed() {
        if (wasFastRenderEnabled) OptifineUtils.setFastRender(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (sr.getScaledWidth() != (int)(this.x * 2 + this.width)) {
            initGui();
        }

        if (introAnimation.isDone(Direction.BACKWARDS)) {
            mc.displayGuiScreen(toEditHUD ? new GuiEditHUD(true) : null);
            return;
        }

        float animVal = introAnimation.getValueFloat();
        if (InternalSettingsMod.getInstance().getBlurSetting().isToggled()) {
            BlurUtils.drawBlurScreen(Math.min(animVal, 1.0f) * 15F);
        }
        
        screenAnimation.wrap(() -> drawNanoVG(mouseX, mouseY, partialTicks), 
                this.x, this.y, this.width, this.height, animVal, Math.min(animVal, 1.0f), true);

        new EventRenderNotification().call();
    }

    private void drawNanoVG(int mouseX, int mouseY, float partialTicks) {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        boolean isModsTab = currentCategory instanceof ModFamily;
        boolean settingsOpen = isModsTab && ((ModFamily) currentCategory).isSettingsOpen();

        nvg.drawRoundedRect(x, y, width, height, 4, BG_COLOR);
        nvg.drawOutlineRoundedRect(x, y, width, height, 4, 1f, new Color(255, 255, 255, 30));

        if (!settingsOpen) {
            nvg.drawRoundedRect(x, y, SIDEBAR_WIDTH, height, 4, SIDEBAR_BG); 
            nvg.drawRect(x + SIDEBAR_WIDTH, y, 1, height, new Color(255, 255, 255, 10));
            
            if(LOGO != null) nvg.drawImage(LOGO, x + (SIDEBAR_WIDTH - 80)/2, y + 12, 80, 18);

            float catY = y + 45; // Ajustado para a nova altura
            for (Category c : categories) {
                boolean selected = c.equals(currentCategory);
                if (selected) {
                    nvg.drawRect(x, catY, 3, 22, ACCENT_COLOR); 
                    nvg.drawRect(x + 3, catY, SIDEBAR_WIDTH - 3, 22, new Color(255, 255, 255, 15));
                }
                nvg.drawText(getCleanName(c), x + 20, catY + 6, selected ? Color.WHITE : TEXT_INACTIVE, 9, Fonts.MEDIUM);
                catY += 28;
            }

            float btnH = 22; 
            float btnW = SIDEBAR_WIDTH - 20; 
            float btnX = x + 10; 
            float btnY = y + height - btnH - 10;
            boolean hoverEdit = MouseUtils.isInside(mouseX, mouseY, btnX, btnY, btnW, btnH);
            nvg.drawRoundedRect(btnX, btnY, btnW, btnH, 3, hoverEdit ? ACCENT_COLOR : new Color(0, 0, 0, 80));
            nvg.drawCenteredText("EDIT GUI", btnX + btnW/2, btnY + 7, hoverEdit ? Color.BLACK : Color.WHITE, 8, Fonts.SEMIBOLD);
        }

        if (isModsTab && !settingsOpen) {
            float searchW = 120;
            float searchH = 16;
            float searchX = x + width - searchW - 15;
            float searchY = y + 12;
            searchBox.setPosition(searchX, searchY, searchW, searchH);
            searchBox.draw(mouseX, mouseY, partialTicks);
        }

        int contentX = (int) (settingsOpen ? x : x + SIDEBAR_WIDTH);
        int contentW = (int) (settingsOpen ? width : width - SIDEBAR_WIDTH);
        
        if (currentCategory != null) {
            nvg.save(); 
            nvg.scissor(contentX, y, contentW, height);
            if (!currentCategory.isInitialized()) { 
                currentCategory.setInitialized(true); 
                currentCategory.initCategory(); 
            }
            currentCategory.drawScreen(mouseX, mouseY, partialTicks);
            nvg.restore();
        }
        
        scroll.onScroll(); 
        scroll.onAnimation();
    }

    private String getCleanName(Category c) {
        if (c instanceof ModFamily) return "Mods";
        if (c instanceof SettingFamily) return "Settings";
        if (c instanceof ProfileFamily) return "Profiles";
        if (c instanceof CosmeticsFamily) return "Cosmetics";
        return "Unknown";
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (introAnimation.getValueFloat() < 0.5f) return;
        boolean isModsTab = currentCategory instanceof ModFamily;
        boolean settingsOpen = isModsTab && ((ModFamily) currentCategory).isSettingsOpen();
        
        if (!settingsOpen) {
            float catY = y + 45;
            for (Category c : categories) {
                if (MouseUtils.isInside(mouseX, mouseY, x, catY, SIDEBAR_WIDTH, 22) && mouseButton == 0) {
                    currentCategory = c; 
                    scroll.resetAll();
                }
                catY += 28;
            }
            float btnY = y + height - 32;
            if (MouseUtils.isInside(mouseX, mouseY, x + 10, btnY, SIDEBAR_WIDTH - 20, 22) && mouseButton == 0) {
                toEditHUD = true; 
                introAnimation.setDirection(Direction.BACKWARDS); 
                return;
            }
        }

        if (isModsTab && !settingsOpen) searchBox.mouseClicked(mouseX, mouseY, mouseButton);

        if (!MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0 && canClose) {
            introAnimation.setDirection(Direction.BACKWARDS);
        }
        if (currentCategory != null) currentCategory.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (currentCategory != null) {
            currentCategory.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (introAnimation.getValueFloat() < 0.5f) return;
        
        boolean isModsTab = currentCategory instanceof ModFamily;
        if (isModsTab && ((ModFamily)currentCategory).isBinding()) {
            currentCategory.keyTyped(typedChar, keyCode); 
            return; 
        }

        boolean settingsOpen = isModsTab && ((ModFamily) currentCategory).isSettingsOpen();
        if (isModsTab && !settingsOpen) {
            searchBox.keyTyped(typedChar, keyCode);
        }

        if (currentCategory != null) currentCategory.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE && canClose) introAnimation.setDirection(Direction.BACKWARDS);
    }
    
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getSidebarWidth() { return SIDEBAR_WIDTH; }
    public Scroll getScroll() { return scroll; }
    public SearchBox getSearchBox() { return searchBox; }
}