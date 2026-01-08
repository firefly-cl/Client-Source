package crack.firefly.com.System.cape.impl;

import crack.firefly.com.System.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class NormalCape extends Cape {

	private ResourceLocation sample;
	
	public NormalCape(String name, ResourceLocation sample, ResourceLocation cape, CapeCategory category) {
		super(name, cape, category);
		this.sample = sample;
	}

	public ResourceLocation getSample() {
		return sample;
	}
}
