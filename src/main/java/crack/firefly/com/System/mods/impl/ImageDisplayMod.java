package crack.firefly.com.System.mods.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.ImageSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;

public class ImageDisplayMod extends HUDMod {

	private NumberSetting radiusSetting = new NumberSetting(TranslateText.RADIUS, this, 6, 2, 64, true);
	private NumberSetting alphaSetting = new NumberSetting(TranslateText.ALPHA, this, 1.0F, 0.0F, 1.0F, false);
	private BooleanSetting shadowSetting = new BooleanSetting(TranslateText.SHADOW, this, false);
	private ImageSetting imageSetting = new ImageSetting(TranslateText.IMAGE, this);
	
	private BufferedImage image;
	private File prevImage;
	
	public ImageDisplayMod() {
		super(TranslateText.IMAGE_DISPLAY, TranslateText.IMAGE_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		if(imageSetting.getImage() != null && prevImage != imageSetting.getImage()) {
			
			prevImage = imageSetting.getImage();
			
			try {
				image = ImageIO.read(imageSetting.getImage());
			} catch (IOException e) {}
		}
		
		if(image != null) {
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			if(width > 500 || height > 500) {
				
				if((width < 1000 || height < 1000)) {
					width = width / 2;
					height = height / 2;
				}
				
				if((width > 1000 || height > 1000)) {
					width = width / 3;
					height = height / 3;
				}
			}
			
			if(shadowSetting.isToggled()) {
				this.drawShadow(0, 0, width, height, radiusSetting.getValueFloat());
			}
			
			this.drawRoundedImage(imageSetting.getImage(), 0, 0, width, height, radiusSetting.getValueFloat(), alphaSetting.getValueFloat());
			
			this.setWidth(width);
			this.setHeight(height);
		}
	}
}
