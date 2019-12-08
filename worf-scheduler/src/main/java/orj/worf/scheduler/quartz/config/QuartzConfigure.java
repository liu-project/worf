package orj.worf.scheduler.quartz.config;

import orj.worf.datasource.DynamicDataSource;

/**
 * 功能：修改quartz的一些配置
 * key必须是{@link DynamicDataSource#setTargetDataSources(java.util.Map) Map参数的key}
 * 在引入worf_scheduler的项目的xml中，使用:
 * <bean class="orj.worf.scheduler.quartz.config.QuartzConfigure">
 * 	<property name="dataSourceKey" value="specificDataSourceKey"/>
 * </bean>
 * @author LiuZhenghua
 * 2017年7月17日 上午10:04:41
 */
public class QuartzConfigure {
	
	//默认值null, 即用default data source
	private String dataSourceKey;

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSource) {
		this.dataSourceKey = dataSource;
	}
	
}
