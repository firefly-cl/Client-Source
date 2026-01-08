package crack.firefly.com.menus.mod.Family.Providers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import crack.firefly.com.Firefly;
import org.lwjgl.input.Keyboard;

import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.menus.mod.Family.Category;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.Setting;
import crack.firefly.com.System.mods.settings.impl.*;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.ui.comp.Providers.*;
import crack.firefly.com.ui.comp.Providers.field.CompModTextBox;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.other.SmoothStepAnimation;
import crack.firefly.com.Support.mouse.MouseUtils;
import crack.firefly.com.Support.mouse.Scroll;
import net.minecraft.util.ResourceLocation;

public class ModFamily extends Category {

    private final GuiModMenu modMenu;
    private Scroll settingScroll = new Scroll();
    private boolean openSetting;
    private Animation settingAnimation;
    private Mod currentMod;

    private static class ModuleSetting { 
        public Setting setting; 
        public Comp comp; 
        public ModuleSetting(Setting s, Comp c) { this.setting = s; this.comp = c; } 
    }

    private ArrayList<ModuleSetting> comps = new ArrayList<>();

    // 🔥 GRID COMPACTO E QUADRADO (ESTILO BADLION)
    private final float CARD_SIZE = 70F; 
    private final float GAP = 8F; 

    private final Color ACCENT = GuiModMenu.ACCENT_COLOR;
    private final Color BACKGROUND_COLOR = GuiModMenu.BG_COLOR; 
    private final Color SECTION_TEXT = new Color(150, 150, 150, 200);

    private List<String> filters = Arrays.asList("All", "PvP", "Render", "Utility");
    private String currentFilter = "All";

