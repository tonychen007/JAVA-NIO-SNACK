package pathSystemTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import utils.PathSystem;

@SuppressWarnings("unused")
public class Test {
	private static final String ZIP_SCHEME = "jar:file"; 
	
	public static void main(String[] args) {
		FileSystem fsys =  FileSystems.getDefault();
		try {
			PathSystem pthSys = new PathSystem("","");
			pthSys.windowsFileSystem();
			pthSys.jarFileSystem();
			pthSys.normalizePath();
			
			Path base = Paths.get("Z:/TEMP/Default/Cache");			
			Path rbase = base.resolve("data_0");
			Path sibling = base.resolveSibling("Media Cache");
			
			int aaa = 0;
		} catch (URISyntaxException | IOException e) {
			
		}		
	}
}
