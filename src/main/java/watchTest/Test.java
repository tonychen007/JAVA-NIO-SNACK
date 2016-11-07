package watchTest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) {
		final Path path = Paths.get("Z:/TEMP");
		WatchService watchService = null;
		Thread th1 = null;
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
			WatchThread wth = new WatchThread(watchService);
			wth.registerTree(path);
			th1 = new Thread(wth);			
			th1.start();
			th1.join();
			watchService.close();	
		} catch (IOException | InterruptedException e) {

		}	
	}
}
