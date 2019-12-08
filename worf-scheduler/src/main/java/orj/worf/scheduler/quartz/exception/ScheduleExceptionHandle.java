package orj.worf.scheduler.quartz.exception;

import orj.worf.scheduler.quartz.model.WorfJob;

/**
 * 计划执行的异常处理
 * @author LiuZhenghua
 * 2017年7月18日 下午12:35:59
 */
public interface ScheduleExceptionHandle {
	
	/**
	 * 定时任务异常后调用此方法
	 * @author LiuZhenghua
	 * 2017年7月18日 下午12:37:01
	 */
	public void afterException(WorfJob job, Throwable e);

}
