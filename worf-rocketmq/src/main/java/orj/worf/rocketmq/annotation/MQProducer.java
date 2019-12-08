package orj.worf.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.aliyun.openservices.ons.api.Producer;

/**
 * MQ生产者
 * 此注解只能在在{@link Producer}类型或其子类上使用
 * @author LiuZhenghua
 * 2016年11月25日 下午5:04:33
 */
@Target({
	ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MQProducer {
	
	public String value() default "${mq.defaultProducerId}";
	
}
