package crack.firefly.com.menus.main.Providers;

import java.awt.*;
import java.net.URI;

import crack.firefly.com.Firefly;
import crack.firefly.com.menus.main.FireflyUI;
import crack.firefly.com.System.remote.update.Update;
import crack.firefly.com.Support.mouse.MouseUtils;
import org.lwjgl.input.Keyboard;

import crack.firefly.com.menus.main.LandingView;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;

public class Updater extends LandingView {

	public Updater(FireflyUI parent) {
		super(parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		Firefly instance = Firefly.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG(mouseX, mouseY, sr, instance, nvg));
	}

	private void drawNanoVG(int mouseX, int mouseY, ScaledResolution sr, Firefly instance, NanoVGManager nvg) {
		nvg.drawRect(0,0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0, 100));
		int acWidth = 220;
		int acHeight = 190;
		int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
		int acY = sr.getScaledHeight() / 2 - (acHeight / 2);
		Update u = instance.getUpdateInstance();
		nvg.drawRoundedRect(acX, acY, acWidth, acHeight, 8, this.getBackgroundColor());
		nvg.drawCenteredText("Update Available", acX + (acWidth / 2), acY + 12, Color.WHITE, 14, Fonts.MEDIUM);
		nvg.drawCenteredText("Would you like to update?", acX + (acWidth / 2), acY + 30, Color.WHITE, 9, Fonts.REGULAR);
		nvg.drawCenteredText(instance.getVersion() + " -> " + u.getVersionString(), acX + (acWidth / 2), acY + 48, Color.WHITE, 9, Fonts.REGULAR);
		nvg.drawCenteredText(instance.getVersionIdentifier() + " -> " + u.getBuildID(), acX + (acWidth / 2), acY + 60, Color.WHITE, 5, Fonts.REGULAR);
		nvg.drawRoundedRect(acX + acWidth/2 - 90, acY + acHeight - 64, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawCenteredText("Go to update", acX + acWidth/2, acY + acHeight - 54 - (nvg.getTextHeight("Go to update", 9.5F, Fonts.REGULAR)/2), Color.WHITE, 9.5F, Fonts.REGULAR);
		nvg.drawRoundedRect(acX + acWidth/2 - 90, acY + acHeight - 32, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawCenteredText("Maybe Later", acX + acWidth/2, acY + acHeight - 22 - (nvg.getTextHeight("Maybe Later", 9.5F, Fonts.REGULAR)/2), Color.WHITE, 9.5F, Fonts.REGULAR);

	}

	public void exitGui(){
		Firefly instance = Firefly.getInstance();
		instance.setUpdateNeeded(false);
		this.setCurrentScene(this.getSceneByClass(FireflyStage.class));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			ScaledResolution sr = new ScaledResolution(mc);
			int acWidth = 220;
			int acHeight = 190;
			int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
			int acY = sr.getScaledHeight() / 2 - (acHeight / 2);
			Firefly instance = Firefly.getInstance();
			if (MouseUtils.isInside(mouseX, mouseY, acX + acWidth/2 - 90, acY + acHeight - 64, 180, 20)) {
				try{ Desktop.getDesktop().browse(new URI(instance.getUpdateInstance().getUpdateLink())); } catch (Exception ignored) {}
			}
			if (MouseUtils.isInside(mouseX, mouseY, acX + acWidth/2 - 90, acY + acHeight - 32, 180, 20)) {
				exitGui();
			}
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			exitGui();
		}
	}
}
