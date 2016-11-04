package walkerTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class ListTree extends SimpleFileVisitor<Path> {	
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		System.out.println("visiting file:" + file.toString());
		if (Files.exists(file,LinkOption.NOFOLLOW_LINKS)) {
			mergeFile(file);
			return FileVisitResult.CONTINUE;
		} else {
			return FileVisitResult.CONTINUE;
		}
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println(exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		System.out.println("Visited dir:" + dir.toString());
		return FileVisitResult.CONTINUE;
	}
	
	private void mergeFile(Path file) throws IOException {
		Path dstFile = Paths.get("Z:/dst.txt");
		if (!Files.exists(dstFile, LinkOption.NOFOLLOW_LINKS)) {
			Files.createFile(dstFile);
		}
		
		byte[] chBuf = new byte[1024];		
		InputStream ins = Files.newInputStream(file, StandardOpenOption.READ);
		OutputStream ous = Files.newOutputStream(dstFile,StandardOpenOption.APPEND);		
		BufferedInputStream bufReader = new BufferedInputStream(ins);
		BufferedOutputStream bufWriter = new BufferedOutputStream(ous);
		
		while ((bufReader.read(chBuf)) != -1) {
			bufWriter.write(chBuf);			
		}
		
		ins.close();
		ous.close();
		
		// Also, can you Java File Channel
	}
}
