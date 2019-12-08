package orj.worf.authentication.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定cookie中的session_key, 适用于多用户体系系统，接口只适用于其中一个用户系统
 * @author LiuZhenghua
 * 2018年1月19日 上午11:34:09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SessionKey {

	/**
	 * cookie中存储session token的key
	 * @author LiuZhenghua
	 * 2018年1月19日 上午11:39:04
	 */
	String value() default "session_token";
}
