package orj.worf.rtx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import orj.worf.rtx.constant.Propagation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TCCTransactional {

	/**
	 * TCC 事务模式下，确认方法，必须设置
	 * @return
	 */
	public String confirmMethod() default "";
	
	/**
	 * TCC 事务模式下，取消方法，必须设置
	 * @return
	 */
	public String cancelMethod() default "";
	
	/**
	 * TCC 事务模式下，事务传播行为，提供两种传播（REQUIRED，REQUIRES_NEW），意义同spring事务传播
	 * @return
	 */
	Propagation propagation() default Propagation.REQUIRED;
	
	/**
	 * 事务超时时间，不设置则按系统设定时间来，-1表示不设置
	 * @return
	 */
	int timeout() default -1;
	
	/**
	 * confirm方法是否走异步，默认true 走异步
	 * @return
	 */
	public boolean asyncConfirm() default true;
	
	/**
	 * cancel方法是否走异步，默认true 走异步
	 * @return
	 */
	public boolean asyncCancel() default true;
}
