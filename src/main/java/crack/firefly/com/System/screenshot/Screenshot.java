package crack.firefly.com.System.screenshot;

import java.io.File;

import crack.firefly.com.Support.animation.simple.SimpleAnimation;

public class Screenshot {

	private SimpleAnimation selectAnimation = new SimpleAnimation();
	
	private String name;
	private File image;
	
	public Screenshot(File image) {
		this.image = image;
		this.name = image.getName().replace(".png", "");
	}

	public SimpleAnimation getSelectAnimation() {
		return selectAnimation;
	}

	public String getName() {
		return name;
	}

	public File getImage() {
		return image;
	}
}
