package asyncTest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class Test {

	public static void main(String[] args) {
		ExecutorService taskExecutor = Executors.newFixedThreadPool(5);
		Set<StandardOpenOption> fileOpt = new TreeSet<>();
		fileOpt.add(StandardOpenOption.READ);
		try {
			AsynchronousFileChannel aFileChn = AsynchronousFileChannel.open(Paths.get("Z:/1.txt"), fileOpt,
					taskExecutor);
			List<Future<ByteBuffer>> futureList = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				Callable<ByteBuffer> cal = new Callable<ByteBuffer>() {

					@Override
					public ByteBuffer call() throws Exception {
						ByteBuffer buffer = ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
						aFileChn.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));
						return buffer;
					}
				};
				Future<ByteBuffer> future = taskExecutor.submit(cal);
				futureList.add(future);
			}

			taskExecutor.shutdown();
			while (!taskExecutor.isTerminated()) {
				;
			}

			for (Future<ByteBuffer> future : futureList) {
				ByteBuffer buffer = future.get();
				System.out.println("\n\n" + buffer);
				System.out.println("______________________________________________________");
				buffer.flip();
				System.out.print(Charset.forName("utf-8").decode(buffer));
				buffer.clear();
			}
		} catch (IOException | InterruptedException | ExecutionException e) {

		}
	}
}