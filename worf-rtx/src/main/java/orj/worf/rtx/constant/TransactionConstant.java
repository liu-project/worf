package orj.worf.rtx.constant;

import org.apache.commons.lang3.StringUtils;

public class TransactionConstant {

	/**
	 * 默认事务超时时间 -- 60s
	 */
	public static final int TRANSACTION_TIME_OUT = 60;
	
	/**
	 * 默认重试次数 3次
	 */
	public static final int TRANSACTION_RETRY_TIMES = 3;
	
	
	private static final String SERVER_ENV = "server.home";
	
	//主要用于区分不同服务
	public static String SERVER_NAME = "";
	
	static {
		SERVER_NAME = System.getProperty(SERVER_ENV);
		if(StringUtils.isBlank(SERVER_NAME)) {
			SERVER_NAME = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		}
	}
}
