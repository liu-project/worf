package orj.worf.scheduler.quartz.model;

import orj.worf.core.model.BaseObject;

public class WorfJob extends BaseObject implements Comparable<WorfJob> {

    private static final long serialVersionUID = 1L;
    /** 任务id */
    private Long jobId;
    /** 任务名称 */
    private String jobName;
    /** 任务分组 */
    private String jobGroup;
    /** 触发器名称 **/
    private String triggerName;
    /** 触发器分组 **/
    private String triggerGroup;

    /** 任务状态  0启用 1禁用 2删除*/
    private String jobStatus;
    /** 任务运行时间表达式 */
    private String cronExpression;
    /** 任务描述 */
    private String desc;
    /** JOB CLASS的全名 **/
    private String className;
    /** JOB CLASS的方法名 **/
    private String methodName;
    /** 异常时的邮件通知，如果采用系统默认的处理方式，配置此值有效*/
    private String userEmail;
    /** 异常时的短信通知，如果采用系统默认的处理方式，配置此值有效*/
    private String userPhone;
    /** 下次触发时间 */
    private String nextFireTime;
    
    @Override
	public int compareTo(WorfJob o) {
		return this.getNextFireTime().compareTo(o.getNextFireTime());
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getClassName() {
        return className;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getDesc() {
        return desc;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

	public String getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

}
