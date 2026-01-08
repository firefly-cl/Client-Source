package crack.firefly.com.System.mods.impl;

import java.text.DecimalFormat;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRender2D;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.HUDMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;

public class HorseStatsMod extends HUDMod {

	private DecimalFormat df = new DecimalFormat("0.0");
	
	public HorseStatsMod() {
		super(TranslateText.HORSE_STATS, TranslateText.HORSE_STATS_DESCRIPTION);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		String speed = "Speed: 0.0 b/s";
		String jump = "Jump: 0.0 Blocks";
		
		if(mc.objectMouseOver.entityHit instanceof EntityHorse) {
			
			EntityHorse horse = (EntityHorse) mc.objectMouseOver.entityHit;
			
			if(!mc.thePlayer.isRidingHorse()) {
				speed = "Speed: " + this.getHorseSpeedRounded(horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()) + " b/s";
				jump = "Jump: " + df.format(horse.getHorseJumpStrength() * 5.5) + " Blocks";
			}
		}
		
		this.drawBackground(95, 28);
		
		this.drawText(speed, 5.5F, 5.5F, 9, getHudFont(1));
		this.drawText(jump, 5.5F, 15.5F, 9, getHudFont(1));
		
		this.setWidth(95);
		this.setHeight(28);
	}
	
    private String getHorseSpeedRounded(double baseSpeed) {
    	
        final float factor = 43.1703703704f;
        
        float speed = (float) (baseSpeed * factor);
        
        return df.format(speed);
    }
}
