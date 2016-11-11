package zookeeperTest;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

@SuppressWarnings("all")
public class Test implements Watcher {

	private ZooKeeper zk;

	public static void main(String[] args) {

		Test m = new Test();
		try {
			m.startZK();
		} catch (IOException e) {

		}

		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {

		}
	}

	@Override
	public void process(WatchedEvent e) {
		System.out.println(e);
	}

	public void startZK() throws IOException {
		zk = new ZooKeeper("192.168.198.128:2181", 1500, this);
		try {
			runForMaster();
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	void runForMaster() throws KeeperException, InterruptedException {
		String nodeName = zk.create("/master", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Stat stat = new Stat();
		byte[] byst = zk.getData("/master", false, stat);
		String serverId = new String(byst);		
	}
}
