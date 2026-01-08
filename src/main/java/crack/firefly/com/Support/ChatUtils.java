package crack.firefly.com.Support;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {

    // Prefixo do Client (ex: [Firefly] Mensagem)
    public static final String PREFIX = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GREEN + "Firefly" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET;

    public static void print(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX + message));
        }
    }
    
    // Método sem prefixo (opcional)
    public static void printRaw(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}