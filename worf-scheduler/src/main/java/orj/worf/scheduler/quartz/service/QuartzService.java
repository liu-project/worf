package orj.worf.scheduler.quartz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import orj.worf.datasource.DynamicDataSourceHolder;
import orj.worf.scheduler.quartz.config.QuartzConfigFactory;
import orj.worf.scheduler.quartz.constant.JobConstants;
import orj.worf.scheduler.quartz.model.WorfJob;
import orj.worf.scheduler.report.util.DateUtil;
import orj.worf.util.StringUtils;

/**
 * QUARTZ操作类
 * @author XFL
 * @date 2016年4月26日 上午11:36:47
 */
public class QuartzService {
    Logger logger = LoggerFactory.getLogger(QuartzService.class);

    @Autowired
    private WorfJobService worfJobService;
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private QuartzConfigFactory quartzConfigFactory;
    
    
    
    /**
     * 添加任务
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void addJob(WorfJob job) throws SchedulerException, ClassNotFoundException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
        JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        if (scheduler.checkExists(jobkey) && scheduler.checkExists(triggerKey)) {
            return;
        }
        JobDetail jobDetail = this.getJobDetail(jobkey, job);
        CronTrigger trigger = this.getCornTrigger(triggerKey, job);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 删除一个JOB的触发器，如果JOB已经没有触发器，清理掉job
     * @throws SchedulerException 
     */
    public void deleteJobTrigger(WorfJob job) throws SchedulerException {
        Scheduler sched = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
        JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        // 暂停触发器
        sched.pauseTrigger(triggerKey);
        // 移除触发器
        sched.unscheduleJob(triggerKey);
        sched.deleteJob(jobkey);
    }
    
    /**
     * 手动触发任务
     * @author LiuZhenghua
     * 2018年5月2日 下午5:11:39
     */
    public void triggerJob(WorfJob job) throws SchedulerException {
    	Scheduler sched = schedulerFactory.getScheduler();
    	JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
    	sched.triggerJob(jobkey);
    }


    /**
     * @throws SchedulerException 
     * 修改JOB的CORN表达式
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void modifyJobCron(WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
//        JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        if (!scheduler.checkExists(triggerKey)) {
            return;
        }
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldCron = trigger.getCronExpression();
        if (!StringUtils.equalsIgnoreCase(oldCron, job.getCronExpression())) {
            // this.deleteJob(job);
            // this.addJob(job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
//            trigger.getTriggerBuilder().forJob(jobkey).withSchedule(scheduleBuilder);
//            scheduler.resumeTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }

    }

    /**
     * 暂停某任务，不管它在哪个触发器里，主要看JOBKEY
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void pauseJob(WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.pauseJob(jobkey);
    }

    /**
     * 暂停触发器
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void pauseTrigger(WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
        scheduler.pauseTrigger(triggerKey);
    }

    /**
     * 恢复任务
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void resumeJob(WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobkey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.resumeJob(jobkey);
    }

	/**
     * 恢复触发器
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void resumeTrigger(WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
        scheduler.resumeTrigger(triggerKey);
    }

    /**
     * 停止所有调度
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void shutdownJobs() throws SchedulerException {
        Scheduler sched = schedulerFactory.getScheduler();
        if (!sched.isShutdown()) {
            sched.shutdown();
        }
    }

    /**
     * 开启所有调度
     * @author XFL 2016年4月26日 
     * @throws
     */
    public void startJobs() throws SchedulerException {
        Scheduler sched = schedulerFactory.getScheduler();
        if (!sched.isStarted()) {
            sched.start();
        }
    }
    
