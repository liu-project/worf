package orj.worf.zkconfig.handler;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import orj.worf.zkconfig.ZookeeperDataListener;

/**
 * ZookeeperDataListener Handler could extends this class.
 * It aims at easyly implementing {@link ZookeeperDataListener}
 * @author LiuZhenghua
 * 2017年11月20日 下午2:38:19
 */
public abstract class AbstractDataChangeHandler implements ZookeeperDataListener {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private ApplicationContext applicationContext;
	
	public AbstractDataChangeHandler(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	public void handleDataChange(String path, Object data) throws Exception {
		logger.info("-------->start change config data, path:{}, new value:{}", path, data);
		Object bean = getCfgBean(path, data, applicationContext);
		String propertyName = getPropertyName(path, data);
		Object nodeValue = getNodeValue(data);
		
		Field field = bean.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		field.set(bean, nodeValue);
		logger.info("-------->config data changed, path:{}, new value:{}", path, data);
	}
	
	protected abstract Object getNodeValue(Object data);

	protected abstract Object getCfgBean(String path, Object data, ApplicationContext applicationContext) throws Exception;

	protected abstract String getPropertyName(String path, Object data);

	
}
