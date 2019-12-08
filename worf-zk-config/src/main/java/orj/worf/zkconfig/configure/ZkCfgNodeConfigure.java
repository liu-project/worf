package orj.worf.zkconfig.configure;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import orj.worf.zkconfig.CfgNodeZookeeperClient;
import orj.worf.zkconfig.ZookeeperClientFactory;
import orj.worf.zkconfig.annotation.ZkCfgNode;
import orj.worf.zkconfig.annotation.ZkCfgNodeField;
import orj.worf.zkconfig.handler.CfgNodeDataChangeHandler;
import orj.worf.zkconfig.util.CfgNodeUtil;
import orj.worf.zkconfig.zkclient.ZkClientZookeeperClientFactory;

/**
 * 扫描{@link ZkCfgNode},并初始化
 * <bean class="com.rjs.zookeeper.configure.ZkCfgNodeConfigure">
    	<property name="projectName" value="checking"/>
    	<property name="connectionURL" value="${zookeeper.host}"/>
    </bean>
 * @author Liuzhenghua
 * 2017年11月22日 下午10:10:09
 */
public class ZkCfgNodeConfigure implements BeanPostProcessor {
	
	@Autowired
	private ApplicationContext context;
	//项目名，必须指定
	private String projectName;
	//Zookeeper 连接地址， 多个地址用逗号隔开：ip:port,ip:port,ip:port
	private String connectionURL;
	
	private ZookeeperClientFactory factory = new ZkClientZookeeperClientFactory();

	/**
	 * Bean实例化之前
	 * @author Liuzhenghua
	 * 2017年11月22日 下午10:14:22
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		initZkCfgNode(bean, beanName);
		return bean;
	}

	/**
	 * Bean实例化之后
	 * @author Liuzhenghua
	 * 2017年11月22日 下午10:14:33
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	/**
	 * 初始化Zk配置节点
	 * @author Liuzhenghua
	 * 2017年11月22日 下午10:18:12
	 */
	private void initZkCfgNode(Object bean, String beanName) {
		String classProjectName = projectName;
		ZkCfgNode zkCfgNode = bean.getClass().getAnnotation(ZkCfgNode.class);
		if (zkCfgNode == null) {
			return;
		}
		if (!"defaultValue".equals(zkCfgNode.projectName())) {
			classProjectName = zkCfgNode.projectName();
		}
		if (StringUtils.isEmpty(classProjectName)) {
			throw new BeanCreationException("The zk project name in " + getClass() + "is invalid, can't be null");
		}
		CfgNodeZookeeperClient cfgNodeZookeeperClient = factory.getInstanceForConfigUse(classProjectName, getConnectionURL());
		String cfgFileName = beanName;
		if (!"defaultValue".equals(zkCfgNode.value())) {
			cfgFileName = zkCfgNode.value();
		}
		String cfgFilePath = "/" + cfgFileName;
		Field[] fields = bean.getClass().getDeclaredFields();
		if (fields.length == 0) {
			return;
		}
		for (Field field : fields) {
			String fieldDesc = field.getName();
			ZkCfgNodeField zkCfgNodeFiled = field.getAnnotation(ZkCfgNodeField.class);
			if (zkCfgNodeFiled != null && !"defaultValue".equals(zkCfgNodeFiled.value())) {
				fieldDesc = zkCfgNodeFiled.value();
			}
			
			String fieldPath = cfgFilePath + "/" + field.getName();
			try {
				CfgNodeUtil.saveOrUploadData(cfgNodeZookeeperClient, bean, field, fieldPath, fieldDesc);
			} catch (Exception e) {
				throw new BeanCreationException("Creating bean" + beanName + " with class " + bean.getClass() + " got an exception", e);
			}
		}
		cfgNodeZookeeperClient.subscribeChildrenDataChanges(cfgFilePath, new CfgNodeDataChangeHandler(context));
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

}
