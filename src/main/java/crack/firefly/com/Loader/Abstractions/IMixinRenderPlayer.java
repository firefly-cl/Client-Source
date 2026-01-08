package crack.firefly.com.Loader.Abstractions;

import crack.firefly.com.System.mods.impl.skin3d.layers.BodyLayerFeatureRenderer;
import crack.firefly.com.System.mods.impl.skin3d.layers.HeadLayerFeatureRenderer;

public interface IMixinRenderPlayer {
	public boolean hasThinArms();
	public HeadLayerFeatureRenderer getHeadLayer();
	public BodyLayerFeatureRenderer getBodyLayer();
}