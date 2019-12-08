package orj.worf.exception.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

	public int retries() default 2;	//失败重试次数
	
	public int delay() default 100;	//失败后延时时间，毫秒
	
	public boolean throwsException() default true;	//最后一次重试遇到异常后是否抛出
	
	public boolean logException() default false;	//打印异常日志,一般来说，看最后一次错误异常就可以了
	
	public Class<? extends Throwable> exception() default Throwable.class;	//捕获的异常
	
}
