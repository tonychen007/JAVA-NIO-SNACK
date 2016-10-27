package MetadataAttribTest;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public class Test {	
	public static void main(String[] args) throws IOException {		
		FileSystem fs = FileSystems.getDefault();
		Set<String> views = fs.supportedFileAttributeViews();
		
		Iterator<String> itr = views.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
		
		for (FileStore fileStore : fs.getFileStores()) {
			boolean ret = fileStore.supportsFileAttributeView(BasicFileAttributeView.class);
		}
		
		Path path = Paths.get("Z:/1.txt");
		DosFileAttributes attrib = Files.readAttributes(path, DosFileAttributes.class);		
		UserPrincipal prin =  Files.getOwner(path);
		String owner = prin.getName();
		
		UserPrincipalLookupService prinLookup = path.getFileSystem().getUserPrincipalLookupService();
		prin = prinLookup.lookupPrincipalByName("Administrator");
		
		FileOwnerAttributeView fileAttrib = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		prin = (UserPrincipal)Files.getAttribute(path, "owner:owner");
		
		AclFileAttributeView acl = Files.getFileAttributeView(path, AclFileAttributeView.class);
		List<AclEntry> aclList = acl.getAcl();
	}
}
