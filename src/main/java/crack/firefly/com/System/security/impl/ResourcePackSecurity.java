package crack.firefly.com.System.security.impl;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventReceivePacket;
import crack.firefly.com.System.security.SecurityFeature;
import net.minecraft.network.play.server.S48PacketResourcePackSend;

public class ResourcePackSecurity extends SecurityFeature {

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        // Verifica se o pacote recebido é de envio de Resource Pack
        if (event.getPacket() instanceof S48PacketResourcePackSend) {
            S48PacketResourcePackSend packet = (S48PacketResourcePackSend) event.getPacket();
            
            String url = packet.getURL();

            // Se o check retornar verdadeiro, o pacote é malicioso e deve ser cancelado
            if (isMalicious(url)) {
                // System.out.println("[Firefly Security] Bloqueada URL de Resource Pack suspeita: " + url);
                event.setCancelled(true);
            }
        }
    }

    /**
     * @author Firefly Security
     * @reason Verifica se a URL do Resource Pack tenta acessar arquivos locais ou diretórios proibidos.
     */
    private boolean isMalicious(String url) {
        try {
            String lowerUrl = url.toLowerCase();

            // 1. Bloqueia protocolos perigosos que não sejam HTTP, HTTPS ou LEVEL
            if (!lowerUrl.startsWith("http://") && !lowerUrl.startsWith("https://") && !lowerUrl.startsWith("level://")) {
                return true;
            }

            // 2. Se usar o protocolo level:// (recurso local do mundo)
            if (lowerUrl.startsWith("level://")) {
                // Decodifica a URL para checar caracteres ocultos
                String decodedPath = URLDecoder.decode(url.substring(8), StandardCharsets.UTF_8.name());

                // Bloqueia tentativa de sair da pasta do mundo (Directory Traversal)
                // Checa por ".." e também por caminhos absolutos no Windows/Linux
                if (decodedPath.contains("..") || decodedPath.contains(":") || decodedPath.contains("\\")) {
                    return true;
                }

                // No 1.8.9, packs de nível legítimos devem terminar obrigatoriamente com /resources.zip
                if (!decodedPath.endsWith("/resources.zip")) {
                    return true;
                }
            }

            // 3. Validação final via URI para garantir que a estrutura é válida
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            
            return scheme == null || (!scheme.equals("http") && !scheme.equals("https") && !scheme.equals("level"));

        } catch (Exception e) {
            // Se houver qualquer erro ao processar a URL, por segurança, bloqueamos.
            return true;
        }
    }
}