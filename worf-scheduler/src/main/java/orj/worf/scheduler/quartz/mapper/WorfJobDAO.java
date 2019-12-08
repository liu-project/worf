package orj.worf.scheduler.quartz.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import orj.worf.scheduler.quartz.model.WorfJob;

@Repository
public interface WorfJobDAO {

    List<WorfJob> queryByStatus(@Param("status") int status);

    void updateJobRunTime(@Param("lastRunTime") Date date, @Param("jobKey") String jobKey);
    
    /**
     * query job by job name
     * use 'limit 1',make sure return one object
     * @param jobName
     * @return
     * @author linruzhou
     */
    WorfJob queryWorfJobByJobName(@Param("jobName")String jobName);
    
    int insert(WorfJob worfJob);
    
    int updateExpressionByJobName(@Param("cron_expression")String cronExpression, @Param("job_name")String jobName);
    
    void deleteByJobName(String jobName);

    /**
     * 查询正在执行的任务，直接查询qrtz自带的qrtz_fired_triggers表
     * @return
     * @author linruzhou
     */
	List<Map<String, String>> queryExecutingJobs();

	List<Map<String, String>> getBeginingToFireJobs();
}