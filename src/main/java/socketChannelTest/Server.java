package socketChannelTest;

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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class Server {
	public static void main(String[] args) {
		Server srv = new Server();
		try {
			ServerSocketChannel servCh = ServerSocketChannel.open();
			SelectableChannel selCh = servCh.configureBlocking(true);
			srv.dumpSockOpts(servCh);
			srv.setSockOpts(servCh);

			servCh.bind(new InetSocketAddress(10086), 5);
			System.out.println("Bind address:" + servCh.getLocalAddress());
			
			SocketChannel socketChannel = servCh.accept();
			System.out.println("Incoming connection from: " + socketChannel.getRemoteAddress());
			
			// Use telnet to connect to the port 10086
			ByteBuffer buf = ByteBuffer.allocateDirect(10);
			InputStream ins = socketChannel.socket().getInputStream();
			OutputStream ons =  socketChannel.socket().getOutputStream();
			ReadableByteChannel readCh = Channels.newChannel(ins);  
			WritableByteChannel writeCh = Channels.newChannel(ons);
			
			/* ByteBuffer Methods
			while(socketChannel.read(buf) != -1) {
				buf.flip();
				socketChannel.write(buf);
				
				if(buf.hasRemaining()) {
					buf.compact();
				} else {
					buf.clear();
				}
			}
			*/
			
			while (readCh.read(buf) != -1) {
				buf.flip();
				writeCh.write(buf);
				if(buf.hasRemaining()) {
					buf.compact();
				} else {
					buf.clear();
				}	
			}
			
			socketChannel.shutdownInput();
			socketChannel.shutdownOutput();			
	
			socketChannel.close();
			servCh.close();
			
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
			channel.setOption(StandardSocketOptions.SO_RCVBUF,4 * 1024);
			channel.setOption(StandardSocketOptions.SO_REUSEADDR,true);
		} catch (IOException e) {
			String err = e.getCause().getMessage();
			System.out.println(err);
		}
	}
}
