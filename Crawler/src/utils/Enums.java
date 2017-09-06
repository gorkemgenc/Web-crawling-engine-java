package utils;

import java.io.File;
import java.nio.file.Path;

public class Enums {

	/** Crawler uses this directory to store documents **/
	public static final String MAP_URL_FILE1 = ".url1.map";
        public static final String MAP_URL_FILE2 = ".url2.map";
        public static final String MAP_URL_FILE3 = ".url3.map";
        public static final String MAP_URL_FILE4 = ".url4.map";
	
	public static boolean createDirectory(Path path) {
		String pathStr = path.toString();
		File dir = new File(pathStr);
		
		if (!dir.exists()) {
			try {
				dir.mkdir();
				return true;
			} catch (SecurityException se) {
				return false;
			}
		} else {
			return false;
		}
	}
}