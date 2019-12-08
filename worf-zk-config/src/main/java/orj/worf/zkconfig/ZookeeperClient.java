package orj.worf.zkconfig;

/**
 * zookeeper client interface
 * @author Liuzhenghua
 * 2017年11月19日 下午2:42:29
 */
public interface ZookeeperClient extends BaseZookeeperClient {
	
	/**
	 * 创建一个有值的节点
	 * 如果父节点不存在，不会自动创建
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:18:13
	 */
	void create(String path, Object data);
	
	/**
	 * 修改节点的值,节点必须先存在
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:20:23
	 */
	void writeData(String path, Object data);
	
	/**
	 * 读取节点的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:38:49
	 */
	Object readData(String path);
	
	/**
	 * 读取节点的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:38:49
	 */
	Object readData(String parentPath, String childPath);
	
}
