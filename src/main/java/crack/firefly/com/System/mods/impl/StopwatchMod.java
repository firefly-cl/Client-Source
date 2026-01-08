package crack.firefly.com.System.mods.impl;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.SimpleHUDMod;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.System.nanovg.font.LegacyIcon;
import crack.firefly.com.Support.TimerUtils;

public class StopwatchMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	private KeybindSetting keybindSetting = new KeybindSetting(TranslateText.KEYBIND, this, Keyboard.KEY_P);
	
	private TimerUtils timer = new TimerUtils();
	private int pressCount;
	private float currentTime;
	private DecimalFormat timeFormat = new DecimalFormat("0.00");
	
	public StopwatchMod() {
		super(TranslateText.STOPWATCH, TranslateText.STOPWATCH_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		switch(pressCount){
			case 0:
				timer.reset();
				break;
			case 1:
				currentTime = (timer.getElapsedTime() / 1000F);
				break;
			case 3:
				timer.reset();
				currentTime = 0;
				pressCount = 0;
				break;
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKeyCode() == keybindSetting.getKeyCode()) {
			pressCount++;
		}
	}
	
	@Override
	public String getText() {
		return timeFormat.format(currentTime) + " s";
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? LegacyIcon.WATCH : null;
	}
	
	@Override
	public void onEnable() {
		
		super.onEnable();
		
		if(timer != null) {
			timer.reset();
		}
		
		pressCount = 0;
		currentTime = 0;
	}
}
