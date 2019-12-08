package orj.worf.zkconfig.zkclient;

import org.I0Itec.zkclient.IZkDataListener;

import orj.worf.zkconfig.ZookeeperDataListener;

/**
 * adapter for {@link ZookeeperDataListener} to {@link IZkDataListener}
 * @author Liuzhenghua
 * 2017年11月19日 下午11:20:53
 */
public class IZkDataListenerAdapter implements IZkDataListener {
	
	private ZookeeperDataListener zookeeperDataListener;

	IZkDataListenerAdapter(ZookeeperDataListener zookeeperDataListener) {
		super();
		this.zookeeperDataListener = zookeeperDataListener;
	}

	public void handleDataChange(String path, Object data) throws Exception {
		zookeeperDataListener.handleDataChange(path, data);
	}

	public void handleDataDeleted(String path) throws Exception {
		zookeeperDataListener.handleDataDeleted(path);
	}

}
