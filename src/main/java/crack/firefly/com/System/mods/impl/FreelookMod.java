package crack.firefly.com.System.mods.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventCameraRotation;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.event.impl.EventPlayerHeadRotation;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class FreelookMod extends Mod {

    private static FreelookMod instance;

    public static FreelookMod getInstance() {
        if (instance == null) {
            instance = new FreelookMod();
        }
        return instance;
    }

    private final BooleanSetting invertYawSetting = new BooleanSetting(TranslateText.INVERT_YAW, this, false);
    private final BooleanSetting invertPitchSetting = new BooleanSetting(TranslateText.INVERT_PITCH, this, false);
    private final ComboSetting modeSetting = new ComboSetting(TranslateText.MODE, this, TranslateText.KEYDOWN, new ArrayList<Option>(Arrays.asList(
            new Option(TranslateText.TOGGLE), new Option(TranslateText.KEYDOWN))));
    private final KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_V);
    
    private boolean active;
    
    // Rotação Atual
    private float yaw;
    private float pitch;
    
    // Rotação Anterior (O SEGREDO DA SUAVIDADE)
    private float prevYaw;
    private float prevPitch;
    
    private int previousPerspective;
    private boolean toggled;

    public FreelookMod() {
        super(TranslateText.FREELOOK, TranslateText.FREELOOK_DESCRIPTION, ModCategory.PLAYER, "perspectivemod", true);
        if (instance == null) {
            instance = this;
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        // ATUALIZA A ROTAÇÃO ANTERIOR A CADA TICK
        if (active) {
            prevYaw = yaw;
            prevPitch = pitch;
        }

        Option mode = modeSetting.getOption();

        if (mode.getTranslate().equals(TranslateText.KEYDOWN)) {
            if (keybindSetting.isKeyDown()) {
                start();
            } else {
                stop();
            }
        }

        if (mode.getTranslate().equals(TranslateText.TOGGLE)) {
            if (toggled) {
                start();
            } else {
                stop();
            }
        }
    }

    @EventTarget
    public void onKey(EventKey event) {
        Option mode = modeSetting.getOption();
        if (mode.getTranslate().equals(TranslateText.TOGGLE)) {
            if (keybindSetting.isKeyDown()) {
                toggled = !toggled;
            }
        }
        if (event.getKeyCode() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
            toggled = false;
        }
    }

    @EventTarget
    public void onCameraRotation(EventCameraRotation event) {
        if (active) {
            event.setYaw(yaw);
            event.setPitch(pitch);
        }
    }

    @EventTarget
    public void onPlayerHeadRotation(EventPlayerHeadRotation event) {
        if (active) {
            float eYaw = event.getYaw();
            float ePitch = event.getPitch();
            event.setCancelled(true);
            ePitch = -ePitch;

            if (!invertPitchSetting.isToggled()) {
                ePitch = -ePitch;
            }

            if (invertYawSetting.isToggled()) {
                eYaw = -eYaw;
            }

            this.yaw += eYaw * 0.15F;
            this.pitch = MathHelper.clamp_float(this.pitch + (ePitch * 0.15F), -90, 90);
            
            // Força renderização para evitar delay visual
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    private void start() {
        if (!active) {
            active = true;
            previousPerspective = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 3; 
            
            Entity renderView = mc.getRenderViewEntity();
            yaw = renderView.rotationYaw;
            pitch = renderView.rotationPitch;
            
            // Sincroniza o anterior com o atual ao iniciar para não "pular"
            prevYaw = yaw;
            prevPitch = pitch;
        }
    }

    private void stop() {
        if (active) {
            active = false;
            mc.gameSettings.thirdPersonView = previousPerspective;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    public boolean isActive() {
        return active;
    }

    public float getCameraYaw() {
        return yaw;
    }

    public float getCameraPitch() {
        return pitch;
    }
    
    // GETTERS PARA A INTERPOLAÇÃO (USADOS NO MIXIN)
    public float getPrevCameraYaw() {
        return prevYaw;
    }

    public float getPrevCameraPitch() {
        return prevPitch;
    }
}