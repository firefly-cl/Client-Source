package crack.firefly.com.System;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload; // Nome correto na 1.8.9
import net.minecraft.network.play.server.S3FPacketCustomPayload; // Nome correto na 1.8.9

public class FireflyNetwork {

    private static final String CHANNEL = "firefly:check";

    public static void sendAnnounce() {
        if (Minecraft.getMinecraft().getNetHandler() != null) { // getNetHandler na 1.8.9
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeString("v1.0");
            
            // C17PacketCustomPayload na 1.8.9
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C17PacketCustomPayload(CHANNEL, buffer));
        }
    }

    public static boolean onPacketReceive(Object packet) {
        // S3FPacketCustomPayload na 1.8.9
        if (packet instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload wrapper = (S3FPacketCustomPayload) packet;
            
            if (wrapper.getChannelName().equals(CHANNEL)) {
                return true;
            }
        }
        return false;
    }
}