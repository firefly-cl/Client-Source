package crack.firefly.com.System.mods.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KeystrokesMod extends HUDMod {

    private NumberSetting scaleSetting = new NumberSetting(TranslateText.SCALE, this, 1.0, 0.5, 2.0, false); 
    private NumberSetting fadeDelaySetting = new NumberSetting(TranslateText.FADE_DELAY, this, 15, 5, 50, true); 
    private BooleanSetting rounded = new BooleanSetting(TranslateText.ROUNDED, this, false);
    private BooleanSetting mouseButtonsSetting = new BooleanSetting(TranslateText.MOUSE_BUTTONS, this, true);
    private BooleanSetting spaceSetting = new BooleanSetting(TranslateText.SPACE, this, true); 
    private BooleanSetting cpsSetting = new BooleanSetting(TranslateText.CPS, this, true);

    private ColorSetting backColorSetting = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120), false);
    private ColorSetting pressedColorSetting = new ColorSetting(TranslateText.PRESSED_COLOR, this, new Color(255, 255, 255, 100), false); 
    private ColorSetting fontColorSetting = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE, false);

    private SimpleAnimation[] animations = new SimpleAnimation[7];
    private List<Long> leftClicks = new ArrayList<>();
    private List<Long> rightClicks = new ArrayList<>();
    private boolean wasLeftPressed, wasRightPressed;

    private final float BASE_SIZE = 26; 
    private final float BASE_GAP = 2;

    public KeystrokesMod() {
        super(TranslateText.KEYSTROKES, TranslateText.KEYSTROKES_DESCRIPTION);
        for (int i = 0; i < animations.length; i++) animations[i] = new SimpleAnimation();
    }
    
    @EventTarget
    public void onRender2D(EventRender2D event) {
        updateCPS();
        updateSize();
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        nvg.setupAndDraw(this::drawKeys);
    }

    private void updateSize() {
        double scale = scaleSetting.getValue();
        float size = (float) (BASE_SIZE * scale);
        float gap = (float) (BASE_GAP * scale);
        float width = (size * 3) + (gap * 2);
        
        float height = (size * 2) + gap; 
        if (mouseButtonsSetting.isToggled()) height += (size * 0.8f) + gap;
        if (spaceSetting.isToggled()) height += (size * 0.6f) + gap;

        this.setWidth((int) width);
        this.setHeight((int) height);
    }

    private void updateCPS() {
        boolean lp = Mouse.isButtonDown(0), rp = Mouse.isButtonDown(1);
        if (lp && !wasLeftPressed) leftClicks.add(System.currentTimeMillis());
        if (rp && !wasRightPressed) rightClicks.add(System.currentTimeMillis());
        wasLeftPressed = lp; wasRightPressed = rp;
        long time = System.currentTimeMillis();
        leftClicks.removeIf(t -> t + 1000 < time);
        rightClicks.removeIf(t -> t + 1000 < time);
    }

    private void drawKeys() {
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float scale = (float) scaleSetting.getValue();
        float size = BASE_SIZE * scale;
        float gap = BASE_GAP * scale;
        int speed = (int) fadeDelaySetting.getValue();

        int[] keys = {mc.gameSettings.keyBindForward.getKeyCode(), mc.gameSettings.keyBindLeft.getKeyCode(), 
                      mc.gameSettings.keyBindBack.getKeyCode(), mc.gameSettings.keyBindRight.getKeyCode(),
                      mc.gameSettings.keyBindJump.getKeyCode()};

        for (int i = 0; i < 5; i++) animations[i].setAnimation(Keyboard.isKeyDown(keys[i]) ? 1.0f : 0.0f, speed);
        animations[5].setAnimation(Mouse.isButtonDown(0) ? 1.0f : 0.0f, speed);
        animations[6].setAnimation(Mouse.isButtonDown(1) ? 1.0f : 0.0f, speed);

        drawKey(nvg, getX() + size + gap, getY(), size, size, getKeyName(keys[0]), animations[0]);
        drawKey(nvg, getX(), getY() + size + gap, size, size, getKeyName(keys[1]), animations[1]);
        drawKey(nvg, getX() + size + gap, getY() + size + gap, size, size, getKeyName(keys[2]), animations[2]);
        drawKey(nvg, getX() + (size + gap) * 2, getY() + size + gap, size, size, getKeyName(keys[3]), animations[3]);

        float currentY = getY() + (size + gap) * 2;

        if (mouseButtonsSetting.isToggled()) {
            float mW = (size * 3 + gap * 2 - gap) / 2f;
            float mH = size * 0.8f;
            drawKey(nvg, getX(), currentY, mW, mH, cpsSetting.isToggled() ? leftClicks.size() + " LMB" : "LMB", animations[5]);
            drawKey(nvg, getX() + mW + gap, currentY, mW, mH, cpsSetting.isToggled() ? rightClicks.size() + " RMB" : "RMB", animations[6]);
            currentY += mH + gap;
        }

        if (spaceSetting.isToggled()) {
            float sW = size * 3 + gap * 2;
            float sH = size * 0.6f;
            drawKey(nvg, getX(), currentY, sW, sH, "_____", animations[4]);
        }
    }

    private void drawKey(NanoVGManager nvg, float x, float y, float w, float h, String text, SimpleAnimation anim) {
        Color bg = interpolateColor(backColorSetting.getColor(), pressedColorSetting.getColor(), anim.getValue());
        nvg.drawRoundedRect(x, y, w, h, rounded.isToggled() ? 3 : 0, bg);
        
        GL11.glPushMatrix();
        float scale = (float) scaleSetting.getValue();
        float textScale = scale * 1.0f; 
        int textW = mc.fontRendererObj.getStringWidth(text);
        
        GL11.glTranslatef(x + w / 2f, y + h / 2f - (mc.fontRendererObj.FONT_HEIGHT * textScale / 2f) + (1 * scale), 0);
        GL11.glScalef(textScale, textScale, 1);
        mc.fontRendererObj.drawStringWithShadow(text, -textW / 2f, 0, fontColorSetting.getColor().getRGB());
        GL11.glPopMatrix();
    }

    private Color interpolateColor(Color c1, Color c2, float fraction) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * fraction);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * fraction);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * fraction);
        int a = (int) (c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * fraction);
        return new Color(r, g, b, a);
    }

    private String getKeyName(int k) {
        if (k < 0) return "";
        String name = Keyboard.getKeyName(k);
        if (name.equals("LSHIFT")) return "SHT";
        if (name.equals("LCONTROL")) return "CTRL";
        return name;
    }
}