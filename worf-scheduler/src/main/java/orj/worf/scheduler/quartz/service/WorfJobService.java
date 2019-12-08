package orj.worf.scheduler.quartz.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import orj.worf.scheduler.quartz.constant.JobConstants;
import orj.worf.scheduler.quartz.mapper.WorfJobDAO;
import orj.worf.scheduler.quartz.model.WorfJob;


public class WorfJobService {
    @Autowired
    private WorfJobDAO worfJobDAO;

    /**
     * 
     * @Title: queryEnabledJob
     * @Description: 查询可用的job信息
     * @param @return   
     * @return List<WorfJob> 
     * @author zhangwm 2016年5月4日 
     * @throws
     */
    public List<WorfJob> queryEnabledJob() {
        return worfJobDAO.queryByStatus(JobConstants.JOB_STATUS_ENABLED);
    }

    public void updateJobRunTime(Date date, String jobKey) {
        worfJobDAO.updateJobRunTime(date, jobKey);
    }
    
    
    public int insert(WorfJob worfJob) {
    	return worfJobDAO.insert(worfJob);
    }
    
    public void deleteByJobName(String jobName) {
    	worfJobDAO.deleteByJobName(jobName);
    }
    
    public int updateExpressionByJobName(String cronExpression, String jobName) {
    	return worfJobDAO.updateExpressionByJobName(cronExpression, jobName);
    }
    
    /**
     * 查询正在执行的任务，直接查询qrtz自带的qrtz_fired_triggers表
     * @return
     * @author linruzhou
     */
    public List<Map<String, String>> queryExecutingJobs() {
    	return worfJobDAO.queryExecutingJobs();
    }
    
    /**
     * 根据jobName查询Job
     * @param jobName
     * @return
     * @author linruzhou
     */
    public WorfJob queryWorfJobByJobName(String jobName) {
    	return worfJobDAO.queryWorfJobByJobName(jobName);
    }

    /**
     * 获取所有即将触发的任务
     * @param jobName
     * @return
     * @author linruzhou
     */
	public List<Map<String, String>> getBeginingToFireJobs() {
		return worfJobDAO.getBeginingToFireJobs();
	}
	
}
