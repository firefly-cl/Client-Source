package crack.firefly.com.Support;

import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class OptifineUtils {

    // Método para DESATIVAR (usado no Firefly.java e nos initGui)
    public static void disableFastRender() {
        setFastRender(false);
        System.out.println("[Firefly] Fast Render: OFF");
    }

    // Método para REATIVAR (usado no onGuiClosed)
    public static void enableFastRender() {
        setFastRender(true);
        System.out.println("[Firefly] Fast Render: ON");
    }

    // Checa se está ativado no Optifine
    public static boolean isFastRenderEnabled() {
        try {
            Field field = Minecraft.getMinecraft().gameSettings.getClass().getDeclaredField("ofFastRender");
            return field.getBoolean(Minecraft.getMinecraft().gameSettings);
        } catch (Exception e) {
            return false;
        }
    }

    // Altera o valor via Reflexão (Reflection)
    public static void setFastRender(boolean state) {
        try {
            Field field = Minecraft.getMinecraft().gameSettings.getClass().getDeclaredField("ofFastRender");
            field.setBoolean(Minecraft.getMinecraft().gameSettings, state);
            
            // Salva a configuração para o Minecraft aplicar a mudança de renderização
            Minecraft.getMinecraft().gameSettings.saveOptions();
        } catch (Exception e) {
            // Se não houver Optifine, não faz nada
        }
    }
}