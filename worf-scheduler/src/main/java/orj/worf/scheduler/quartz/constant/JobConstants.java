package orj.worf.scheduler.quartz.constant;

public class JobConstants {
    /**任务状态 -启用*/
    public static final int JOB_STATUS_ENABLED = 0;
    /**任务状态 -禁用*/
    public static final int JOB_STATUS_DISABLED = 1;
    /**任务状态 -删除*/
    public static final int JOB_STATUS_DELETED = 2;

    /**任务执行间隔，一天1440分钟*/
    public static final int JOB_INTERVAL_ONE_DAY = 1440;
    
    public static final String GROUP_NAME = "DEFAULT_GROUP";
    
    public static final String PLANNINGS = "planningJobs";
    
    public static final String EXECUTINGS = "executingJobs";
}
