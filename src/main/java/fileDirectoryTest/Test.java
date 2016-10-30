package fileDirectoryTest;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) {	
		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
	}
}
