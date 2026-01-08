package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import net.java.games.input.*;
import org.lwjgl.input.Mouse;

import java.util.concurrent.locks.LockSupport;

public class RawInputMod extends Mod {

    private static RawInputMod instance;
    private Controller mouseDevice;
    private Thread thread;
    private volatile boolean running;

    // Deltas acumulados (Raw counts da GPU/Mouse)
    private double deltaX, deltaY;
    private final Object syncLock = new Object();

    public RawInputMod() {
        super(TranslateText.RAW_INPUT, TranslateText.RAW_INPUT_DESCRIPTION, ModCategory.OTHER);
        instance = this;
    }

    @Override
    public void onEnable() {
        if (setupDevice()) {
            startPollingThread();
        }
    }

    @Override
    public void onDisable() {
        running = false;
        resetDeltas();
    }

    private boolean setupDevice() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (Controller c : controllers) {
            if (c.getType() == Controller.Type.MOUSE) {
                this.mouseDevice = c;
                return true;
            }
        }
        return false;
    }

    private void startPollingThread() {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                // Polling rate de 1000Hz (1ms) - Estilo Lunar
                if (Mouse.isGrabbed() && mouseDevice != null) {
                    mouseDevice.poll();
                    EventQueue queue = mouseDevice.getEventQueue();
                    Event event = new Event();

                    synchronized (syncLock) {
                        while (queue.getNextEvent(event)) {
                            Component component = event.getComponent();
                            float value = event.getValue();

                            if (component.getIdentifier() == Component.Identifier.Axis.X) {
                                deltaX += value;
                            } else if (component.getIdentifier() == Component.Identifier.Axis.Y) {
                                // Invertemos o Y para bater com o padrão do Minecraft
                                deltaY -= value;
                            }
                        }
                    }
                } else {
                    resetDeltas();
                }
                LockSupport.parkNanos(1_000_000L); // 1ms
            }
        }, "Firefly-RawInput-Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Retorna o movimento acumulado e limpa para o próximo frame.
     * Chamado via Mixin no MouseHelper.
     */
    public double[] getAndResetDeltas() {
        synchronized (syncLock) {
            double[] deltas = {deltaX, deltaY};
            deltaX = 0;
            deltaY = 0;
            return deltas;
        }
    }

    private void resetDeltas() {
        synchronized (syncLock) {
            deltaX = 0;
            deltaY = 0;
        }
    }

    public static RawInputMod getInstance() {
        return instance;
    }
}