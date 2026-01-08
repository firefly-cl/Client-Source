package crack.firefly.com.System.mods.impl;

import crack.firefly.com.Console.FireflyConsole;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import crack.firefly.com.Loader.Abstractions.IMixinMinecraft;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventPreRenderTick;
import crack.firefly.com.System.event.impl.EventToggleFullscreen;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class BorderlessFullscreenMod extends Mod {

	private int prevX, prevY, prevWidth, prevHeight;
	
	private long fullscreenTime = -1;
	
	public BorderlessFullscreenMod() {
		super(TranslateText.BORDERLESS_FULSCREEN, TranslateText.BORDERLESS_FULLSCREEN_DESCRIPTION, ModCategory.OTHER);
	}

	@EventTarget
	public void onRenderTick(EventPreRenderTick event) {
		if(fullscreenTime != -1 && System.currentTimeMillis() - fullscreenTime >= 100) {
			fullscreenTime = -1;
			
			if(mc.inGameHasFocus) {
				mc.mouseHelper.grabMouseCursor();
			}
		}
	}
	
	@EventTarget
	public void onFullscreenToggle(EventToggleFullscreen event) {
		event.setApplyState(false);
		setBorderlessFullscreen(event.getState());
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		if(mc.isFullScreen()) {
			setBorderlessFullscreen(true);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		if(mc.isFullScreen()) {
			setBorderlessFullscreen(false);
			mc.toggleFullscreen();
			mc.toggleFullscreen();
		}
	}
	
	private void setBorderlessFullscreen(boolean state) {
		try {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", Boolean.toString(state));
			Display.setFullscreen(false);
			Display.setResizable(!state);

			if(state) {
				prevX = Display.getX();
				prevY = Display.getY();
				prevWidth = mc.displayWidth;
				prevHeight = mc.displayHeight;
				Display.setDisplayMode(new DisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight()));
				Display.setLocation(0, 0);
				((IMixinMinecraft)mc).resizeWindow(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight());
			}
			else {
				Display.setDisplayMode(new DisplayMode(prevWidth, prevHeight));
				Display.setLocation(prevX, prevY);
				((IMixinMinecraft)mc).resizeWindow(prevWidth, prevHeight);

				if(mc.inGameHasFocus) {
					mc.mouseHelper.ungrabMouseCursor();
					fullscreenTime = System.currentTimeMillis();
				}
			}
		}
		catch(LWJGLException error) {
			FireflyConsole.error("Could not totggle borderless fullscreen", error);
		}
	}
}
