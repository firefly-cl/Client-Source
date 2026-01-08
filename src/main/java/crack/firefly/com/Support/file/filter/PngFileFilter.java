package crack.firefly.com.Support.file.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import crack.firefly.com.Support.file.FileUtils;

public class PngFileFilter extends FileFilter {
	
    @Override
    public boolean accept(File file) {
    	
        if (file.isDirectory()) {
            return true;
        }

        String extension = FileUtils.getExtension(file);
        
        if (extension != null && extension.equalsIgnoreCase("png")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Png Images (*.png)";
    }
}
