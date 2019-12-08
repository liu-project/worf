package orj.worf.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ 
	ElementType.FIELD,
	ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MaxValue {

	public abstract String value(); 		//最大值
	
	public abstract String message();		//错误信息
	
	public abstract int code() default -2;	//错误码
	
	public boolean notnull() default true; //是否允许为空
}
