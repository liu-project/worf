package orj.worf.zkconfig.zkclient;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import orj.worf.zkconfig.BaseZookeeperClient;
import orj.worf.zkconfig.ZookeeperDataListener;

/**
 * 利用ZkClient实现zookeeperClient
 * @author Liuzhenghua
 * 2017年11月19日 下午2:50:00
 */
public class AbstarctZkClientZookeeperClient implements BaseZookeeperClient {
	
	private ZkClient zkClient;
	
	AbstarctZkClientZookeeperClient(String url) {
		super();
		this.zkClient = new ZkClient(url);
	}

	public void create(String path) {
		zkClient.createPersistent(path, true);
	}

	public void create(String path, Object zookeeperNode) {
		zkClient.createPersistent(path, zookeeperNode);
	}

	public boolean exists(String path) {
		return zkClient.exists(path);
	}
	
	public void writeData(String path, Object zookeeperNode) {
		zkClient.writeData(path, zookeeperNode);
	}
	
	public Object readData(String path) {
		return zkClient.readData(path);
	}
	
	public Object readData(String parentPath, String childPath) {
		return zkClient.readData(combinePath(parentPath, childPath));
	}
	
	public boolean delete(String path, boolean recursive) {
		if (recursive) {
			return zkClient.deleteRecursive(path);
		} else {
			return zkClient.delete(path);
		}
	}
	
	public List<String> getChildren(String path) {
		path = removeForwardSplash(path);
		return zkClient.getChildren(path);
	}
	
	public void subscribeDataChanges(String path, ZookeeperDataListener listener) {
		path = removeForwardSplash(path);
		zkClient.subscribeDataChanges(path, new IZkDataListenerAdapter(listener));
	}
	
	private static String removeForwardSplash(String path) {
		if (path.charAt(path.length() - 1) == '/' && path.length() > 1) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	private static String combinePath(String parentPath, String childPath) {
		if (parentPath.charAt(parentPath.length() - 1) == '/') {
			return parentPath + childPath;
		}
		return parentPath + "/" + childPath;
	}

	public void subscribeChildrenDataChanges(String parentPath, ZookeeperDataListener listener) {
		List<String> children = getChildren(parentPath);
		for (String childPath : children) {
			subscribeDataChanges(combinePath(parentPath, childPath), listener);
		}
	}
	
}
