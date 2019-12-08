package orj.worf.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;

/**
 * MQ消费者
 * 使用该注解的类需要实现  {@link MessageOrderListener}接口
 * @author LiuZhenghua
 * 2016年11月30日 下午12:42:01
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MQOrderConsumer {
	
	String tags();									//二级标签，支持 tagA、tagA||tagB、*
	
	String topic() default "${mq.defaultTopic}";			//topic
	
	String consumerId() default "${mq.defaultConsumerId}";	//消费者Id
	
}
