package orj.worf.rocketmq.bean;

/**
 * MQ要扫描的包
 * @author LiuZhenghua
 * 2016年12月9日 下午8:02:52
 */
public class MQScannerConfigure {
	
	private String basePackage;

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
}
