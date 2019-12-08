package orj.worf.zkconfig.handler;

import org.springframework.context.ApplicationContext;

import orj.worf.zkconfig.CfgNode;
import orj.worf.zkconfig.ZookeeperDataListener;

/**
 * Concrete data change handler for cfg node that implementing {@link ZookeeperDataListener}
 * It is required that the type of node is {@link CfgNode}
 * @author LiuZhenghua
 * 2017年11月20日 下午2:39:56
 */
public class CfgNodeDataChangeHandler extends AbstractDataChangeHandler {
	
	private static ClassLoader classLoader;
	
	public static synchronized ClassLoader getClassLoader() {
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		return classLoader;
	}
	
	public CfgNodeDataChangeHandler(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	public void handleDataDeleted(String path) throws Exception {
		
	}

	@Override
	protected Object getNodeValue(Object data) {
		if (data instanceof CfgNode) {
			data = ((CfgNode) data).getValue();
		}
		return data;
	}

	@Override
	protected String getPropertyName(String path, Object data) {
		int index = path.lastIndexOf('/');
		if (index == -1) {
			throw new RuntimeException("path is not a legal cfg node");
		}
		return path.substring(index + 1);
	}

	@Override
	protected Object getCfgBean(String path, Object data, ApplicationContext applicationContext) throws Exception {
		CfgNode cfgNode = null;
		if (data instanceof CfgNode) {
			cfgNode = ((CfgNode) data);
		}
		if (cfgNode == null) {
			throw new RuntimeException("Node data requires to be CfgNode!");
		}
		return applicationContext.getBean(getClassLoader().loadClass(cfgNode.getBeanClassName()));
	}

}
