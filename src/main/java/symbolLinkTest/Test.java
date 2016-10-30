package symbolLinkTest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) throws IOException {
		Path link = FileSystems.getDefault().getPath("C:/symbol.txt");
		Path target = FileSystems.getDefault().getPath("Z:/1.txt");
		if(!Files.isSymbolicLink(link)) {
			Path sl = Files.createSymbolicLink(link,target);		}
		
		// To check if the path is a symbolicLink, LinkOption.NOFOLLOW_LINKS should be passed
		BasicFileAttributes attr = Files.readAttributes(link, BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS);
		boolean ret = attr.isSymbolicLink();
		
		ret = Files.isSameFile(link, target);
	}
}
