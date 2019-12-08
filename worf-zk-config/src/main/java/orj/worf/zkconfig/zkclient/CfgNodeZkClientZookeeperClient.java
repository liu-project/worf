package orj.worf.zkconfig.zkclient;

import orj.worf.zkconfig.CfgNode;
import orj.worf.zkconfig.CfgNodeZookeeperClient;

/**
 * 利用ZkClient作为zookeeper客户端（专用于管理项目配置）
 * @author LiuZhenghua
 * 2017年11月21日 上午10:25:28
 */
public class CfgNodeZkClientZookeeperClient extends AbstarctZkClientZookeeperClient implements CfgNodeZookeeperClient {

	CfgNodeZkClientZookeeperClient(String url) {
		super(url);
	}

	public void create(String path, CfgNode data) {
		super.create(path, data);
	}

	/**
	 * 配置文件会对应java配置类，类型已经写死，如果改之后的类型与之前不匹配，会导致类型转换异常
	 * @param path
	 * @param data
	 * @author LiuZhenghua 2017年11月21日 上午10:46:10
	 */
	public void writeData(String path, CfgNode data) {
		CfgNode oldData = readData(path);
		if (oldData == null || oldData.getValue() == null || oldData.getValue().getClass().equals(data.getValue().getClass())) {
			super.writeData(path, data);
		} else {
			throw new RuntimeException("Can't change object type from " + data.getValue().getClass() + " to " + oldData.getValue().getClass());
		}
	}

	public CfgNode readData(String path) {
		return (CfgNode)super.readData(path);
	}

	public CfgNode readData(String parentPath, String childPath) {
		return (CfgNode)super.readData(parentPath, childPath);
	}

}
