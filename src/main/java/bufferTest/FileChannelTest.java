package bufferTest;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileChannelTest {

	public static void main(String[] args) {
		Path from = Paths.get("Z:/1.tmp");
		Path to = Paths.get("Z:/2.tmp");

		try (
			FileChannel channel_from = FileChannel.open(from, EnumSet.of(StandardOpenOption.READ));
			FileChannel channel_to = FileChannel.open(to, EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE))) {
			
			channel_from.transferTo(0L, channel_from.size(), channel_to);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
