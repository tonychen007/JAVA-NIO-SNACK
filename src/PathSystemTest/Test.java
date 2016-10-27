package PathSystemTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Test {
	private static final String ZIP_SCHEME = "jar:file"; 
	
	public static void main(String[] args) {
		FileSystem fsys =  FileSystems.getDefault();
		try {
			// For Windows file
			FileSystem zsys =  FileSystems.getFileSystem(new URI("file:///"));			
			Path zsysPath = zsys.getPath("Z:/1.txt");
			readFile(zsysPath.toFile());
			
			// For Jar file
			URI jarURI = Paths.get("Z:/dt.jar").toFile().toURI();
			URI uri = new URI("jar:file", jarURI.getHost(), jarURI.getPath(), jarURI.getFragment());
			FileSystem fs = FileSystems.newFileSystem(uri,new HashMap<String, String>());
			Path p = fs.getPath("/META-INF/MANIFEST.MF");
			readJarFile(p);
			int aaa = 00;
		} catch (URISyntaxException | IOException e) {
			int aaaa = 0;
		}		
	}
	
	private static void readFile(File file) throws IOException {
		FileReader fileReader = new FileReader(file);		
		BufferedReader bufReader = new BufferedReader(fileReader);
		
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			System.out.println(line);
		}
		
		bufReader.close();
	}
	
	private static void readJarFile(Path path) throws IOException {
		byte[] contents = Files.readAllBytes(path);
		String sb = new String(contents,"UTF-8");
		System.out.println(sb);
	}
	
}
