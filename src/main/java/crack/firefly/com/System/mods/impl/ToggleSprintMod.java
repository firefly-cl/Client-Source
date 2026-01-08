package crack.firefly.com.System.mods.impl;

import java.awt.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ColorSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import net.minecraft.client.settings.KeyBinding;

public class ToggleSprintMod extends HUDMod {

    // CORREÇÃO: IDs diferentes
    private BooleanSetting background = new BooleanSetting(TranslateText.BACKGROUND, this, true);
    private ColorSetting bgColor = new ColorSetting(TranslateText.BACK_COLOR, this, new Color(0, 0, 0, 120));
    private ColorSetting textColor = new ColorSetting(TranslateText.TEXT_COLOR, this, Color.WHITE);

    private BooleanSetting hudSetting = new BooleanSetting(TranslateText.HUD, this, true);
    private BooleanSetting alwaysSetting = new BooleanSetting(TranslateText.ALWAYS, this, false);

    private long startTime;
    private boolean wasDown;
    private State state;
    
    public ToggleSprintMod() {
        super(TranslateText.TOGGLE_SPRINT, TranslateText.TOGGLE_SPRINT_DESCRIPTION);
    }
    
    @Override
    public void setup() {
        state = State.WALK;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        
        if(!hudSetting.isToggled()) {
            this.setWidth(0); this.setHeight(0); return;
        }

        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        
        String text = getText();
        float scale = this.getScale();
        
        int baseStringWidth = mc.fontRendererObj.getStringWidth(text);
        int baseFontHeight = mc.fontRendererObj.FONT_HEIGHT;
        float paddingX = 6f;
        float paddingY = 4f;
        
        float w = (baseStringWidth + (paddingX * 2)) * scale;
        float h = (baseFontHeight + (paddingY * 2)) * scale;
        
        this.setWidth((int) w);
        this.setHeight((int) h);
        
        float x = this.getX();
        float y = this.getY();
        
        nvg.setupAndDraw(() -> {
            if (background.isToggled()) {
                nvg.drawRoundedRect(x, y, w, h, 4, bgColor.getColor());
            }
        });
        
        GL11.glPushMatrix();
        float centerX = x + (w / 2f);
        float centerY = y + (h / 2f);
        GL11.glTranslatef(centerX, centerY, 0);
        GL11.glScalef(scale, scale, 1f);
        mc.fontRendererObj.drawStringWithShadow(text, -baseStringWidth / 2f, -(baseFontHeight / 2f) + 1f, textColor.getColor().getRGB());
        GL11.glPopMatrix();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), state.equals(State.HELD) || state.equals(State.TOGGLED) || alwaysSetting.isToggled());
    }
    
    @EventTarget
    public void onTick(EventTick event) {
        boolean down = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
        if(alwaysSetting.isToggled() || mc.currentScreen != null) return;
        
        if(down) {
            if(!wasDown) {
                startTime = System.currentTimeMillis();
                if(state.equals(State.TOGGLED)) state = State.HELD;
                else state = State.TOGGLED;
            } else if((System.currentTimeMillis() - startTime) > 250) {
                state = State.HELD;
            }
        } else if(state.equals(State.HELD) && mc.thePlayer.isSprinting()) {
            state = State.VANILLA;
        } else if((state.equals(State.VANILLA) || state.equals(State.HELD)) && !mc.thePlayer.isSprinting()) {
            state = State.WALK;
        }
        wasDown = down;
    }
    
    public String getText() {
        String prefix = "[Sprinting]";
        if(alwaysSetting.isToggled()) return prefix + " (Always)";
        if(state.equals(State.WALK)) return "[Sprinting] (Toggled)";
        return prefix + " (" + state.name + ")";
    }
    
    private enum State {
        WALK("Walking"), VANILLA("Vanilla"), HELD("Key Held"), TOGGLED("Toggled");
        private String name;
        private State(String name) { this.name = name; }
    }
}