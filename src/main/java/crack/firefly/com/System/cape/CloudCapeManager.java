package crack.firefly.com.System.cape;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CloudCapeManager {

    // Guarda na memória: "DarkRz" -> "Cat Couple"
    private static final Map<String, String> userCapes = new HashMap<>();
    
    // Link direto para o arquivo de texto gerado pelo PHP
    private static final String CAPES_LIST_URL = "https://fireflyclient.kesug.com/capes.txt";

    public static void reload() {
        new Thread(() -> {
            try {
                System.out.println("[Firefly Cloud] Baixando lista de capas...");
                
                URL url = new URL(CAPES_LIST_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                // Fingimos ser navegador de novo para passar pelo Anti-Bot
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                conn.setConnectTimeout(10000);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                
                String line;
                while ((line = reader.readLine()) != null) {
                    // O formato no arquivo é: Nick:NomeDaCapa
                    if (line.contains(":")) {
                        String[] parts = line.split(":");
                        if (parts.length >= 2) {
                            String username = parts[0].trim();
                            String capeName = parts[1].trim();
                            
                            // Guarda no mapa (convertendo nick para minusculo para evitar erros de case sensitive)
                            userCapes.put(username.toLowerCase(), capeName);
                        }
                    }
                }
                reader.close();
                System.out.println("[Firefly Cloud] Total de capas carregadas: " + userCapes.size());
                
            } catch (Exception e) {
                System.err.println("[Firefly Cloud] Falha ao baixar lista de capas. O site pode estar offline.");
            }
        }, "Firefly Cloud Cape Downloader").start();
    }

    /**
     * Retorna o nome da capa que o usuário deve usar.
     * Retorna NULL se o usuário não tiver capa registrada.
     */
    public static String getCapeName(String username) {
        if (username == null) return null;
        return userCapes.get(username.toLowerCase());
    }
}