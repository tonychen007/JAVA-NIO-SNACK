package walkerTest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.StringTokenizer;

@SuppressWarnings("unused")
public class Search implements FileVisitor<Path> {

	private Path searchedFile;
	private PathMatcher matcher;
	private boolean found;
	private StringTokenizer token;
	private ArrayList<String> wordsarray = new ArrayList<>();

	public Search(Path target) {
		this.searchedFile = target;
		this.found = false;
	}
	
	public Search(String glob) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		
		String sb = "version,encoding";		
		StringTokenizer st = new StringTokenizer(sb, ",");
		while (st.hasMoreTokens()) {
			wordsarray.add(st.nextToken());
		}
	}
	
	public void search(Path target) {
		Path name = target.getFileName();
		if (name != null && name.equals(searchedFile)) {
			System.out.println("Found file:" + target.toString());
			this.found = true;
		}
	}
	
	public void searchMatcher(Path target) {
		Path name = target.getFileName();
		if (name != null && matcher.matches(name)) {
			System.out.println("Found file:" + target.toString());
			this.found = true;
		}
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	   	searchMatcher(file);
	   	//return this.found ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
	   	return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		System.out.println("visited:" + dir.toString());
		return FileVisitResult.CONTINUE;
	}

}
