package orj.worf.redis;

import java.util.Stack;

import orj.worf.redis.constant.RedisDSEnum;

/**
 * ThreadLocal机制选择当前redis源
 * @author linruzhou
 *
 */
public class RedisDataSourceHolder {

   /**
	 * 根据线程切换redis数据源
   */
   private static final ThreadLocal<Stack<RedisDSEnum>> THREAD_DATA_SOURCE = new ThreadLocal<Stack<RedisDSEnum>>();
   
   public static RedisDSEnum getDataSource() {
	   
	   if(THREAD_DATA_SOURCE.get() == null
			   || THREAD_DATA_SOURCE.get().empty()) {
		   return RedisDSEnum.DEFAULT_DATA_SOURCE;
	   }
	   return THREAD_DATA_SOURCE.get().peek();
   }

   public static void setDataSource(RedisDSEnum dataSource) {
	   initQueue();
	   THREAD_DATA_SOURCE.get().push(dataSource);
   }

   public static void clearDataSource() {
	   THREAD_DATA_SOURCE.get().pop();
	   //when the stack is empty, remove the stack from the threadLocal
	   if(THREAD_DATA_SOURCE.get().empty()) {
		   THREAD_DATA_SOURCE.remove();
	   }
   }
   
   public static void initQueue() {
	   Stack<RedisDSEnum> stack = THREAD_DATA_SOURCE.get();
	   if(stack == null) {
		   stack = new Stack<RedisDSEnum>();
		   THREAD_DATA_SOURCE.set(stack);
	   }
   }
   
}
