package crack.firefly.com.System.cape.impl;

import java.io.File;

import crack.firefly.com.System.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class CustomCape extends Cape {

	private File sample;
	
	public CustomCape(String name, File sample, ResourceLocation cape, CapeCategory category) {
		super(name, cape, category);
		this.sample = sample;
	}

	public File getSample() {
		return sample;
	}
}
