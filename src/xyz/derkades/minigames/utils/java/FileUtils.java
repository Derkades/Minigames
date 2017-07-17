package xyz.derkades.minigames.utils.java;

import java.io.File;

public class FileUtils {
	
	public static String getFileName(File file){
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}

}