    // ÍCONES
    private final ResourceLocation ICON_DEFAULT = new ResourceLocation("Firefly/Modsicons/mods.png");
    private final ResourceLocation ICON_ANIMATIONS = new ResourceLocation("Firefly/Modsicons/animations.png");
    private final ResourceLocation ICON_3DSKIN = new ResourceLocation("Firefly/Modsicons/skin 3d.png");
    private final ResourceLocation ICON_CAPES = new ResourceLocation("Firefly/Modsicons/capes.png");
    private final ResourceLocation ICON_FULLBRIGHT = new ResourceLocation("Firefly/Modsicons/fullbright.png");
    private final ResourceLocation ICON_MOTIONBLUR = new ResourceLocation("Firefly/Modsicons/motionblur.png");
    private final ResourceLocation ICON_GLINT = new ResourceLocation("Firefly/Modsicons/glint.png");
    private final ResourceLocation ICON_BLOOD = new ResourceLocation("Firefly/Modsicons/blood.png");
    private final ResourceLocation ICON_PARTICLES = new ResourceLocation("Firefly/Modsicons/Particle.png");
    private final ResourceLocation ICON_DAMAGEPARTICLES = new ResourceLocation("Firefly/Modsicons/Particle.png");
    private final ResourceLocation ICON_DAMAGETINT = new ResourceLocation("Firefly/Modsicons/damagetint.png");
    private final ResourceLocation ICON_CHINA = new ResourceLocation("Firefly/Modsicons/china.png");
    private final ResourceLocation ICON_TRAIL = new ResourceLocation("Firefly/Modsicons/trail.png");
    private final ResourceLocation ICON_ZOOM = new ResourceLocation("Firefly/Modsicons/zoom.png");
    private final ResourceLocation ICON_BLOCKOVERLAY = new ResourceLocation("Firefly/Modsicons/block overlay.png");
    private final ResourceLocation ICON_ITEMPHYSICS = new ResourceLocation("Firefly/Modsicons/itemPhs.png");
    private final ResourceLocation ICON_CHUNK = new ResourceLocation("Firefly/Modsicons/chunk.png");
    private final ResourceLocation ICON_FPS = new ResourceLocation("Firefly/Modsicons/fps.png");
    private final ResourceLocation ICON_CPS = new ResourceLocation("Firefly/Modsicons/cps.png");
    private final ResourceLocation ICON_KEYSTROKES = new ResourceLocation("Firefly/Modsicons/icon (11).png");
    private final ResourceLocation ICON_SCOREBOARD = new ResourceLocation("Firefly/Modsicons/scoreboard.png");
    private final ResourceLocation ICON_ARMORSTATUS = new ResourceLocation("Firefly/Modsicons/armorstatus.png");
    private final ResourceLocation ICON_POTIONS = new ResourceLocation("Firefly/Modsicons/potions.png");
    private final ResourceLocation ICON_BOSSHEALTH = new ResourceLocation("Firefly/Modsicons/boss bar.png");
    private final ResourceLocation ICON_CLOCK = new ResourceLocation("Firefly/Modsicons/Clock Display.png");
    private final ResourceLocation ICON_COMPASS = new ResourceLocation("Firefly/Modsicons/compass.png");
    private final ResourceLocation ICON_COORDS = new ResourceLocation("Firefly/Modsicons/coords.png");
    private final ResourceLocation ICON_SPEED = new ResourceLocation("Firefly/Modsicons/speedometer.png");
    private final ResourceLocation ICON_PING = new ResourceLocation("Firefly/Modsicons/ping.png");
    private final ResourceLocation ICON_TARGETHUD = new ResourceLocation("Firefly/Modsicons/targethud.png");
    private final ResourceLocation ICON_INVENTORY = new ResourceLocation("Firefly/Modsicons/inventory.png");
    private final ResourceLocation ICON_CROSSHAIR = new ResourceLocation("Firefly/Modsicons/Crosshair.png");
    private final ResourceLocation ICON_NAMETAG = new ResourceLocation("Firefly/Modsicons/nametags.png");
    private final ResourceLocation ICON_TAB = new ResourceLocation("Firefly/Modsicons/tab.png");
    private final ResourceLocation ICON_REACH = new ResourceLocation("Firefly/Modsicons/reach.png");
    private final ResourceLocation ICON_COMBO = new ResourceLocation("Firefly/Modsicons/Combodisplay.png");
    private final ResourceLocation ICON_TOGGLESPRINT = new ResourceLocation("Firefly/Modsicons/togglessprint.png");
    private final ResourceLocation ICON_TOGGLESNEAK = new ResourceLocation("Firefly/Modsicons/sneak.png");
    private final ResourceLocation ICON_HITDELAY = new ResourceLocation("Firefly/Modsicons/hitdelayfix.png");
    private final ResourceLocation ICON_TNT = new ResourceLocation("Firefly/Modsicons/tnt.png");
    private final ResourceLocation ICON_FREELOOK = new ResourceLocation("Firefly/Modsicons/freelook.png");
    private final ResourceLocation ICON_TIME = new ResourceLocation("Firefly/Modsicons/time.png");
    private final ResourceLocation ICON_WEATHER = new ResourceLocation("Firefly/Modsicons/weather changer.png");
    private final ResourceLocation ICON_CHAT = new ResourceLocation("Firefly/Modsicons/chat.png");
    private final ResourceLocation ICON_AUTOTEXT = new ResourceLocation("Firefly/Modsicons/autotext.png");
    private final ResourceLocation ICON_HYPIXEL = new ResourceLocation("Firefly/Modsicons/hypixel.png");
    private final ResourceLocation ICON_DISCORD = new ResourceLocation("Firefly/Modsicons/discord.png");
    private final ResourceLocation ICON_UHC = new ResourceLocation("Firefly/Modsicons/uhc.png");

    private static class CardBounds {
        float x, y, w, h; Mod mod;
        float gearX, gearY, gearW, gearH;
        boolean hasSettings;
        CardBounds(Mod mod, float x, float y, float w, float h) { this.mod = mod; this.x = x; this.y = y; this.w = w; this.h = h; }
    }
    private final ArrayList<CardBounds> cardBounds = new ArrayList<>();

    public ModFamily(GuiModMenu parent) {
        super(parent, TranslateText.MODULE, LegacyIcon.ARCHIVE, true, false);
        this.modMenu = parent;
    }

    @Override public void initGui() { openSetting = false; settingAnimation = new SmoothStepAnimation(250, 1.0); settingAnimation.setValue(1.0); }
    @Override public void initCategory() { scroll.resetAll(); openSetting = false; settingAnimation = new SmoothStepAnimation(250, 1.0); settingAnimation.setValue(1.0); }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Firefly instance = Firefly.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        
        int x = modMenu.getX();
        int y = modMenu.getY();
        int width = modMenu.getWidth();
        int height = modMenu.getHeight();
        int sidebarW = modMenu.getSidebarWidth();

