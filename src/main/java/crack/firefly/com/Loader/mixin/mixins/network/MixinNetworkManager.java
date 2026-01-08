package crack.firefly.com.Loader.mixin.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.netty.channel.ChannelHandlerContext;
import crack.firefly.com.System.event.impl.EventReceivePacket;
import crack.firefly.com.System.event.impl.EventSendPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    // Instâncias únicas para evitar "new" constante
    private static final EventSendPacket SEND_EVENT = new EventSendPacket(null);
    private static final EventReceivePacket RECEIVE_EVENT = new EventReceivePacket(null);

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void preSendPacket(Packet<?> packet, CallbackInfo ci) {
        SEND_EVENT.setPacket(packet);
        SEND_EVENT.setCancelled(false);
        SEND_EVENT.call();

        if (SEND_EVENT.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void preChannelRead0(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        RECEIVE_EVENT.setPacket(packet);
        RECEIVE_EVENT.setCancelled(false);
        RECEIVE_EVENT.call();

        if (RECEIVE_EVENT.isCancelled()) {
            ci.cancel();
        }
    }
}