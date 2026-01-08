package crack.firefly.com.ui.comp.Providers;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.color.ColorManager;
import crack.firefly.com.System.color.palette.ColorPalette;
import crack.firefly.com.System.color.palette.ColorType;
import crack.firefly.com.System.file.FileManager;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.settings.impl.ImageSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.Multithreading;
import crack.firefly.com.Support.file.FileUtils;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompImageSelect extends Comp {

	private ImageSetting imageSetting;
	
	public CompImageSelect(float x, float y, ImageSetting imageSetting) {
		super(x, y);
		this.imageSetting = imageSetting;
	}
	
	public CompImageSelect(ImageSetting imageSetting) {
		super(0, 0);
		this.imageSetting = imageSetting;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		
		Firefly instance = Firefly.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		AccentColor accentColor = colorManager.getCurrentColor();
		ColorPalette palette = colorManager.getPalette();
		
		String name = imageSetting.getImage() == null ? TranslateText.NONE.getText() : imageSetting.getImage().getName();
		float nameWidth = nvg.getTextWidth(name, 9, Fonts.REGULAR);
		
		nvg.drawGradientRoundedRect(this.getX(), this.getY(), 16, 16, 4, accentColor.getColor1(), accentColor.getColor2());
		nvg.drawText(name, this.getX() - nameWidth - 5, this.getY() + 4, palette.getFontColor(ColorType.DARK), 9, Fonts.REGULAR);
		nvg.drawCenteredText(LegacyIcon.FOLDER, this.getX() + 8, this.getY() + 2.5F, Color.WHITE, 10, Fonts.LEGACYICON);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), 16, 16) && mouseButton == 0) {
			
			Multithreading.runAsync(() -> {
				
				File image = FileUtils.selectImageFile();
				
				if(image != null) {
					
					FileManager fileManager = Firefly.getInstance().getFileManager();
					File cacheDir = new File(fileManager.getCacheDir(), "custom-image");
					
					fileManager.createDir(cacheDir);
					
					File newImage = new File(cacheDir, image.getName());
					
					try {
						FileUtils.copyFile(image, newImage);
					} catch (IOException e) {}
					
					imageSetting.setImage(newImage);
				}
			});
		}
	}
}