    /**
     * 获取所有执行中的任务
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getExecutingJobs() throws Exception {
//        List<WorfJob> jobList = new ArrayList<WorfJob>();
//        以下是单台服务器的正在执行中的计划查询，集群部署，不能用此法
//        Scheduler scheduler = schedulerFactory.getScheduler();
//        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
//        for (JobExecutionContext executingJob : executingJobs) {
//        	WorfJob job = new WorfJob();
//            JobDetail jobDetail = executingJob.getJobDetail();
//            JobKey jobKey = jobDetail.getKey();
//            Trigger trigger = executingJob.getTrigger();
//
//            job.setJobName(jobKey.getName());
//            job.setDesc(jobDetail.getDescription());  // 采用job的dec， 不需要用trigger的
//            job.setTriggerName(trigger.getKey().getName());
//            
//            TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//            job.setJobStatus(triggerState.name());
//            job.setNextFireTime(DateUtil.formatDateWith(trigger.getNextFireTime()));
//            if (trigger instanceof CronTrigger) {
//                CronTrigger cronTrigger = (CronTrigger) trigger;
//                String cronExpression = cronTrigger.getCronExpression();
//                job.setCronExpression(cronExpression);
//            }
//            jobList.add(job);
//        }
//        return jobList;
        
//        集群部署直接查询quartz提供的数据库
          return worfJobService.queryExecutingJobs();
    }

    /**
     * 获取所有计划中的任务,以触发时间升序排，时间精确到秒
     * @return
     * @throws Exception
     */
    public List<WorfJob> getPlanningJobs() throws Exception {
        List<WorfJob> jobList = new ArrayList<WorfJob>();
        Scheduler scheduler = schedulerFactory.getScheduler();
        
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JobConstants.GROUP_NAME));
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
            	WorfJob job = new WorfJob();
                job.setJobName(jobKey.getName());
                job.setDesc(scheduler.getJobDetail(jobKey).getDescription());
                TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                job.setNextFireTime(DateUtil.formatDateWith(trigger.getNextFireTime()));
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    job.setCronExpression(cronTrigger.getCronExpression());
                }
                jobList.add(job);
            }
        }
        Collections.sort(jobList);
        return jobList;
    }
    
    /**
     * 获取所有即将触发的任务
     * @return
     * @author linruzhou
     */
	public List<Map<String, String>> getBeginingToFireJobs() {
		return worfJobService.getBeginingToFireJobs();
	}

//    /**
//     * 获取所有的任务
//     * @return
//     * @throws Exception
//     */
//    public Map<String, List<WorfJob>> getJobs() throws Exception {
//        Map<String, List<WorfJob>> map = new HashMap<String, List<WorfJob>>();
//        map.put(JobConstants.PLANNINGS, getPlanningJobs());
//        map.put(JobConstants.EXECUTINGS, getExecutingJobs());
//        return map;
//    }
//    
    private CronTrigger getCornTrigger(TriggerKey triggerKey, WorfJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        CronTrigger trigger;
        if (scheduler.checkExists(triggerKey)) {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        } else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        }
        return trigger;

    }

    @SuppressWarnings("unchecked")
	private JobDetail getJobDetail(JobKey jobkey, WorfJob job) throws ClassNotFoundException, SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail;
        if (scheduler.checkExists(jobkey)) {
            jobDetail = scheduler.getJobDetail(jobkey);
        } else {
//            jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(job.getClassName()))
//                    .withIdentity(jobkey).withDescription(job.getDesc()).build();
//        	不再使用用户自定义的类，而该类也就不再需要实现Job接口，实现execute方法
            jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName("orj.worf.scheduler.quartz.job.BaseWorfJob"))
                    .withIdentity(jobkey).withDescription(job.getDesc()).build();
            // jobDetail.getJobDataMap().put("schedulerJob", job);
        }
        return jobDetail;
    }

    @PostConstruct
    private void init() {
    	DynamicDataSourceHolder.setDataSource(quartzConfigFactory.getDataSourceKey());
        List<WorfJob> list = worfJobService.queryEnabledJob();
        try {
            schedulerFactory.getScheduler().clear();
        } catch (SchedulerException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        for (WorfJob schedulerJob : list) {
            try {
                this.addJob(schedulerJob);
            } catch (ClassNotFoundException e) {
                logger.error("初始化任务失败，CLASS未找到，请检查数据库中classname是否正确");
            } catch (SchedulerException e) {
                logger.error("初始化任务失败，调度异常");
                e.printStackTrace();
            }
        }
        DynamicDataSourceHolder.clearDataSource();
    }

}
