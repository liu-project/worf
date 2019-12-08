package orj.worf.zkconfig;

/**
 * 此ZookeeperClient只用于管理各个项目的配置, 操作的类型必须为{@link CfgNode}
 * @author Liuzhenghua
 * 2017年11月19日 下午10:47:38
 */
public interface CfgNodeZookeeperClient extends BaseZookeeperClient {

	/**
	 * 创建一个有值的节点
	 * 如果父节点不存在，不会自动创建
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:18:13
	 */
	void create(String path, CfgNode data);
	
	/**
	 * 修改节点的值,节点必须先存在
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:20:23
	 */
	void writeData(String path, CfgNode data);
	
	/**
	 * 读取节点的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:38:49
	 */
	CfgNode readData(String path);
	
	/**
	 * 读取节点的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:38:49
	 */
	CfgNode readData(String parentPath, String childPath);
	
}
