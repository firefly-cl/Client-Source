package crack.firefly.com.System.web.websocket;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import crack.firefly.com.Console.FireflyConsole;

public class WebSocketManager {

    private WebSocketClient client;
    
    // URL real do seu deploy na Cloudflare (wss é obrigatório para HTTPS)
    private final String SERVER_URL = "wss://shindo-websocket.firefly-client.workers.dev/websocket";
    
    // A chave ADMIN_KEY que você configurou no painel da Cloudflare (no seu print estava '123')
    private final String ADMIN_KEY = "123";

    public void init() {
        try {
            FireflyConsole.info("[WebSocket] Conectando à Cloudflare Edge...");
            
            client = new WebSocketClient(new URI(SERVER_URL)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    FireflyConsole.info("[WebSocket] Conexão aberta com sucesso!");
                    
                    // PASSO OBRIGATÓRIO: Autenticar para evitar erro no servidor
                    // O Shindo Websocket sanitiza entradas via Zod e exige esse payload
                    sendAuth();
                }

                @Override
                public void onMessage(String message) {
                    FireflyConsole.info("[WebSocket] Mensagem recebida: " + message);
                    // Aqui você processa o estado de presença sincronizado com o Firestore
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    FireflyConsole.warn("[WebSocket] Conexão fechada: " + reason + " (Código: " + code + ")");
                }

                @Override
                public void onError(Exception ex) {
                    // Se aparecer "Internal Server Error" aqui, verifique a FIREBASE_PRIVATE_KEY no painel
                    FireflyConsole.error("[WebSocket] Erro na conexão", ex);
                }
            };

            // Define um timeout para não travar o jogo se a internet cair
            client.setConnectionLostTimeout(30);
            client.connect();

        } catch (Exception e) {
            FireflyConsole.error("[WebSocket] Falha ao iniciar conexão", e);
        }
    }

    private void sendAuth() {
        if (client != null && client.isOpen()) {
            // Payload de autenticação exigido pela camada de sinalização
            String authPayload = "{\"type\":\"auth\",\"key\":\"" + ADMIN_KEY + "\"}";
            client.send(authPayload);
            FireflyConsole.info("[WebSocket] Autenticação enviada.");
        }
    }

    public void send(String message) {
        if (client != null && client.isOpen()) {
            client.send(message);
        }
    }

    public void shutdown() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception ignored) {}
    }
}