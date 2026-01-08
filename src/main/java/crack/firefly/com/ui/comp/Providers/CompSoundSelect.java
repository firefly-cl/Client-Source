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
import crack.firefly.com.System.mods.settings.impl.SoundSetting;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.ui.comp.Comp;
import crack.firefly.com.Support.Multithreading;
import crack.firefly.com.Support.file.FileUtils;
import crack.firefly.com.Support.mouse.MouseUtils;

public class CompSoundSelect extends Comp {

	private SoundSetting soundSetting;
	
	public CompSoundSelect(float x, float y, SoundSetting soundSetting) {
		super(x, y);
		this.soundSetting = soundSetting;
	}
	
	public CompSoundSelect(SoundSetting soundSetting) {
		super(0, 0);
		this.soundSetting = soundSetting;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		
		Firefly instance = Firefly.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		ColorManager colorManager = instance.getColorManager();
		AccentColor accentColor = colorManager.getCurrentColor();
		ColorPalette palette = colorManager.getPalette();
		
		String name = soundSetting.getSound() == null ? TranslateText.NONE.getText() : soundSetting.getSound().getName();
		float nameWidth = nvg.getTextWidth(name, 9, Fonts.REGULAR);
		
		nvg.drawGradientRoundedRect(this.getX(), this.getY(), 16, 16, 4, accentColor.getColor1(), accentColor.getColor2());
		nvg.drawText(name, this.getX() - nameWidth - 5, this.getY() + 4, palette.getFontColor(ColorType.DARK), 9, Fonts.REGULAR);
		nvg.drawCenteredText(LegacyIcon.FOLDER, this.getX() + 8, this.getY() + 2.5F, Color.WHITE, 10, Fonts.LEGACYICON);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), 16, 16) && mouseButton == 0) {
			
			Multithreading.runAsync(() -> {
				
				File sound = FileUtils.selectSoundFile();
				
				if(sound != null) {
					
					FileManager fileManager = Firefly.getInstance().getFileManager();
					File cacheDir = new File(fileManager.getCacheDir(), "custom-sound");
					
					fileManager.createDir(cacheDir);
					
					File newImage = new File(cacheDir, sound.getName());
					
					try {
						FileUtils.copyFile(sound, newImage);
					} catch (IOException e) {}
					
					soundSetting.setSound(newImage);
				}
			});
		}
	}
}