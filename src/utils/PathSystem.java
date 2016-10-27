package utils;

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

@SuppressWarnings("unused")
public class PathSystem {
	private String filePath;
	private String jarPath;

	public PathSystem(String filePath, String jarPath) {
		super();
		this.filePath = filePath;
		this.jarPath = jarPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getJarPath() {
		return jarPath;
	}

	
	public void windowsFileSystem() throws IOException, URISyntaxException {
		if (filePath == null || "".equals(filePath)) {
			return;
		}
			
		FileSystem zsys =  FileSystems.getFileSystem(new URI("file:///"));			
		Path zsysPath = zsys.getPath(filePath);
		readFile(zsysPath.toFile());
	}
	
	public void jarFileSystem() throws IOException, URISyntaxException {
		if (jarPath == null || "".equals(jarPath)) {
			return;
		}
		
		URI jarURI = Paths.get(jarPath).toFile().toURI();
		URI uri = new URI("jar:file", jarURI.getHost(), jarURI.getPath(), jarURI.getFragment());
		FileSystem fs = FileSystems.newFileSystem(uri,new HashMap<String, String>());
		Path p = fs.getPath("/META-INF/MANIFEST.MF");
		readJarFile(p);		
	}
	
	
	public void normalizePath() {		
		Path normalPath = Paths.get("Z:/TEMP/Default/Cache/../Media Cache").normalize();
		Path abnormalPath = Paths.get("Z:/TEMP/Default/Cache/../");
		Path pathEle = Paths.get("Z","TEMP","Default");
		Path s1 = pathEle.getName(1);
		Path s2 = pathEle.subpath(0, 2);	
	}
	
	private void readFile(File file) throws IOException {
		FileReader fileReader = new FileReader(file);		
		BufferedReader bufReader = new BufferedReader(fileReader);
		
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			System.out.println(line);
		}
		
		bufReader.close();
	}
	
	private void readJarFile(Path path) throws IOException {
		byte[] contents = Files.readAllBytes(path);
		String sb = new String(contents,"UTF-8");
		System.out.println(sb);
	}
}
