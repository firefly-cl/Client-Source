package crack.firefly.com.discord;

import java.time.OffsetDateTime;

// Imports da sua biblioteca Discord IPC
import crack.firefly.com.discord.ipc.IPCClient;
import crack.firefly.com.discord.ipc.IPCListener;
import crack.firefly.com.discord.ipc.entities.RichPresence;
import crack.firefly.com.discord.ipc.entities.pipe.PipeStatus;
import crack.firefly.com.discord.ipc.exceptions.NoDiscordClientException;

public class DiscordRPC {

    private IPCClient client;
    
    /**
     * Inicia a conexão IPC com o Discord e define o status inicial.
     */
    public void start() {
        
        final long CLIENT_ID = 801762347449450506L;
        
        client = new IPCClient(CLIENT_ID);
        
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                
                RichPresence.Builder builder = new RichPresence.Builder();
                
                builder.setState("Playing Minecraft 1.8.9")
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("icon", "Minecraft Client");
                
                client.sendRichPresence(builder.build());
                System.out.println("Discord RPC iniciado e status definido.");
            }
            
            @Override
            public void onDisconnect(IPCClient client, Throwable t) {
                System.err.println("Conexão Discord IPC perdida: " + t.getMessage());
            }
        });
        
        try {
            client.connect();
        } catch (NoDiscordClientException e) {
            System.err.println("Cliente Discord não está sendo executado. RPC não será ativado.");
            // e.printStackTrace();
        }
    }
    
    /**
     * Fecha a conexão IPC de forma limpa. Deve ser chamado ao fechar a aplicação.
     */
    public void stop() {
        if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
            // Chamada direta, pois o compilador indicou que não há IOException para capturar.
            client.close(); 
            System.out.println("Discord RPC desconectado.");
        }
        client = null;
    }


    // --- Métodos de Utilidade ---

    public IPCClient getClient() {
        return client;
    }
    
    public boolean isStarted() {
        return client != null && client.getStatus() == PipeStatus.CONNECTED;
    }
    
}