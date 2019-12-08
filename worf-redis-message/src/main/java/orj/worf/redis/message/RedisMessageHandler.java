package orj.worf.redis.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import orj.worf.redis.RedisClientTemplate;

/**
 * 
 * @Title: RedisMessageHandler.java
 * @Package orj.worf.redis.message
 * @Description: 支持轮询拉取队列消息（ONly for single consumer）
 * @author RJS
 * @date 2016年4月21日 上午11:14:12
 * @version V1.0
 */
public abstract class RedisMessageHandler implements InitializingBean{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected RedisClientTemplate redisClientTemplate;
	
	public abstract String getMessageKey();
	

	
	//在Spring初始化后执行一次
    public void afterPropertiesSet() {  
    	execute();
    } 

	private void execute() {
		// TODO Auto-generated method stub
    	ExecutorService service = Executors.newFixedThreadPool(1);
    	service.execute(new Runnable() {
		
			public void run() {
				while(true){
		    		String value = null;
		    		try{
		    			value  = redisClientTemplate.rpop(getMessageKey());
		    			if(value == null){
		    				//如果没有取到值等待1秒
		        			Thread.sleep(200);
		    			}else{
		    				onMessage(value);
		    			}
		    			
		    		}catch(Exception e){
		    			logger.error("redis message handler error,redis value:"+value,e);
		    		}
		    		
		    	}
			}
		});
	}


	public abstract void onMessage(String message);
}
