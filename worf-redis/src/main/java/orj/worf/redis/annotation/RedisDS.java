package orj.worf.redis.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import orj.worf.redis.constant.RedisDSEnum;

@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisDS {
	public RedisDSEnum value() default RedisDSEnum.DEFAULT_DATA_SOURCE;
}
