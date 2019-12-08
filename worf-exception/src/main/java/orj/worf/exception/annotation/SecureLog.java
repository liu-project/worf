package orj.worf.exception.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感方法需要加上此注解，避免异常日志打印了敏感信息
 * @author LiuZhenghua
 * 2018年9月26日 下午2:03:18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecureLog {

}
