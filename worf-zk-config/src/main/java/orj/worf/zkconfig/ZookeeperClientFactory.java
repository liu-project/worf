package orj.worf.zkconfig;

/**
 * 获取ZookeeperClient的工厂类
 * @author Liuzhenghua
 * 2017年11月19日 下午10:45:49
 */
public interface ZookeeperClientFactory {
	
	/**
	 * 获取zkClient
	 * @author Liuzhenghua
	 * 2017年11月19日 下午9:48:08
	 */
	public ZookeeperClient getInstance(String conenctionURL);
	
	/**
	 * 获取用户维护配置的zkClient，所有路径都会自动以/config/projectName/开始
	 * @author Liuzhenghua
	 * 2017年11月19日 下午9:47:02
	 */
	public CfgNodeZookeeperClient getInstanceForConfigUse(String projectName, String connectionURL);
}
