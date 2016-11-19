package asyncSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class ServerCompletionHandler {

	public static void main(String[] args) {
		try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
			if (asynchronousServerSocketChannel.isOpen()) {
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				asynchronousServerSocketChannel.bind(new InetSocketAddress("0.0.0.0", 10086));
				System.out.println("Waiting for connections ...");
				asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
					
					final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
					
					@Override
					public void completed(AsynchronousSocketChannel result, Void attachment) {
						asynchronousServerSocketChannel.accept(null, this);
						try {
							System.out.println("Incoming connection from: " + result.getRemoteAddress());
							
							while (result.read(buffer).get() != -1) {
								buffer.flip();
								result.write(buffer).get();
								if (buffer.hasRemaining()) {
									buffer.compact();
								} else {
									buffer.clear();
								}
							}
						} catch (IOException | InterruptedException | ExecutionException ex) {
							System.err.println(ex);
						} finally {
							try {								
								result.close();
							} catch (IOException e) {
								System.err.println(e);
							}
						}
					}

					@Override
					public void failed(Throwable exc, Void attachment) {
						asynchronousServerSocketChannel.accept(null, this);
						throw new UnsupportedOperationException("Cannot accept connections!");
					}
				});
				Thread.currentThread().join();
			} else {
				System.out.println("The asynchronous server-socket channel cannot be opened!");
			}
		} catch (IOException | InterruptedException e) {

		}
	}
}
