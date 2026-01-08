package crack.firefly.com.Loader.Abstractions;

import net.minecraft.client.multiplayer.WorldClient;

public interface IMixinRenderGlobal {
	WorldClient getWorldClient();
}