        float contentX = x + sidebarW;
        float contentW = width - sidebarW;
        if (openSetting) { contentX = x; contentW = width; }

        settingAnimation.setDirection(openSetting ? Direction.BACKWARDS : Direction.FORWARDS);
        if (settingAnimation.isDone(Direction.FORWARDS)) { this.setCanClose(true); currentMod = null; }

        nvg.save();
        nvg.translate((float) -(width - (settingAnimation.getValue() * width)), 0);
        if (settingAnimation.getValue() > 0.01) {
            drawFilters(nvg, contentX + 20, y + 20, mouseX, mouseY);
            
            float gridStartY = y + 50;
            float scrollAreaH = height - 65;

            if (!openSetting) {
                if (MouseUtils.isInside(mouseX, mouseY, contentX, gridStartY, contentW, scrollAreaH)) scroll.onScroll();
                scroll.onAnimation();
            }

            nvg.save();
            nvg.scissor(contentX, gridStartY, contentW, scrollAreaH);
            nvg.translate(0, scroll.getValue());
            cardBounds.clear();
            
            int cols = 4;
            float totalGridW = (cols * CARD_SIZE) + ((cols - 1) * GAP);
            float startGridX = contentX + (contentW - totalGridW) / 2;
            
            int c = 0, r = 0;
            float my = 0;
            for (Mod modIter : instance.getModManager().getMods()) {
                if (filterMod(modIter)) continue;
                
                float cx = startGridX + c * (CARD_SIZE + GAP);
                float cy = gridStartY + r * (CARD_SIZE + GAP);
                my = cy + CARD_SIZE;
                
                if (cy + CARD_SIZE + scroll.getValue() > y && cy + scroll.getValue() < y + height) {
                    drawModCard(nvg, modIter, cx, cy, CARD_SIZE, mouseX, mouseY);
                }
                c++; if (c >= cols) { c = 0; r++; }
            }
            if (!openSetting) scroll.setMaxScroll(Math.max(0, (my - gridStartY) - scrollAreaH + 15));
            nvg.restore();
        }
        nvg.restore();

