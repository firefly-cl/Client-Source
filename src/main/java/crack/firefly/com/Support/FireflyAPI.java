package crack.firefly.com.Support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;

public class FireflyAPI {

    // Lista dinâmica: guarda quem o client "descobriu" no servidor
    private static final List<UUID> detectedUsers = new ArrayList<>();

    // Método para adicionar alguém na lista (usaremos isso nos próximos passos)
    public static void addUser(UUID uuid) {
        if (!detectedUsers.contains(uuid)) {
            detectedUsers.add(uuid);
            // System.out.println("[Firefly] Usuario detectado: " + uuid.toString());
        }
    }

    // Verifica se o nome no Tab pertence a alguém da lista ou a você mesmo
    public static boolean isClientUser(String displayName) {
        Minecraft mc = Minecraft.getMinecraft();
        String cleanName = StringUtils.stripControlCodes(displayName);

        // 1. Verifica você mesmo (sempre true)
        if (mc.thePlayer != null && cleanName.equals(mc.thePlayer.getName())) {
            return true;
        }

        // 2. Verifica se o jogador está na lista de detectados
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile() != null) {
                if (cleanName.contains(info.getGameProfile().getName())) {
                    return detectedUsers.contains(info.getGameProfile().getId());
                }
            }
        }
        return false;
    }
}