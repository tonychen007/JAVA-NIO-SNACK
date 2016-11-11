package socketChannelSelectorTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("all")
public class Client {
	public static void main(String[] args) {
		final int DEFAULT_PORT = 10086;
		final String IP = "127.0.0.1";
		ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
		Charset charset = Charset.defaultCharset();
		CharsetDecoder decoder = charset.newDecoder();

		try (Selector selector = Selector.open(); SocketChannel socketChannel = SocketChannel.open()) {
			if ((socketChannel.isOpen()) && (selector.isOpen())) {
				socketChannel.configureBlocking(false);

				socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
				socketChannel.connect(new java.net.InetSocketAddress(IP, DEFAULT_PORT));
				System.out.println("Localhost: " + socketChannel.getLocalAddress());

				boolean writed = false;
				boolean down = false;

				while (!down && selector.select(1000) > 0) {
					Set keys = selector.selectedKeys();
					Iterator its = keys.iterator();
					while (its.hasNext()) {
						SelectionKey key = (SelectionKey) its.next();
						its.remove();

						SocketChannel keySocketChannel = (SocketChannel) key.channel();
						if (key.isConnectable()) {
							System.out.println("I am connected!");
							if (keySocketChannel.isConnectionPending()) {
								keySocketChannel.finishConnect();
							}
							keySocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						}

						if (key.isWritable() && !writed) {
							System.out.println("Input(bye to end): ");
							BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
							String s = br.readLine();
							if (s != null && !s.trim().equals("")) {
								buffer.clear();
								buffer.put(s.getBytes());
								buffer.flip();
								keySocketChannel.write(buffer);
								writed = true;
								if (s.equals("bye")) {
									down = true;
									break;
								}
							}
						}

						if (key.isReadable()) {
							buffer.clear();
							keySocketChannel.read(buffer);
							buffer.flip();
							byte[] b = new byte[buffer.limit()];
							buffer.get(b);
							System.out.println((new String(b)));
							writed = false;
						}
					}

				} // end of while
				socketChannel.close();
			} else {
				System.out.println("The socket channel or selector cannot be opened!");
			}
		} catch (Exception e) {
			System.err.println(e);
		}		
	}
}
