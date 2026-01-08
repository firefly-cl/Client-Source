package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventReceivePacket;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import net.minecraft.network.play.server.S02PacketChat;

public class MinemenMod extends Mod {

	private BooleanSetting autoPlaySetting = new BooleanSetting(TranslateText.AUTO_PLAY, this, false);
	
	public MinemenMod() {
		super(TranslateText.MINEMEN, TranslateText.MINEMEN_DESCRIPTION, ModCategory.OTHER);
	}

	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
		if(autoPlaySetting.isToggled() && event.getPacket() instanceof S02PacketChat) {
			
			S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
			String raw = chatPacket.getChatComponent().toString();
			
			if (raw.contains("clickEvent=ClickEvent{action=RUN_COMMAND, value='/requeue")) {
				mc.thePlayer.sendChatMessage("/requeue");
			}
		}
	}
}
