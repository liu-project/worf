package orj.worf.rtx.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("rtxannotationAspect")
public class AnnotationAspect {

//	private static final Logger logger = LoggerFactory.getLogger(AnnotationAspect.class);
	
	@Autowired
	private AnnotationInterceptor annotationInterceptor;
	
	@Pointcut("execution(public * *(orj.worf.rtx.model.TransactionContext,..)) || @annotation(orj.worf.rtx.annotation.TCCTransactional)")
	public void annotation(){
		
	}
	
    @Around("annotation()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
    	// 最高优先级的拦截器，
    	// 拦截获取参数
    	// 业务方法执行成功后（本地事务提交），修改TCC状态
    	return annotationInterceptor.intercept(pjp);
    }

	public void setAnnotationInterceptor(AnnotationInterceptor annotationInterceptor) {
		this.annotationInterceptor = annotationInterceptor;
	}
    
}
