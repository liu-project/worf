package orj.worf.zkconfig.zkclient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import orj.worf.zkconfig.CfgNodeZookeeperClient;
import orj.worf.zkconfig.ZookeeperClient;
import orj.worf.zkconfig.ZookeeperClientFactory;

/**
 * 利用zkClient作为Zookeeper的客户端  的工厂类
 * @author LiuZhenghua
 * 2017年11月22日 上午11:39:17
 */
public class ZkClientZookeeperClientFactory implements ZookeeperClientFactory {
	
	private static ZookeeperClient zookeeperClient = null;
	private static Map<String, CfgNodeZookeeperClient> cfgNodeZookeeperClientMap = new HashMap<String, CfgNodeZookeeperClient>();
	private static Lock zkClientlock = new ReentrantLock();
	private static Lock cfgNodZkClientlock = new ReentrantLock();

	public synchronized ZookeeperClient getInstance(String conenctionURL) {
		try {
			zkClientlock.lock();
			if (zookeeperClient == null) {
				zookeeperClient = new ZkClientZookeeperClient(conenctionURL);
			}
			return zookeeperClient;
		} finally {
			zkClientlock.unlock();
		}
	}

	/**
	 * url后面加路径（chroot）在zookeeper3.2版本开始才支持
	 * @author Liuzhenghua
	 * 2017年11月19日 下午10:38:41
	 */
	public synchronized CfgNodeZookeeperClient getInstanceForConfigUse(String projectName, String connectionURL) {
		try {
			cfgNodZkClientlock.lock();
			CfgNodeZookeeperClient cfgNodeZookeeperClient = cfgNodeZookeeperClientMap.get(projectName);
			if (cfgNodeZookeeperClient == null) {
				String oldConnectionURL = connectionURL;
				connectionURL = connectionURL + "/config/" + projectName;
				cfgNodeZookeeperClient = new CfgNodeZkClientZookeeperClient(connectionURL);
				if (!cfgNodeZookeeperClient.exists("/")) {
					ZookeeperClient client = getInstance(oldConnectionURL);
					client.create("/config/" + projectName);
				}
				cfgNodeZookeeperClientMap.put(projectName, cfgNodeZookeeperClient);
			}
			return cfgNodeZookeeperClient;	
		} finally {
			cfgNodZkClientlock.unlock();
		}
	}
}
