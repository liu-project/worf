package orj.worf.zkconfig.zkclient;

import orj.worf.zkconfig.ZookeeperClient;

/**
 * 利用zkClient包作为Zokeeper客户端
 * @author LiuZhenghua
 * 2017年11月21日 上午10:25:08
 */
public class ZkClientZookeeperClient extends AbstarctZkClientZookeeperClient implements ZookeeperClient {

	ZkClientZookeeperClient(String url) {
		super(url);
	}

}
