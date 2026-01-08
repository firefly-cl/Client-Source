package crack.firefly.com.System.command.impl;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.command.Command;
import crack.firefly.com.System.file.FileManager;
import crack.firefly.com.Support.transferable.FileTransferable;
import net.minecraft.util.ChatComponentText;

public class ScreenshotCommand extends Command {

	public ScreenshotCommand() {
		super("screenshot");
	}

	@Override
	public void onCommand(String message) {
		
		FileManager fileManager = Firefly.getInstance().getFileManager();
		String[] args = message.split(" ");
		File file = new File(fileManager.getScreenshotDir(), args[1]);
		
		if(args.length < 1) {
			return;
		}
		
		if(args[0].equals("open")) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(args[0].equals("copy")) {
            FileTransferable selection = new FileTransferable(file);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		}
		
		if(args[0].equals("del")) {
			file.delete();
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(args[1] + " has been deleted"));
		}
	}
}
