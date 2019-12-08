package orj.worf.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注该注解的类或方法将被校验
 * @author LiuZhenghua
 * 2016年11月16日 下午2:49:32
 */
@Target({ 
	ElementType.TYPE,
	ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Check {

}
