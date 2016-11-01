package fileDirectoryTest;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.imageio.ImageIO;

@SuppressWarnings("all")
public class Test {

	private static DirectoryStream.Filter<Path> filterPath;

	static {
		filterPath = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path entry) throws IOException {
				boolean ret = entry.getFileName().toString().endsWith(".xml");
				return ret;
			}
		};
	}

	public static void main(String[] args) throws IOException {
		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
		printDir(Paths.get("Z:/TEMP"));
		//createFile();
		createUnBufferedFile();
	}

	public static void printDir(Path path) throws IOException {
		DirectoryStream<Path> dirStream = Files.newDirectoryStream(path,filterPath);

		for (Path p : dirStream) {
			if (Files.isDirectory(p)) {
				printDir(p);
			}
			System.out.println(p.getFileName());
		}
	}
	
	public static void createFile() throws IOException {
		Path path = Paths.get("Z:/1.png");
		byte[] array_byte = Files.readAllBytes(path);
		char[] char_arr = new String(array_byte).toCharArray();
		int total_length = char_arr.length;
		
		BufferedWriter bw =  Files.newBufferedWriter(Paths.get("Z:/1"),Charset.forName("UTF-8"),StandardOpenOption.CREATE_NEW);
		bw.write(char_arr);		
		bw.close();
	}
	
	public static void createUnBufferedFile() throws IOException {
		OutputStream ous =  Files.newOutputStream(Paths.get("Z:/1.txt"), StandardOpenOption.CREATE);
		BufferedWriter bufWr = new BufferedWriter(new OutputStreamWriter(ous));
		bufWr.write("Hello");
		bufWr.close();
		ous.close();
	}
}
