package socketChannelUDPTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) {
		try {
			DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
			if (datagramChannel.isOpen()) {
				datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				datagramChannel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);
				datagramChannel.bind(new InetSocketAddress("192.168.2.101", 10086));
				System.out.println(datagramChannel.getLocalAddress());
				final int MAX_PACKET_SIZE = 4096;
				ByteBuffer echoText = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
				while (true) {
					SocketAddress clientAddress = datagramChannel.receive(echoText);
					echoText.flip();
					datagramChannel.send(echoText, clientAddress);
					echoText.clear();
				}
			}
		} catch (IOException ex) {
			if (ex instanceof ClosedChannelException) {
				System.err.println("The channel was unexpected closed ...");
			}		
			if (ex instanceof IOException) {
				System.err.println("An I/O error occured ...");
			}
			System.err.println("\n" + ex);
		}
	}
}
