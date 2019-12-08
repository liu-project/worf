package orj.worf.scheduler.quartz.config;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import orj.worf.datasource.DynamicDataSource;
import orj.worf.datasource.DynamicDataSourceHolder;

/**
 * quartz配置
 * bean在xml文件中声明
 * @author LiuZhenghua
 * 2017年7月18日 上午9:24:30
 */
public class QuartzConfigFactory {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired(required = false)
	private QuartzConfigure dataSourceConfigure;
	@Autowired
	private DynamicDataSource dynamicDataSource;
	
	/**
	 * 获取quartz的DataSource
	 * @author LiuZhenghua
	 * 2017年7月18日 上午9:32:21
	 */
	public DataSource getDataSource() {
		DataSource retDataSource = null;
		DynamicDataSourceHolder.setDataSource(getDataSourceKey());
		try {
			Method method = AbstractRoutingDataSource.class.getDeclaredMethod("determineTargetDataSource");
			method.setAccessible(true);
			retDataSource = (DataSource)method.invoke(dynamicDataSource);
		} catch (Exception e) {
			logger.error("获取quartz数据源失败", e);
		}
		DynamicDataSourceHolder.clearDataSource();
		return retDataSource;
	}
	
	public String getDataSourceKey() {
		if (dataSourceConfigure != null) {
			return dataSourceConfigure.getDataSourceKey();
		}
		return null;
	}
}
