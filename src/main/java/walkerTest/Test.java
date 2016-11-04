package walkerTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) {
		Path tmpDir = Paths.get("D:/05.LinuxSoft");				
		ListTree lstTree = new ListTree();
		
		try {
			Path retPath = Files.walkFileTree(tmpDir, lstTree);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
