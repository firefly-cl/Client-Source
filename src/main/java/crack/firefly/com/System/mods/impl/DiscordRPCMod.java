package crack.firefly.com.System.mods.impl;

import crack.firefly.com.discord.DiscordRPC;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class DiscordRPCMod extends Mod {

	// Cria uma instância do nosso serviço Discord RPC
	private DiscordRPC discord = new DiscordRPC();
	
	public DiscordRPCMod() {
		// Define o mod com informações básicas.
		// Se o framework permitir, você pode criar uma ModCategory.INTERNAL para ocultá-lo da UI.
		super(TranslateText.DISCORD_RPC, TranslateText.DISCORD_RPC_DESCRIPTION, ModCategory.OTHER);
        
        // Se a classe base 'Mod' tiver um método para desabilitar a opção de desligar, use-o aqui.
        // Exemplo: this.setToggleable(false);
	}

	@Override
	public void onEnable() {
		// Ao ser ativado (automaticamente na inicialização do jogo), o RPC é iniciado.
		super.onEnable();
		discord.start();
	}
	
	// O método onDisable() foi REMOVIDO INTENCIONALMENTE.
	// Isso impede que o gerenciador de mods chame o método de parada do RPC,
	// garantindo que o mod esteja sempre ativo (o RPC só parará ao fechar o jogo).
}