        nvg.save();
        nvg.translate((float) (settingAnimation.getValue() * width), 0);
        if (currentMod != null) drawSettingsScreen(nvg, x, y, width, height, mouseX, mouseY, partialTicks);
        nvg.restore();
    }

    private void drawModCard(NanoVGManager nvg, Mod m, float cx, float cy, float size, int mx, int my) {
        boolean h = MouseUtils.isInside(mx, my, cx, cy + scroll.getValue(), size, size);
        CardBounds b = new CardBounds(m, cx, cy, size, size);
        
        // FUNDO QUADRADO E COMPACTO
        nvg.drawRoundedRect(cx, cy, size, size, 4, h ? new Color(255, 255, 255, 15) : new Color(0, 0, 0, 40));
        
        // Ícone e Nome
        ResourceLocation icon = getModIcon(m.getName());
        nvg.drawImage(icon, cx + (size - 22)/2, cy + 8, 22, 22);
        nvg.drawCenteredText(m.getName(), cx + size/2, cy + 34, Color.WHITE, 7.5f, Fonts.SEMIBOLD);
        
        // SWITCH ORIGINAL (Design anterior restaurado)
        float tw = 28, th = 12, ty = cy + size - 18;
        float tx = cx + (size - tw) / 2;
        
        m.getAnimation().setAnimation(m.isToggled() ? 1.0F : 0.0F, 16);
        float a = m.getAnimation().getValue();
        
        nvg.drawRoundedRect(tx, ty, tw, th, th/2, new Color(30, 30, 30, 200));
        nvg.drawRoundedRect(tx + 2 + (tw - (th-2) - 4) * a, ty + 2, th - 4, th - 4, (th-4)/2, m.isToggled() ? ACCENT : Color.WHITE);
        
        // ENGRENAGEM (Mesma posição de antes)
        List<Setting> sList = Firefly.getInstance().getModManager().getSettingsByMod(m);
        b.hasSettings = (sList != null && !sList.isEmpty());
        if (b.hasSettings) {
            float gx = cx + size - 11, gy = cy + 4;
            b.gearX = gx - 2; b.gearY = gy - 2; b.gearW = 10; b.gearH = 10;
            boolean hG = MouseUtils.isInside(mx, my, b.gearX, b.gearY + scroll.getValue(), 10, 10);
            nvg.drawText(LegacyIcon.SETTINGS, gx, gy, hG ? Color.WHITE : new Color(200, 200, 200, 80), 6.5f, Fonts.LEGACYICON);
        }
        
        cardBounds.add(b);
    }

    private void drawSettingsScreen(NanoVGManager nvg, int x, int y, int w, int h, int mx, int my, float pt) {
        nvg.drawRoundedRect(x, y, w, h, 4, BACKGROUND_COLOR);
        nvg.drawOutlineRoundedRect(x, y, w, h, 4, 1f, new Color(255, 255, 255, 30));
        
        float btnW = 80, btnH = 22, btnX = x + w - btnW - 30, btnY = y + 30;
        nvg.drawRoundedRect(btnX, btnY, btnW, btnH, 5, currentMod.isToggled() ? ACCENT : new Color(180, 50, 50));
        nvg.drawCenteredText(currentMod.isToggled() ? "Enabled" : "Disabled", btnX + btnW/2, btnY + 6, Color.WHITE, 9, Fonts.SEMIBOLD);

        nvg.drawText(currentMod.getName(), x + 30, y + 30, Color.WHITE, 20, Fonts.SEMIBOLD);
        nvg.drawText("Configure o módulo ao seu gosto.", x + 30, y + 54, SECTION_TEXT, 8, Fonts.REGULAR);

        float listY = y + 85, viewH = h - 100;
        if (MouseUtils.isInside(mx, my, x, listY, w, viewH)) settingScroll.onScroll();
        settingScroll.onAnimation();

        List<ModuleSetting> normalSettings = comps.stream().filter(s -> !(s.setting instanceof ColorSetting)).collect(Collectors.toList());
        List<ModuleSetting> colorSettings = comps.stream().filter(s -> s.setting instanceof ColorSetting).collect(Collectors.toList());

        nvg.save();
        nvg.scissor(x, listY, w, viewH);
        nvg.translate(0, settingScroll.getValue());

        float margin = 40, gap = 30, colW = (w - (margin * 2) - gap) / 2;
        for (int i = 0; i < normalSettings.size(); i++) {
            ModuleSetting s = normalSettings.get(i);
            int column = i % 2;
            float ix = x + margin + (column * (colW + gap));
            float iy = listY + ((i / 2) * 40);
            nvg.drawText(s.setting.getName(), ix, iy + 10, Color.WHITE, 9, Fonts.MEDIUM);
            float compW = (s.setting instanceof NumberSetting) ? 70 : 50;
            s.comp.setX(ix + colW - compW); s.comp.setY(iy); s.comp.setWidth(compW);
            s.comp.draw(mx, (int)(my - settingScroll.getValue()), pt);
        }

        float lastNormalY = listY + (((normalSettings.size() + 1) / 2) * 40);
        if (!colorSettings.isEmpty()) {
            float colorSectionY = lastNormalY + 20;
            nvg.drawRect(x + margin, colorSectionY - 5, w - (margin * 2), 0.5f, new Color(255, 255, 255, 20));
            nvg.drawText("Colors", x + margin, colorSectionY + 8, SECTION_TEXT, 8.5f, Fonts.SEMIBOLD);
            for (int i = 0; i < colorSettings.size(); i++) {
                ModuleSetting s = colorSettings.get(i);
                float iy = colorSectionY + 25 + (i * 35);
                nvg.drawText(s.setting.getName(), x + margin, iy + 10, Color.WHITE, 9, Fonts.MEDIUM);
                s.comp.setX(x + margin + colW - 35); s.comp.setY(iy); s.comp.setWidth(25);
                s.comp.draw(mx, (int)(my - settingScroll.getValue()), pt);
            }
            lastNormalY = colorSectionY + 25 + (colorSettings.size() * 35);
        }
        settingScroll.setMaxScroll(Math.max(0, (lastNormalY - listY) - viewH + 60));
        nvg.restore();
    }

    private boolean filterMod(Mod m) {
        if (m.isHide()) return true;
        String s = modMenu.getSearchBox().getText().toLowerCase();
        if (!s.isEmpty() && !m.getName().toLowerCase().contains(s)) return true;
        if (currentFilter.equals("PvP") && m.getCategory() != ModCategory.PLAYER) return true;
        if (currentFilter.equals("Render") && m.getCategory() != ModCategory.RENDER) return true;
        if (currentFilter.equals("Utility") && (m.getCategory() == ModCategory.RENDER || m.getCategory() == ModCategory.PLAYER)) return true;
        return false;
    }

    private void drawFilters(NanoVGManager nvg, float x, float y, int mx, int my) {
        float cx = x;
        for (String f : filters) {
            boolean sel = currentFilter.equals(f);
            float tw = nvg.getTextWidth(f, 8.5f, Fonts.SEMIBOLD) + 12;
            if (sel) { nvg.drawRoundedRect(cx, y, tw, 16, 4, ACCENT); nvg.drawText(f, cx + 6, y + 4.5f, Color.BLACK, 8.5f, Fonts.SEMIBOLD); }
            else { if (MouseUtils.isInside(mx, my, cx, y, tw, 16)) nvg.drawRoundedRect(cx, y, tw, 16, 4, new Color(255,255,255, 10)); nvg.drawText(f, cx + 6, y + 4.5f, Color.WHITE, 8.5f, Fonts.SEMIBOLD); }
            if (MouseUtils.isInside(mx, my, cx, y, tw, 16) && org.lwjgl.input.Mouse.isButtonDown(0)) { currentFilter = f; scroll.reset(); }
            cx += tw + 6;
        }
    }

    private void openModuleSettings(Mod mod) {
        this.setCanClose(false); this.openSetting = true; this.currentMod = mod; this.settingScroll.reset(); this.comps.clear();
        List<Setting> settings = Firefly.getInstance().getModManager().getSettingsByMod(mod);
        if(settings != null) {
            for(Setting s : settings) {
                Comp c = null;
                if (s instanceof BooleanSetting) c = new CompToggleButton((BooleanSetting) s);
                else if (s instanceof NumberSetting) c = new CompSlider((NumberSetting) s);
                else if (s instanceof ComboSetting) c = new CompComboBox(0, (ComboSetting) s);
                else if (s instanceof ColorSetting) c = new CompColorPicker((ColorSetting) s);
                else if (s instanceof KeybindSetting) c = new CompKeybind(0, (KeybindSetting) s);
                else if (s instanceof TextSetting) c = new CompModTextBox((TextSetting) s);
                if(c != null) comps.add(new ModuleSetting(s, c));
            }
        }
    }

    private ResourceLocation getModIcon(String name) {
        String n = name.toLowerCase();
        if (n.contains("auto") && n.contains("text")) return ICON_AUTOTEXT;
        if (n.contains("damage") && n.contains("tint")) return ICON_DAMAGETINT;
        if (n.contains("damage") && n.contains("particle")) return ICON_DAMAGEPARTICLES;
        if (n.contains("particle")) return ICON_PARTICLES;
        if (n.contains("glint")) return ICON_GLINT;
        if (n.contains("china")) return ICON_CHINA;
        if (n.contains("trail")) return ICON_TRAIL;
        if (n.contains("zoom")) return ICON_ZOOM;
        if (n.contains("block") && n.contains("overlay")) return ICON_BLOCKOVERLAY;
        if (n.contains("item") && n.contains("physics")) return ICON_ITEMPHYSICS;
        if (n.contains("chunk")) return ICON_CHUNK;
        if (n.contains("fps")) return ICON_FPS;
        if (n.contains("cps")) return ICON_CPS;
        if (n.contains("keystroke")) return ICON_KEYSTROKES;
        if (n.contains("scoreboard")) return ICON_SCOREBOARD;
        if (n.contains("armor")) return ICON_ARMORSTATUS;
        if (n.contains("potion")) return ICON_POTIONS;
        if (n.contains("boss")) return ICON_BOSSHEALTH;
        if (n.contains("clock")) return ICON_CLOCK;
        if (n.contains("compass")) return ICON_COMPASS;
        if (n.contains("coord") || n.contains("pos")) return ICON_COORDS;
        if (n.contains("speed")) return ICON_SPEED;
        if (n.contains("ping")) return ICON_PING;
        if (n.contains("target")) return ICON_TARGETHUD;
        if (n.contains("inventory") || n.contains("inv")) return ICON_INVENTORY;
        if (n.contains("crosshair")) return ICON_CROSSHAIR;
        if (n.contains("nametag")) return ICON_NAMETAG;
        if (n.contains("tab")) return ICON_TAB;
        if (n.contains("reach")) return ICON_REACH;
        if (n.contains("combo")) return ICON_COMBO;
        if (n.contains("sprint")) return ICON_TOGGLESPRINT;
        if (n.contains("sneak")) return ICON_TOGGLESNEAK;
        if (n.contains("delay") || n.contains("hit")) return ICON_HITDELAY;
        if (n.contains("tnt")) return ICON_TNT;
        if (n.contains("freelook")) return ICON_FREELOOK;
        if (n.contains("time")) return ICON_TIME;
        if (n.contains("weather")) return ICON_WEATHER;
        if (n.contains("chat")) return ICON_CHAT;
        if (n.contains("hypixel")) return ICON_HYPIXEL;
        if (n.contains("discord")) return ICON_DISCORD;
        if (n.contains("uhc")) return ICON_UHC;
        if (n.contains("animation")) return ICON_ANIMATIONS;
        if (n.contains("3d") || n.contains("skin")) return ICON_3DSKIN;
        if (n.contains("cape")) return ICON_CAPES;
        if (n.contains("bright")) return ICON_FULLBRIGHT;
        if (n.contains("motion")) return ICON_MOTIONBLUR;
        if (n.contains("blood")) return ICON_BLOOD;
        return ICON_DEFAULT;
    }

    @Override
    public void mouseClicked(int mx, int my, int mb) {
        if (openSetting) {
            if (mb == 0 && mx < modMenu.getX() + 40) { openSetting = false; return; }
            float btnW = 80, btnH = 22, btnX = modMenu.getX() + modMenu.getWidth() - btnW - 30, btnY = modMenu.getY() + 30;
            if(mb == 0 && MouseUtils.isInside(mx, my, btnX, btnY, btnW, btnH)) { currentMod.toggle(); return; }
            for (ModuleSetting s : comps) s.comp.mouseClicked(mx, (int)(my - settingScroll.getValue()), mb);
            return;
        }
        for (CardBounds cb : cardBounds) {
            float ry = cb.y + scroll.getValue();
            if (ry < modMenu.getY() || ry > modMenu.getY() + modMenu.getHeight()) continue;
            
            // Clique na engrenagem abre configurações
            if (cb.hasSettings && MouseUtils.isInside(mx, my, cb.gearX, ry + (cb.gearY - cb.y), cb.gearW, cb.gearH)) {
                if (mb == 0) { openModuleSettings(cb.mod); return; }
            }
            
            // Clique no card faz toggle
            if (MouseUtils.isInside(mx, my, cb.x, ry, cb.w, cb.h)) {
                if (mb == 0) { cb.mod.toggle(); return; }
            }
        }
    }

    @Override public void mouseReleased(int mx, int my, int mb) { if(openSetting) for(ModuleSetting s : comps) s.comp.mouseReleased(mx, (int)(my - settingScroll.getValue()), mb); }
    @Override public void keyTyped(char c, int k) { if(openSetting) { for(ModuleSetting s : comps) if(s.comp instanceof CompModTextBox && ((CompModTextBox)s.comp).isFocused()) { s.comp.keyTyped(c, k); return; } if(k == Keyboard.KEY_ESCAPE) { openSetting = false; return; } for(ModuleSetting s : comps) s.comp.keyTyped(c, k); } else scroll.onKey(k); }
    public boolean isBinding() { if(!openSetting) return false; for(ModuleSetting s : comps) if(s.comp instanceof CompKeybind && ((CompKeybind)s.comp).isBinding()) return true; return false; }
    public boolean isSettingsOpen() { return openSetting; }
}