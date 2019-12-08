package orj.worf.authentication.aspect;


import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import orj.worf.authentication.constant.SessionConstant;
import orj.worf.authentication.exception.ForbbidenException;
import orj.worf.authentication.util.LogUtil;
import orj.worf.authentication.util.RequestUtil;
import orj.worf.exception.FastRuntimeException;
import orj.worf.exception.annotation.SecureLog;


/**
 * 控制层异常处理
 * @author LiuZhenghua
 * 2017年11月30日 下午6:50:13
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionAspect {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			//非法请求直接返回403错误码
			if (e instanceof ForbbidenException) {
				throw e;
			}
			if (e instanceof FastRuntimeException) {
				FastRuntimeException fe = (FastRuntimeException)e;
				LogUtil.warn((MethodSignature)pjp.getSignature(), fe);
				LoggerFactory.getLogger(pjp.getSignature().getDeclaringType())
				.warn("Method [{}] threw an FastException code:{},message:{}.", pjp.getSignature().getName(), fe.getCode(), fe.getMessage());	
			} else {
				LogUtil.error((MethodSignature)pjp.getSignature(), RequestUtil.get(SessionConstant.UID), e);
				LoggerFactory.getLogger(pjp.getSignature().getDeclaringType()).error(buildLog(pjp,e));
			}
			Constructor constructor = ((MethodSignature)pjp.getSignature()).getReturnType().getConstructor(int.class, String.class);
			if (constructor != null) {
				if (e instanceof FastRuntimeException) {
					FastRuntimeException fe = (FastRuntimeException)e;
					int code = 9999;
					if (fe.getCode() != null) {
						code = Integer.valueOf(fe.getCode());
					}
					
					return constructor.newInstance(code, fe.getMessage());
				} else {
					return constructor.newInstance(9999, "系统正在加载中……");
				}
			} else {
				return null;
			}
		} finally {
			RequestUtil.clear();
		}
	}
	
	private String buildLog(final ProceedingJoinPoint pjp, final Throwable e) {
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		SecureLog secureLog = signature.getMethod().getAnnotation(SecureLog.class);
        List<String> detail = new LinkedList<String>();
        detail.add("********************** Exception is thrown **********************");
        detail.add(concat("class", signature.getDeclaringTypeName()));
        detail.add(concat("method", signature.getMethod().getName()));
        if (secureLog != null) {
        	detail.add(concat("parameters", "该方法有Secure注解，不打印参数。"));	
        } else {
        	detail.add(concat("parameters", RequestUtil.get(SessionConstant.DATA_JSON)));
        }
		detail.add(concat("message", e.getMessage()));
		detail.add(concat("stacktrace", "") + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		return appendWithOSLineSeparator(detail);
	}

	private String concat(final String key, final Object obj) {
		StringBuilder buf = new StringBuilder(32);
		buf.append(key);
		buf.append(" : ");
		buf.append(obj);
		return buf.toString();
	}

	private String appendWithOSLineSeparator(final List<String> str) {
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		for (String s : str) {
			sb.append(s);
			sb.append(lineSeparator);
		}
		return sb.toString();
	}
}
