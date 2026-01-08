package crack.firefly.com.System.security.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventReceivePacket;
import crack.firefly.com.System.security.SecurityFeature;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class TeleportSecurity extends SecurityFeature {

	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			
			S08PacketPlayerPosLook pos = ((S08PacketPlayerPosLook) event.getPacket());
			
			if(Math.abs(pos.getX()) > 1E+9 || Math.abs(pos.getY()) > 1E+9 || Math.abs(pos.getZ()) > 1E+9) {
				event.setCancelled(true);
			}
		}
	}
}
