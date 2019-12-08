package orj.worf.scheduler.quartz.job;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import orj.worf.core.util.ApplicationContextHolder;
import orj.worf.datasource.DynamicDataSourceHolder;
import orj.worf.scheduler.quartz.config.QuartzConfigFactory;
import orj.worf.scheduler.quartz.exception.ScheduleExceptionHandle;
import orj.worf.scheduler.quartz.mapper.WorfJobDAO;
import orj.worf.scheduler.quartz.model.WorfJob;

/**
 * 任务调度基类，实现Job接口
 * 该类必须是线程安全的
 * @author linruzhou
 */
@DisallowConcurrentExecution
public class BaseWorfJob implements Job {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WorfJobDAO worfJobDAO;
	@Autowired
    private QuartzConfigFactory quartzConfigFactory;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DynamicDataSourceHolder.setDataSource(quartzConfigFactory.getDataSourceKey());
		JobKey jobKey = context.getJobDetail().getKey();
		WorfJob worfJob = worfJobDAO.queryWorfJobByJobName(jobKey.getName());
		if(worfJob == null) {
			logger.info("job do not exists, group={}, name={}", jobKey.getGroup(), jobKey.getName());
			return;
		}
		try {
			if(!StringUtils.isBlank(worfJob.getClassName()) && !StringUtils.isBlank(worfJob.getMethodName())) {
				if(logger.isDebugEnabled()) {
					logger.debug("a trigger is fired, trigger name={}, job name={}, trigger cron={}, job desc={}",
							worfJob.getTriggerName(),worfJob.getJobName(), worfJob.getCronExpression(), worfJob.getDesc());
				}
				Class<?> targetClass = Class.forName(worfJob.getClassName());
				Object targetObject = ApplicationContextHolder.get().getBean(targetClass);
				Method targetMethod = ReflectionUtils.findMethod(targetClass, worfJob.getMethodName());
				try {
					DynamicDataSourceHolder.clearDataSource();
					//调用任务配置的具体方法
					ReflectionUtils.invokeMethod(targetMethod, targetObject);
				} catch (Exception e) {
					//如果定义了异常处理方法，则再抛出异常后调用
					if (targetObject instanceof ScheduleExceptionHandle) {
						ScheduleExceptionHandle exceptionHandler = (ScheduleExceptionHandle)targetObject;
						exceptionHandler.afterException(worfJob, e);
					}
					throw e;
				}
			}
		} catch (Exception e) {
			logger.error("a trigger is occur error,job name=" + worfJob.getJobName() + "job desc=" + worfJob.getDesc(), e);
		} finally {
			if(logger.isDebugEnabled()) {
				logger.debug("the task is over, trigger name={}, job name={}, trigger cron={}, job desc={}",
						worfJob.getTriggerName(),worfJob.getJobName(), worfJob.getCronExpression(), worfJob.getDesc());
			}
		}
	}
	
}
