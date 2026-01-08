package crack.firefly.com.System.mods.impl;

import java.util.Arrays;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventJoinServer;
import crack.firefly.com.System.event.impl.EventReceivePacket;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.ServerUtils;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;

public class SessionInfoMod extends HUDMod {

	private int killCount;
	private String[] killTrigger = {"by *", "para *", "fue destrozado a manos de *"};
	
	private long startTime;
	
	public SessionInfoMod() {
		super(TranslateText.SESSION_INFO, TranslateText.SESSION_INFO_DESCRIPTION, "stats");
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		String time;
		
		if(mc.isSingleplayer()) {
			time = "Singleplayer";
		}else {
			long durationInMillis = System.currentTimeMillis() - startTime;
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
            time = String.format("%02d:%02d:%02d", hour, minute, second);
		}
		
		this.drawBackground(140, 64);
		this.drawText("Session Info", 5.5F, 6F, 10.5F, getHudFont(1));
		this.drawRect(0, 17.5F, 140, 1);
		
		this.drawText(LegacyIcon.CLOCK, 5.5F, 22.5F, 10F, Fonts.LEGACYICON);
		this.drawText(time, 18, 24, 9, getHudFont(1));
		
		this.drawText(LegacyIcon.SERVER, 5.5F, 22.5F + 13, 10F, Fonts.LEGACYICON);
		this.drawText(ServerUtils.getServerIP(), 18, 24 + 12, 9, getHudFont(1));
		
		this.drawText(LegacyIcon.USER, 5.5F, 22.5F + 26, 10F, Fonts.LEGACYICON);
		this.drawText(killCount + " kill", 18, 24 + 26.5F, 9, getHudFont(1));
		
		this.setWidth(140);
		this.setHeight(64);
	}
	
	@EventTarget
	public void onJoinServer(EventJoinServer event) {
		startTime = System.currentTimeMillis();
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
        if (ServerUtils.isHypixel() && event.getPacket() instanceof S02PacketChat) {
        	
            S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
            String chatMessage = chatPacket.getChatComponent().getUnformattedText();
	
            String message = StringUtils.stripControlCodes(chatMessage);
            
            if (!message.contains(":") && Arrays.stream(killTrigger).anyMatch(message.replace(mc.thePlayer.getName(), "*")::contains)) {
                killCount++;
            }
        }
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		startTime = System.currentTimeMillis();
	}
}
