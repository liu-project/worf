package orj.worf.zkconfig;

import java.util.List;

/**
 * ZookeeperClient公共接口
 * @author Liuzhenghua
 * 2017年11月19日 下午10:55:07
 */
public interface BaseZookeeperClient {

	/**
	 * 创建一个节点,值为null
	 * 如果父节点不存在，则会自动创建父节点
	 * @author Liuzhenghua
	 * 2017年11月19日 下午2:58:43
	 */
	void create(String path);
	
	/**
	 * 判断一个path是否存在
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:19:48
	 */
	boolean exists(String path);
	
	/**
	 * 删除一个节点
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:54:02
	 */
	boolean delete(String path, boolean recursive);
	
	/**
	 * 获取节点的子节点key
	 * @author Liuzhenghua
	 * 2017年11月19日 下午4:54:11
	 */
	List<String> getChildren(String path);
	
	/**
	 * 添加节点监听器
	 * @author Liuzhenghua
	 * 2017年11月19日 下午8:09:52
	 */
	void subscribeDataChanges(String path, ZookeeperDataListener listener);
	
	/**
	 * 子节点数据发生变化时，该监听器会触发，传入参数为：子节点全路径、新的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午9:06:42
	 */
	void subscribeChildrenDataChanges(String path, ZookeeperDataListener listener);
}
