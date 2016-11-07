package watchTest;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class WatchThread implements Runnable {

	private final WatchService watchService;
	private final Map<WatchKey, Path> directories = new HashMap<>();

	public WatchThread(final WatchService watchService) {
		this.watchService = watchService;
	}

	@Override
	public void run() {
		try {
			registerTree(Paths.get("Z:/TEMP"));
		} catch (IOException e) {

		}
		
		while (true) {
			WatchKey watchKey = null;
			try {
				watchKey = watchService.take();
				for (WatchEvent<?> e : watchKey.pollEvents()) {
					final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) e;
					final Path filename = watchEventPath.context();
					System.out.println(e.kind().name());
					System.out.println(filename);

					if (e.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						Path directory_path = directories.get(watchKey);
						Path child;
						if (directory_path != null) {
							child = directory_path.resolve(filename);

							if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
								registerTree(child);
							}
						}
					}
				}
				boolean isValid = watchKey.reset();
				if (!isValid) {
					directories.remove(watchKey);

					if (directories.isEmpty()) {
						break;
					}
				}
			} catch (InterruptedException | IOException e) {

			}
		}
	}

	public void registerPath(Path path) throws IOException {
		WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

		directories.put(key, path);
	}

	public void registerTree(Path dir) throws IOException {
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerPath(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
