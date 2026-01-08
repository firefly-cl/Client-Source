package crack.firefly.com.Support.file.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import crack.firefly.com.Support.file.FileUtils;

public class SoundFileFilter extends FileFilter {
	
    @Override
    public boolean accept(File file) {
    	
        if (file.isDirectory()) {
            return true;
        }

        String extension = FileUtils.getExtension(file);
        
        if (extension != null && extension.equalsIgnoreCase("wav")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Sounds (*.wav)";
    }
}
