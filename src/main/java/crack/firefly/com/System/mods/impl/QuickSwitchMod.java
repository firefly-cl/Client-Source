package crack.firefly.com.System.mods.impl;

import org.lwjgl.input.Keyboard;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventKey;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;
import crack.firefly.com.System.mods.settings.impl.KeybindSetting;
import crack.firefly.com.Support.PlayerUtils;
import net.minecraft.init.Items;

public class QuickSwitchMod extends Mod {

	private KeybindSetting swordSetting = new KeybindSetting(TranslateText.SWORD, this, Keyboard.KEY_NONE);
	private KeybindSetting blockSetting = new KeybindSetting(TranslateText.BLOCK, this, Keyboard.KEY_NONE);
	private KeybindSetting rodSetting = new KeybindSetting(TranslateText.ROD, this, Keyboard.KEY_NONE);
	private KeybindSetting axeSetting = new KeybindSetting(TranslateText.AXE, this, Keyboard.KEY_NONE);
	private KeybindSetting pickaxeSetting = new KeybindSetting(TranslateText.PICKAXE, this, Keyboard.KEY_NONE);
	private KeybindSetting bowSetting = new KeybindSetting(TranslateText.BOW, this, Keyboard.KEY_NONE);
	
	public QuickSwitchMod() {
		super(TranslateText.QUICK_SWITCH, TranslateText.QUICK_SWITCH_DESCRIPTION, ModCategory.PLAYER,"itemhotkey", true);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		
		if(event.getKeyCode() == swordSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestSword(mc.thePlayer));
		}
		
		if(event.getKeyCode() == blockSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestBlock(mc.thePlayer));
		}
		
		if(event.getKeyCode() == rodSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getItemSlot(Items.fishing_rod));
		}
		
		if(event.getKeyCode() == axeSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestAxe(mc.thePlayer));
		}
		
		if(event.getKeyCode() == pickaxeSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestPickaxe(mc.thePlayer));
		}
		
		if(event.getKeyCode() == bowSetting.getKeyCode()) {
			setCurrentItem(PlayerUtils.getBestBow(mc.thePlayer));
		}
	}
	
	private void setCurrentItem(int slot) {
		mc.thePlayer.inventory.currentItem = slot;
	}
}
