package crack.firefly.com.menus.main;

import java.awt.Color;

import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import net.minecraft.client.Minecraft;

public class LandingView {

	public Minecraft mc = Minecraft.getMinecraft();
	private FireflyUI parent;
	
	private SimpleAnimation animation = new SimpleAnimation();
	
	public LandingView(FireflyUI parent) {
		this.parent = parent;
	}
	
	public void initScene() {}
	
	public void initGui() {}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
	
	public void keyTyped(char typedChar, int keyCode) {}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
	
	public void handleInput() {}
	
	public void onGuiClosed() {}
	
	public void onSceneClosed() {}
	
	public FireflyUI getParent() {
		return parent;
	}

	public void setCurrentScene(LandingView scene) {
		parent.setCurrentScene(scene);
	}
	
	public Color getBackgroundColor() {
		return parent.getBackgroundColor();
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}
	
	public LandingView getSceneByClass(Class<? extends LandingView> clazz) {
		return parent.getSceneByClass(clazz);
	}
}
