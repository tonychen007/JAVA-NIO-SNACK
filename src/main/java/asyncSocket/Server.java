package asyncSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {

	public static void main(String[] args) {
		ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		try {
			AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			if (asynchronousServerSocketChannel.isOpen()) {
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				asynchronousServerSocketChannel.bind(new InetSocketAddress("0.0.0.0", 10086));

				while (true) {
					Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel
							.accept();

					try {
						AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();
						Callable<String> worker = new Callable<String>() {

							@Override
							public String call() throws Exception {
								String host = asynchronousSocketChannel.getRemoteAddress().toString();
								System.out.println("Incoming connection from: " + host);
								final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
								while (asynchronousSocketChannel.read(buffer).get() != -1) {
									buffer.flip();
									asynchronousSocketChannel.write(buffer).get();
									if (buffer.hasRemaining()) {
										buffer.compact();
									} else {
										buffer.clear();
									}
								}
								asynchronousSocketChannel.close();
								System.out.println(host + " was successfully served!");
								return host;
							}
						};
						taskExecutor.submit(worker);
					} catch (InterruptedException | ExecutionException e) {
						System.err.println(e);
						taskExecutor.shutdown();
						while (!taskExecutor.isTerminated()) {
							;
						}
						break;
					}
				}
			} else {
				System.out.println("The asynchronous server-socket channel cannot be opened!");
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
