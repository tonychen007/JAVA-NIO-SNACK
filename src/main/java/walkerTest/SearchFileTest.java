package walkerTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class SearchFileTest {

	public static void main(String[] args) throws IOException {
		Path target = Paths.get("leslie.png");
		Search walk = new Search("*.xml");
		Files.walkFileTree(Paths.get("Z:/"), walk);

		// Copy file
		Path copyFrom = Paths.get("Z:/temp");
		Path copyTo = Paths.get("Z:/temp_copy");
		CopyFile walk1 = new CopyFile(copyFrom, copyTo);
		Files.walkFileTree(copyFrom, walk1);
	}
}
