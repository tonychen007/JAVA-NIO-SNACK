package bufferTest;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;


class Foo<T extends Foo<T>> {
	T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}

class Color extends Foo<Color> {
	
}

@SuppressWarnings("all")
public class BufferChannelTest {

	public static void main(String[] args) {
		Color cl = new Color();
		String sb = cl.getData().getClass().getName();
		
		Path file = Paths.get("Z:/1.tmp");
		
		// SeekableChannel can be cast into FileChannel		
		try (FileChannel seekableByteChannel = FileChannel.open(file,
				EnumSet.of(StandardOpenOption.READ))) {
			ByteBuffer buffer = ByteBuffer.allocate(12);
			MappedByteBuffer mapBuffer = null;
			String encoding = System.getProperty("file.encoding");
			buffer.clear();
			
			mapBuffer = seekableByteChannel.map(MapMode.READ_ONLY, 0, seekableByteChannel.size());
			readBuffer(mapBuffer);			
//			while (seekableByteChannel.read(buffer) > 0) {
//				buffer.flip();
//				System.out.print(Charset.forName(encoding).decode(buffer));
//				buffer.clear();
//			}		
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void readBuffer(ByteBuffer buf) throws CharacterCodingException {
		if (buf != null) {
			String encoding = System.getProperty("file.encoding");
			CharBuffer charBuf = Charset.forName(encoding).decode(buf);			
			System.out.println(charBuf);
			int aaa = 0;
		}
	}
}



