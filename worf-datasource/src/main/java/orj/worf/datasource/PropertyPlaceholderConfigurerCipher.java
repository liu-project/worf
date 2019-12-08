package orj.worf.datasource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerCipher  extends PropertyPlaceholderConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerCipher.class);
	private static List<String> keys = new ArrayList<String>(Arrays.asList(
			"jdbc.driverclass",
			"jdbc1.driverclass", 
			
			"jdbc.url",
			"jdbc1.url",
			
			"jdbc.username",
			"jdbc1.username",
			
			"jdbc.password",
			"jdbc1.password",
			
			"jdbc.pool.size.max",
			"jdbc1.pool.size.max"
			));
	
	private static String encrypt_key_prefixs = "encrypt.";
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		try{
			
			for (String key : keys) {
				if(props.containsKey(key)){
					String value = props.getProperty(key);
					String decryptData = RJSCipher.decryptData(value);
					props.setProperty(key, decryptData);
				}
			}
			//需要放置重复加密，不要同时匹配上两种规则
			for(Object key : props.keySet()){
				String strKey = (String)key;
				
	
					if(strKey.startsWith(encrypt_key_prefixs)){
						String value = props.getProperty(strKey);
						String decryptData = RJSCipher.decryptData(value);
						props.setProperty(strKey, decryptData);
						
					}
				
			}
		} catch (Exception e) {
			logger.error("无法解密数据库文件。数据库文件必须要加密存放!",e);
			throw new FatalBeanException("无法解密数据库文件。数据库文件必须要加密存放!");
		}
		super.processProperties(beanFactoryToProcess, props);
	}
	
	
}
