/*package orj.worf.mybatis.interceptor;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.annotation.Value;

import orj.worf.mybatis.thread.VerifyThread;
import orj.worf.rocketmq.annotation.MQProducer;

import com.aliyun.openservices.ons.api.Producer;

*//**
 * 防篡改项目需要在此拿sql
 * @author LiuZhenghua
 * 2016年12月4日 下午5:06:50
 *//*
@Intercepts({ 
	@org.apache.ibatis.plugin.Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class VerifyInterceptor implements Interceptor {
	
	private static ExecutorService executorService = Executors.newFixedThreadPool(50);
	
	@MQProducer
	private Producer producer;
	
	@Value("${mq.defaultTopic}")
	private String topic;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        executorService.execute(new VerifyThread(boundSql, configuration, producer, topic));
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

	

}
*/