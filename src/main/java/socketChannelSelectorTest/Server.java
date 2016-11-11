package socketChannelSelectorTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("all")
public class Server {

	private ByteBuffer buf = ByteBuffer.allocateDirect(1024);

	public static void main(String[] args) {
		Server srv = new Server();
		try {
			Selector selector = Selector.open();
			ServerSocketChannel servCh = ServerSocketChannel.open();
			SelectableChannel selCh = servCh.configureBlocking(false);
			srv.dumpSockOpts(servCh);
			srv.setSockOpts(servCh);
			servCh.bind(new InetSocketAddress(10086), 5);

			System.out.println("Bind address:" + servCh.getLocalAddress());

			servCh.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				int s = selector.select();
				srv.processSelKeys(selector);
			}

		} catch (IOException e) {
			String err = e.getCause().getMessage();
			System.out.println(err);
		}
	}

	public void dumpSockOpts(NetworkChannel channel) {
		Set<SocketOption<?>> opts = channel.supportedOptions();
		for (SocketOption<?> sk : opts) {
			String name = sk.name();
			Class<?> clazz = sk.type();
			String claszzName = clazz.getName();
			int debugger = 3;
			System.out.println("Socket opts:" + name);
		}
	}

	public void setSockOpts(NetworkChannel channel) {
		try {
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		} catch (IOException e) {
			String err = e.getCause().getMessage();
			System.out.println(err);
		}
	}

	public void processSelKeys(Selector selector) {
		Iterator keys = selector.selectedKeys().iterator();
		while (keys.hasNext()) {
			SelectionKey key = (SelectionKey) keys.next();
			keys.remove();
			if (!key.isValid()) {
				continue;
			}

			if (key.isAcceptable()) {
				acceptOP(key, selector);
			} else if (key.isReadable()) {
				this.readOP(key);
			} else if (key.isWritable()) {
				this.writeOP(key);
			}
		}
	}

	private void writeOP(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			buf.flip();
			socketChannel.write(buf);
			if (buf.hasRemaining()) {
				buf.compact();
			} else {
				buf.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		key.interestOps(SelectionKey.OP_READ);
	}

	private void readOP(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		buf.clear();
		int numRead = -1;

		try {
			numRead = socketChannel.read(buf);		
		} catch (IOException e) {
			System.err.println(e);
		}

		if (numRead == -1) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				System.err.println(e);	
			}
			key.cancel();
			return;
		}
		key.interestOps(SelectionKey.OP_WRITE);
	}

	private void acceptOP(SelectionKey key, Selector selector) {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel;
		try {
			socketChannel = serverChannel.accept();
			socketChannel.configureBlocking(false);
			System.out.println("Incoming connection from: " + socketChannel.getRemoteAddress());
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
