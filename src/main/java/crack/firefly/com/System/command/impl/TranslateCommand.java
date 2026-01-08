package crack.firefly.com.System.command.impl;

import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.System.command.Command;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.impl.ChatTranslateMod;
import crack.firefly.com.System.mods.settings.impl.ComboSetting;
import crack.firefly.com.System.mods.settings.impl.combo.Option;
import crack.firefly.com.Support.Multithreading;
import crack.firefly.com.Support.translate.Translator;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class TranslateCommand extends Command {

	private String to = Translator.JAPANESE;
	
	public TranslateCommand() {
		super("translate");
	}

	@Override
	public void onCommand(String message) {
		
		ComboSetting setting = ChatTranslateMod.getInstance().getLanguageSetting();
		Option option = setting.getOption();
		
		if(option.getTranslate().equals(TranslateText.JAPANESE)) {
			to = Translator.JAPANESE;
		} else if(option.getTranslate().equals(TranslateText.ENGLISH)) {
			to = Translator.ENGLISH;
		} else if(option.getTranslate().equals(TranslateText.CHINESE)) {
			to = Translator.CHINESE_SIMPLIFIED;
		} else if(option.getTranslate().equals(TranslateText.POLISH)) {
			to = Translator.POLISH;
		}
		
		String text = message;
		
		Multithreading.runAsync(()-> {
			try {
				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Translate] " + EnumChatFormatting.WHITE + Translator.translate(text, Translator.AUTO_DETECT, to)));
			} catch (Exception e) {
				FireflyConsole.error("Failed translate", e);
			}
		});
	}
}
