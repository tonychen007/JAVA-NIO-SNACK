package walkerTest;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class CopyFile implements FileVisitor<Path> {

	private final Path copyFrom;
	private final Path copyTo;

	public CopyFile(Path copyFrom, Path copyTo) {
		this.copyFrom = copyFrom;
		this.copyTo = copyTo;
	}

	static void copySubTree(Path copyFrom, Path copyTo) throws IOException {
		try {
			Files.copy(copyFrom, copyTo, REPLACE_EXISTING, COPY_ATTRIBUTES);
		} catch (IOException e) {
			System.err.println("Unable to copy " + copyFrom + " [" + e + "]");
		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		System.out.println("Copy directory: " + (Path) dir);
		Path rel = copyFrom.relativize(dir);
		Path newdir = copyTo.resolve(rel);
		try {
			Files.copy(dir, newdir, REPLACE_EXISTING, COPY_ATTRIBUTES);
		} catch (IOException e) {
			System.err.println("Unable to create " + newdir + " [" + e + "]");
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		System.out.println("Copy file: " + (Path) file);
		Path rel = copyFrom.relativize(file);
		Path dst = copyTo.resolve(rel);
		copySubTree(file, dst);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (exc == null) {
			Path rel = copyFrom.relativize(dir);
			Path newdir = copyTo.resolve(rel);
			try {
				FileTime time = Files.getLastModifiedTime(dir);
				Files.setLastModifiedTime(newdir, time);
			} catch (IOException e) {
				System.err.println("Unable to copy all attributes to: " + newdir + " [" + e + "]");
			}
		} else {
			throw exc;
		}
		return FileVisitResult.CONTINUE;
	}
}
