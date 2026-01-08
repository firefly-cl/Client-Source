package crack.firefly.com;

import crack.firefly.com.System.event.impl.*;
import org.apache.commons.lang3.StringUtils;

import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.cape.CapeManager;
import crack.firefly.com.System.cape.impl.Cape;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.profile.Profile;
import crack.firefly.com.Support.OptifineUtils;
import crack.firefly.com.Support.TargetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.util.ResourceLocation;

public class FireflyManipulador {

	private Minecraft mc = Minecraft.getMinecraft();
	
	private Firefly instance;
	
	private String prevOfflineName;
	private ResourceLocation offlineSkin;
	
	public FireflyManipulador() {
		instance = Firefly.getInstance();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		OptifineUtils.disableFastRender();
	}
	
	@EventTarget
	public void onJoinServer(EventJoinServer event) {
		for(Profile p : instance.getProfileManager().getProfiles()) {
			if(!p.getServerIp().isEmpty() && StringUtils.containsIgnoreCase(event.getIp(), p.getServerIp())) {
				instance.getModManager().disableAll();
				instance.getProfileManager().load(p.getJsonFile());
				break;
			}
		}

		instance.getRestrictedMod().joinServer(event.getIp());
	}

	@EventTarget
	public void onLoadWorld(EventLoadWorld event) {
		instance.getRestrictedMod().joinWorld();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		TargetUtils.onUpdate();
	}
	
	@EventTarget
	public void onClickMouse(EventClickMouse event) {
        if (mc.gameSettings.keyBindTogglePerspective.isPressed()) {
            mc.gameSettings.thirdPersonView = (mc.gameSettings.thirdPersonView + 1) % 3;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
    	if(event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof GuiModMenu) {
    		event.setCancelled(true);
    	}
	}
	
	@EventTarget
	public void onCape(EventLocationCape event) {
		
		CapeManager capeManager = instance.getCapeManager();
		
		if(event.getPlayerInfo() != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
			
			Cape currentCape = capeManager.getCurrentCape();
			
			if(!currentCape.equals(capeManager.getCapeByName("None"))) {
				event.setCancelled(true);
				event.setCape(currentCape.getCape());
			}
		}
	}
}
