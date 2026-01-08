package crack.firefly.com.System.mods;

import java.awt.Color;
import java.io.File;
import crack.firefly.com.Firefly;
import eu.shoroa.contrib.render.ShBlur;
import crack.firefly.com.menus.GuiEditHUD;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.impl.InternalSettingsMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Font;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.util.ResourceLocation;

public class HUDMod extends Mod {

    private int x = 100, y = 100, draggingX, draggingY, width = 100, height = 20;
    private float scale = 1.0F;
    private boolean dragging, draggable = true;

    // --- CONSTRUTORES RESTAURADOS (PARA EVITAR ERROS NAS SUBCLASSES) ---
    public HUDMod(TranslateText name, TranslateText desc) { 
        super(name, desc, ModCategory.HUD); 
    }
    public HUDMod(TranslateText name, TranslateText desc, String alias) { 
        super(name, desc, ModCategory.HUD, alias); 
    }
    public HUDMod(TranslateText name, TranslateText desc, String alias, boolean restricted) { 
        super(name, desc, ModCategory.HUD, alias, restricted); 
    }

    public void save() { Firefly.getInstance().getNanoVGManager().save(); }
    public void restore() { Firefly.getInstance().getNanoVGManager().restore(); }

    // --- MÉTODOS DE RENDERIZAÇÃO ---
    public void drawRect(float ax, float ay, float w, float h, Color c) {
        Firefly.getInstance().getNanoVGManager().drawRect(x + (ax * scale), y + (ay * scale), w * scale, h * scale, c);
    }
    public void drawRect(float ax, float ay, float w, float h) { drawRect(ax, ay, w, h, getFontColor()); }

    public void drawRoundedRect(float ax, float ay, float w, float h, float r, Color c) {
        Firefly.getInstance().getNanoVGManager().drawRoundedRect(x + (ax * scale), y + (ay * scale), w * scale, h * scale, r * scale, c);
    }
    public void drawRoundedRect(float ax, float ay, float w, float h, float r) { drawRoundedRect(ax, ay, w, h, r, getFontColor()); }

    public void drawShadow(float ax, float ay, float w, float h, float r) {
        Firefly.getInstance().getNanoVGManager().drawShadow(x + (ax * scale), y + (ay * scale), w * scale, h * scale, r * scale);
    }

    public void drawPlayerHead(ResourceLocation loc, float ax, float ay, float w, float h, float r) {
        Firefly.getInstance().getNanoVGManager().drawPlayerHead(loc, x + (ax * scale), y + (ay * scale), w * scale, h * scale, r * scale);
    }

    public void drawRoundedImage(File file, float ax, float ay, float w, float h, float r, float alpha) {
        Firefly.getInstance().getNanoVGManager().drawRoundedImage(file, x + (ax * scale), y + (ay * scale), w * scale, h * scale, r * scale, alpha);
    }

    public void drawArc(float ax, float ay, float r, float sa, float ea, float sw, Color c) {
        Firefly.getInstance().getNanoVGManager().drawArc(x + (ax * scale), y + (ay * scale), r * scale, sa, ea, sw * scale, c);
    }
    public void drawArc(float ax, float ay, float r, float sa, float ea, float sw) { drawArc(ax, ay, r, sa, ea, sw, getFontColor()); }

    // --- SOBRECARGAS DE BACKGROUND (ATUALIZAM O WIDTH/HEIGHT PARA O EDITOR) ---
    public void drawBackground(float ax, float ay, float w, float h, float r) {
        this.width = (int)w;
        this.height = (int)h;
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        boolean isBlur = InternalSettingsMod.getInstance().getBlurSetting().isToggled();
        float rx = x + (ax * scale), ry = y + (ay * scale), rw = w * scale, rh = h * scale;
        if (isBlur) ShBlur.getInstance().drawBlur(rx, ry, rw, rh, r * scale);
        nvg.drawRoundedRect(rx, ry, rw, rh, r * scale, new Color(0, 0, 0, 150));
    }
    public void drawBackground(float w, float h) { drawBackground(0, 0, w, h, 6); }
    public void drawBackground(float w, float h, float r) { drawBackground(0, 0, w, h, r); }

    // --- SOBRECARGAS DE TEXTO ---
    public void drawText(String t, float ax, float ay, float s, Font f, Color c) {
        Firefly.getInstance().getNanoVGManager().drawText(t, x + (ax * scale), y + (ay * scale), c, s * scale, f);
    }
    public void drawText(String t, float ax, float ay, float s, Font f) { drawText(t, ax, ay, s, f, getFontColor()); }

    public void drawCenteredText(String t, float ax, float ay, float s, Font f, Color c) {
        Firefly.getInstance().getNanoVGManager().drawCenteredText(t, x + (ax * scale), y + (ay * scale), c, s * scale, f);
    }
    public void drawCenteredText(String t, float ax, float ay, float s, Font f) { drawCenteredText(t, ax, ay, s, f, getFontColor()); }

    public float getTextWidth(String t, float s, Font f) {
        return Firefly.getInstance().getNanoVGManager().getTextWidth(t, s, f);
    }

    // --- GETTERS E SETTERS ---
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return (int) (width * scale); }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return (int) (height * scale); }
    public void setHeight(int height) { this.height = height; }
    
    public int getBaseWidth() { return Math.max(width, 10); }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = Math.max(0.2F, Math.min(scale, 5.0F)); }

    public boolean isDragging() { return dragging; }
    public void setDragging(boolean d) { this.dragging = d; }
    public int getDraggingX() { return draggingX; }
    public void setDraggingX(int dx) { this.draggingX = dx; }
    public int getDraggingY() { return draggingY; }
    public void setDraggingY(int dy) { this.draggingY = dy; }

    public boolean isDraggable() { return draggable; }
    public void setDraggable(boolean draggable) { this.draggable = draggable; }

    public Color getFontColor() { return Color.WHITE; }
    public Color getFontColor(int alpha) { return new Color(255, 255, 255, alpha); }
    public boolean isEditing() { return mc.currentScreen instanceof GuiEditHUD; }

    public Font getHudFont(int in){
        switch (in) {
            case 1: return Fonts.REGULAR;
            case 2: return Fonts.MEDIUM;
            case 3: return Fonts.SEMIBOLD;
            default: return Fonts.REGULAR;
        }
    }
}