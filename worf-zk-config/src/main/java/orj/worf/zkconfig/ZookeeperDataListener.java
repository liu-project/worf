package orj.worf.zkconfig;

import orj.worf.zkconfig.handler.AbstractDataChangeHandler;

/**
 * zk节点数据改变的监听者
 * It's highly recommended to extends {@link AbstractDataChangeHandler}
 * @author Liuzhenghua
 * 2017年11月19日 下午11:12:19
 */
public interface ZookeeperDataListener {

	/**
	 * @param path 发生数据更新的节点的全路径
	 * @param data 节点更新后的值
	 * @author Liuzhenghua
	 * 2017年11月19日 下午11:15:10
	 */
	public abstract void handleDataChange(String path, Object data) throws Exception;

	/**
	 * @param path 被删除的节点的全路径
	 * @author Liuzhenghua
	 * 2017年11月19日 下午11:15:52
	 */
	public abstract void handleDataDeleted(String path) throws Exception;
}
