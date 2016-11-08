package bufferTest;

import java.nio.ByteBuffer;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) {
		byte[] b = new byte[6];
		byte[] b1 = new byte[6];
		byte[] b2 = new byte[7];
		b[0] = 1;b[1] = 2;b[2] = 3;b[3] = 4;b[4] = 5;
		ByteBuffer buf = ByteBuffer.allocateDirect(15);
		buf.put(b);  // pos 5, limit 15, cap 15
		buf.flip();  // pos 0, limit 5, cap 15
		buf.get(b1);
		
		b[5] = 6;
		buf.clear();
		buf.put(b2);
	}
}
