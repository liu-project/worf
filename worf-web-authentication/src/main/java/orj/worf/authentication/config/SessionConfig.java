package orj.worf.authentication.config;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import orj.worf.authentication.configure.WebConfigure;
import orj.worf.zkconfig.CfgNodeZookeeperClient;
import orj.worf.zkconfig.ZookeeperClientFactory;
import orj.worf.zkconfig.annotation.ZkCfgNodeField;
import orj.worf.zkconfig.handler.CfgNodeDataChangeHandler;
import orj.worf.zkconfig.util.CfgNodeUtil;
import orj.worf.zkconfig.zkclient.ZkClientZookeeperClientFactory;

/**
 * session相关配置
 * @author LiuZhenghua
 * 2017年12月4日 下午2:02:05
 */
public class SessionConfig {
	
	@Autowired
	private ApplicationContext context;
	
	@ZkCfgNodeField("WEB端登陆超时时间（秒）")
	@Value("${web.login.timeout:1800}")
	private String webLoginTimeout;
	
	@ZkCfgNodeField("WAP端登陆超时时间（秒）")
	@Value("${wap.login.timeout:1800}")
	private String wapLoginTimeout;
	
	@ZkCfgNodeField("IOS端登陆超时时间（秒）")
	@Value("${ios.login.timeout:604800}")
	private String iosLoginTimeout;
	
	@ZkCfgNodeField("ANDROID端登陆超时时间（秒）")
	@Value("${android.login.timeout:604800}")
	private String androidLoginTimeout;
	
	@ZkCfgNodeField("微信端登陆超时时间（秒）")
	@Value("${wx.login.timeout:604800}")
	private String wxLoginTimeout;
	
	@ZkCfgNodeField("方法签名验证开关")
	@Value("${check.method.sign:1}")
	private String checkMethodSign;
	
	@ZkCfgNodeField("系统维护开关")
	@Value("${maintenance.status:0}")
	private String maintenanceStatus;
	
	@ZkCfgNodeField("系统维护时间（开始-结束）")
	@Value("${maintenance.time:2018-02-02 10:10:10 - 2018-05-02 10:10:10}")
	private String maintenanceTime;
	
	@ZkCfgNodeField("系统维护描述")
	@Value("${maintenance.desc:系统升级中}")
	private String maintenanceDesc;
	
	public String getMaintenanceStatus() {
		return maintenanceStatus;
	}
	public void setMaintenanceStatus(String maintenanceStatus) {
		this.maintenanceStatus = maintenanceStatus;
	}
	public String getMaintenanceTime() {
		return maintenanceTime;
	}
	public void setMaintenanceTime(String maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}
	public String getMaintenanceDesc() {
		return maintenanceDesc;
	}
	public void setMaintenanceDesc(String maintenanceDesc) {
		this.maintenanceDesc = maintenanceDesc;
	}
	public String getWebLoginTimeout() {
		return webLoginTimeout.trim();
	}
	public void setWebLoginTimeout(String webLoginTimeout) {
		this.webLoginTimeout = webLoginTimeout;
	}
	public String getWapLoginTimeout() {
		return wapLoginTimeout.trim();
	}
	public void setWapLoginTimeout(String wapLoginTimeout) {
		this.wapLoginTimeout = wapLoginTimeout;
	}
	public String getIosLoginTimeout() {
		return iosLoginTimeout.trim();
	}
	public void setIosLoginTimeout(String iosLoginTimeout) {
		this.iosLoginTimeout = iosLoginTimeout;
	}
	public String getAndroidLoginTimeout() {
		return androidLoginTimeout.trim();
	}
	public void setAndroidLoginTimeout(String androidLoginTimeout) {
		this.androidLoginTimeout = androidLoginTimeout;
	}
	public String getCheckMethodSign() {
		return checkMethodSign;
	}
	public void setCheckMethodSign(String checkMethodSign) {
		this.checkMethodSign = checkMethodSign;
	}
	
	@PostConstruct
	public void init() {
		WebConfigure configure = context.getBean(WebConfigure.class);
		ZookeeperClientFactory factory = new ZkClientZookeeperClientFactory();
		CfgNodeZookeeperClient cfgNodeZookeeperClient = factory.getInstanceForConfigUse(configure.getAuthProjectName(), configure.getZkHost());
		String cfgFileName = "sessionConfig";
		String cfgFilePath = "/" + cfgFileName;
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ("context".equals(field.getName())) {
				continue;
			}
			String fieldDesc = field.getName();
			ZkCfgNodeField zkCfgNodeFiled = field.getAnnotation(ZkCfgNodeField.class);
			if (zkCfgNodeFiled != null && !"defaultValue".equals(zkCfgNodeFiled.value())) {
				fieldDesc = zkCfgNodeFiled.value();
			}
			String fieldPath = cfgFilePath + "/" + field.getName();
			try {
				CfgNodeUtil.saveOrUploadData(cfgNodeZookeeperClient, this, field, fieldPath, fieldDesc);
			} catch (Exception e) {
				throw new BeanCreationException("Creating bean" + cfgFileName + " with class " + this.getClass() + " got an exception", e);
			}
		}
		cfgNodeZookeeperClient.subscribeChildrenDataChanges(cfgFilePath, new CfgNodeDataChangeHandler(context));
		
	}
	public String getWxLoginTimeout() {
		return wxLoginTimeout;
	}
	public void setWxLoginTimeout(String wxLoginTimeout) {
		this.wxLoginTimeout = wxLoginTimeout;
	}
}
