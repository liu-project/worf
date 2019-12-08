package orj.worf.exception.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.exception.annotation.Retry;

/**
 * 碰到异常后重试
 * @author LiuZhenghua
 * 2017年5月25日 下午3:17:57
 */
public class RetryAspect {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> target = pjp.getTarget().getClass();
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        
        Retry retry = target.getAnnotation(Retry.class);
        Method method = signature.getMethod();
        if (method.getAnnotation(Retry.class) != null) {
        	retry = method.getAnnotation(Retry.class);
        }
        if (retry != null) {
        	Object ret = null;
        	int retried = 0;
        	while (true) {
        		try {
        			if (retried > retry.retries()) {
            			break;
            		}
            		ret = pjp.proceed(args);
            		break;
            	} catch (Exception e) {
            		++retried;
            		if (retry.exception().isAssignableFrom(e.getClass())) {
            			if (retry.logException()) {
            				logger.info("{}执行出现了异常", method.getName(), e);
            			}
            			if (retried > retry.retries() && retry.throwsException()) {
                			throw e;
                		}
            			if (retry.delay() > 0) {
            				Thread.sleep(retry.delay());
            			}
            		} else {
            			if (!retry.throwsException()) {
            				logger.info("{}执行出现了异常", method.getName(), e);
            				break;
            			}
            			throw e;
            		}
            		
            	}
        	}
        	return ret;
        } else {
        	return pjp.proceed(args);
        }
    }
}
