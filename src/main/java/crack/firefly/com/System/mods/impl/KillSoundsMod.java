package crack.firefly.com.System.mods.impl;

import java.io.File;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventMotionUpdate;
import crack.firefly.com.System.event.impl.EventTick;
import crack.firefly.com.System.event.impl.EventUpdate;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.BooleanSetting;
import crack.firefly.com.System.mods.settings.impl.NumberSetting;
import crack.firefly.com.System.mods.settings.impl.SoundSetting;
import crack.firefly.com.Support.Sound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class KillSoundsMod extends Mod {

	private EntityLivingBase target;
	
	private File prevCustomSound;
	
	private Sound oofSound = new Sound();
	private Sound customSound = new Sound();
	
	private NumberSetting volumeSetting = new NumberSetting(TranslateText.VOLUME, this, 0.5, 0.0, 1.0, false);
	private BooleanSetting customSoundSetting = new BooleanSetting(TranslateText.CUSTOM_SOUND, this, false);
	private SoundSetting soundSetting = new SoundSetting(TranslateText.SOUND, this);
	
	public KillSoundsMod() {
		super(TranslateText.KILL_SOUNDS, TranslateText.KILL_SOUNDS_DESCRIPTION, ModCategory.OTHER);
	}

	@EventTarget
	public void onTick(EventTick event) {
		
		if(customSoundSetting.isToggled()) {
			
			if(soundSetting.getSound() != null) {
				
				if(prevCustomSound != soundSetting.getSound()) {
					
					prevCustomSound = soundSetting.getSound();
					
					try {
						customSound.loadClip(soundSetting.getSound());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				customSound.setVolume(volumeSetting.getValueFloat());
			}
		} else {
			oofSound.setVolume(volumeSetting.getValueFloat());
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.objectMouseOver != null & mc.objectMouseOver.entityHit != null) {
			if(mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
				target = (EntityLivingBase) mc.objectMouseOver.entityHit;
			}
		}
	}
	
	@EventTarget
	public void onPreMotionUpdate(EventMotionUpdate event) {
		
		if (target != null && !mc.theWorld.loadedEntityList.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 100) {
			
			if (mc.thePlayer.ticksExisted > 3) {
				
				if(customSoundSetting.isToggled()) {
					customSound.play();
				} else {
					oofSound.play();
				}
			}
			
			target = null;
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		try {
			oofSound.loadClip(new ResourceLocation("soar/audio/oof.wav"));
		} catch (Exception e) {}
	}
}
