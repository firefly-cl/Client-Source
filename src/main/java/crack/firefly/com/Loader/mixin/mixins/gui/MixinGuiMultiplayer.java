package crack.firefly.com.Loader.mixin.mixins.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Shadow private ServerList savedServerList;
    @Shadow private ServerSelectionList serverListSelector;
    @Shadow private GuiButton btnEdit;
    @Shadow private GuiButton btnDelete;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void addDefaultServers(CallbackInfo ci) {
        // Adiciona se não existir
        addServerIfNotExists("SparkMC", "sparkmc.com.br");
        addServerIfNotExists("KaizenMC", "kaizenmc.gg");
    }

    /**
     * Esta injeção verifica qual servidor está selecionado.
     * Se for um dos IPs protegidos, desabilita os botões de Editar e Deletar.
     */
    @Inject(method = "setButtons", at = @At("RETURN"))
    public void onSetButtons(CallbackInfo ci) {
        // Obtém o índice do servidor selecionado na lista visual
        int selectedIndex = this.serverListSelector.func_148193_k(); // getSelectedIndex()

        if (selectedIndex >= 0) {
            ServerData selectedServer = this.savedServerList.getServerData(selectedIndex);
            
            if (selectedServer != null && isProtected(selectedServer.serverIP)) {
                // Desabilita os botões se for um servidor protegido
                this.btnEdit.enabled = false;
                this.btnDelete.enabled = false;
            }
        }
    }

    /**
     * Verifica se o IP pertence à lista de servidores bloqueados
     */
    private boolean isProtected(String ip) {
        return ip.equalsIgnoreCase("sparkmc.com.br") || ip.equalsIgnoreCase("kaizenmc.gg");
    }

    private void addServerIfNotExists(String name, String ip) {
        if (this.savedServerList == null) return;

        boolean exists = false;
        for (int i = 0; i < this.savedServerList.countServers(); i++) {
            ServerData data = this.savedServerList.getServerData(i);
            if (data.serverIP.equalsIgnoreCase(ip)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            ServerData newServer = new ServerData(name, ip, false);
            this.savedServerList.addServerData(newServer);
            this.savedServerList.saveServerList();
            
            // Força a atualização da lista visual
            if (this.serverListSelector != null) {
                // Dependendo da versão, pode ser necessário recarregar o selector
            }
        }
    }
}