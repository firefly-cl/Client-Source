package crack.firefly.com.System.file;

import java.io.File;
import java.io.IOException;

import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.Firefly;
import net.minecraft.client.Minecraft;

public class FileManager {

	private File glideDir, profileDir, cacheDir, screenshotDir, soarDir, customCapeDir, capeCacheDir;
	private boolean migrationSuccess = false;
	
	public FileManager() {
		
		glideDir = new File(Minecraft.getMinecraft().mcDataDir, "Firefly-Profile");
		soarDir = new File(Minecraft.getMinecraft().mcDataDir, "Firefly");
		profileDir = new File(glideDir, "profile");
		cacheDir = new File(glideDir, "cache");
		screenshotDir = new File(glideDir, "screenshots");
		customCapeDir = new File(cacheDir, "custom-cape");
		capeCacheDir = new File(cacheDir, "cape");

		try{

			if(!glideDir.exists()){
				if(soarDir.exists()) {
					migrationSuccess = soarDir.renameTo(glideDir);
					if(!migrationSuccess) createDir(glideDir);
				} else {
					createDir(glideDir);
				}
			}

			if(!profileDir.exists()) createDir(profileDir);
			if(!cacheDir.exists()) createDir(cacheDir);
			if(!screenshotDir.exists()) createDir(screenshotDir);
			if(!customCapeDir.exists()) createDir(customCapeDir);
			if(!capeCacheDir.exists()) createDir(capeCacheDir);

			createVersionFile();

		} catch (Exception e) {
			FireflyConsole.error("Algo deu muito errado ao tentar criar a pasta deslizante, o que pode resultar em travamentos posteriormente", e);
		}

	}
	
	private void createVersionFile() {
		
		File versionDir = new File(cacheDir, "version");
		
		createDir(versionDir);
		createFile(new File(versionDir, Firefly.getInstance().getVersionIdentifier() + ".tmp"));
	}
	
	public void createDir(File file) {
		file.mkdir();
	}
	
	public void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			FireflyConsole.error("Falha ao criar arquivo " + file.getName(), e);
		}
	}

	public File getScreenshotDir() {
		return screenshotDir;
	}

	public File getGlideDir() {
		return glideDir;
	}

	public File getProfileDir() {
		return profileDir;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public File getCustomCapeDir() {
		return customCapeDir;
	}

	public File getCapeCacheDir() {
		return capeCacheDir;
	}

}
