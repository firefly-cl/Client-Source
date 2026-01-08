package crack.firefly.com.System.web;

import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.Minecraft;

public class UserTracker {

    // Link da API que cria o usuário no arquivo de texto
    // O "?nick=" no final é essencial para o PHP entender
    private static final String API_URL = "https://fireflyclient.kesug.com/api.php?nick=";

    public static void track() {
        // Rodamos em uma Thread separada para não travar a inicialização do jogo
        new Thread(() -> {
            try {
                // Pega o nick do usuário (Original ou Pirata)
                String username = Minecraft.getMinecraft().getSession().getUsername();
                
                // Previne erros se o nick tiver espaço (comum em nomes piratas mal configurados)
                String cleanUrl = API_URL + username.replace(" ", "%20");
                
                URL url = new URL(cleanUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                // CONFIGURAÇÃO IMPORTANTE PARA HOSPEDAGEM GRÁTIS
                // Fingimos que somos um navegador Google Chrome para o site não bloquear o Java
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                conn.setConnectTimeout(5000); // Espera no máximo 5 segundos
                
                // Faz a conexão
                conn.connect();
                
                // Pega a resposta só para garantir que enviou
                int responseCode = conn.getResponseCode();
                
                if (responseCode == 200) {
                    System.out.println("[Firefly Tracker] Usuário registrado no site com sucesso: " + username);
                } else {
                    System.err.println("[Firefly Tracker] Site recebeu, mas retornou código: " + responseCode);
                }
                
                conn.disconnect();
                
            } catch (Exception e) {
                System.err.println("[Firefly Tracker] Erro ao conectar no site (Site offline ou sem internet).");
                e.printStackTrace();
            }
        }, "Firefly User Tracker Thread").start();
    }
}