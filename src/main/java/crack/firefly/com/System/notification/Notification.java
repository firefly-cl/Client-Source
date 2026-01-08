package crack.firefly.com.System.notification;

import java.awt.Color;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.color.AccentColor;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import crack.firefly.com.Support.ColorUtils;
import crack.firefly.com.Support.TimerUtils;
import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;
import crack.firefly.com.Support.animation.normal.easing.EaseBackIn;
import crack.firefly.com.Support.buffer.ScreenAlpha;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {

	private Animation animation;
	private String title, message;
	private NotificationType type;
	private TimerUtils timer;
	
	private ScreenAlpha screenAlpha = new ScreenAlpha();
	
	public Notification(TranslateText title, TranslateText message, NotificationType type) {
		this.title = title.getText();
		this.message = message.getText();
		this.type = type;
		this.timer = new TimerUtils();
	}
	public Notification(String title, String message, NotificationType type) {
		this.title = title;
		this.message = message;
		this.type = type;
		this.timer = new TimerUtils();
	}
	
	public void draw() {
		
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		
		screenAlpha.wrap(() -> drawNanoVG(nvg), animation.getValueFloat());
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		Firefly instance = Firefly.getInstance();
		AccentColor currentColor = instance.getColorManager().getCurrentColor();
		
		float maxWidth;
		float titleWidth = nvg.getTextWidth(title, 9.6F, Fonts.MEDIUM);
		float messageWidth = nvg.getTextWidth(message, 7.6F, Fonts.REGULAR);
		
		if(titleWidth > messageWidth) {
			maxWidth = titleWidth;
		}else {
			maxWidth = messageWidth;
		}
		
		maxWidth = maxWidth + 31;
		
		int x = (int) (sr.getScaledWidth() - maxWidth) - 8;
		int y = sr.getScaledHeight() - 29 - 8;
		
		if(timer.delay(3000)) {
			animation.setDirection(Direction.BACKWARDS);
		}
		
		nvg.save();
		nvg.translate(160 - (animation.getValueFloat() * 160), 0);
		
		nvg.drawShadow(x, y, maxWidth, 29, 6);
		nvg.drawGradientRoundedRect(x, y, maxWidth, 29, 6, ColorUtils.applyAlpha(currentColor.getColor1(), 220), ColorUtils.applyAlpha(currentColor.getColor2(), 220));
		nvg.drawText(type.getIcon(), x + 5, y + 6F, Color.WHITE, 17, Fonts.LEGACYICON);
		nvg.drawText(title, x + 26, y + 6F, Color.white, 9.6F, Fonts.MEDIUM);
		nvg.drawText(message, x + 26, y + 17.5F, Color.WHITE, 7.5F, Fonts.REGULAR);
		
		nvg.restore();
	}
	
	public void show() {
		animation = new EaseBackIn(300, 1, 0);
		animation.setDirection(Direction.FORWARDS);
		animation.reset();
		timer.reset();
	}
	
	public boolean isShown() {
		return !animation.isDone(Direction.BACKWARDS);
	}
	
	public Animation getAnimation() {
		return animation;
	}

	public TimerUtils getTimer() {
		return timer;
	}
}
