package orj.worf.exception.interceptor;

import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.exception.FastCheckedException;
import orj.worf.exception.FastRuntimeException;
import orj.worf.exception.annotation.SecureLog;
import orj.worf.exception.util.ExceptionUtils;

class ExceptionInterceptor {
	
    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    public void handleAppException(final JoinPoint jp, final Throwable e) throws Throwable {
        final Signature signature = jp.getSignature();
        final String declaringTypeName = signature.getDeclaringTypeName();
        final String methodFullName = declaringTypeName + "." + signature.getName();
        if (logger.isDebugEnabled()) {
            logger.debug("Catch an exception[{}] when invoke method[{}].", e.getClass().getName(), methodFullName);
        }
        Logger log = LoggerFactory.getLogger(declaringTypeName);
        if (ExceptionUtils.isFastException(e)) {
        	String code = null;
        	if (FastCheckedException.class.isInstance(e)) {
        		code = ((FastCheckedException)e).getCode();
        	} else if (FastRuntimeException.class.isInstance(e)) {
        		code = ((FastRuntimeException)e).getCode();
        	}
            log.warn("Method [{}] threw an FastException code:{},message:{}.", signature.getName(), code, e.getMessage());
            throw e;
        }
        log(jp, e);
        throw e;
    }
    
    private String buildLog(final JoinPoint jp, final Throwable e) {
		MethodSignature signature = (MethodSignature)jp.getSignature();
		SecureLog secureLog = signature.getMethod().getAnnotation(SecureLog.class);
        List<String> detail = new LinkedList<String>();
        detail.add("********************** Exception is thrown **********************");
        detail.add(concat("class", signature.getDeclaringTypeName()));
        detail.add(concat("method", signature.getMethod().getName()));
        if (secureLog != null) {
        	detail.add(concat("parameters", "该方法有Secure注解，不打印参数。"));	
        } else {
        	detail.add(concat("parameters", ExceptionUtils.convertArgsToString(jp.getArgs())));
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

    private void log(final JoinPoint jp, final Throwable e) {
    	MethodSignature signature = (MethodSignature)jp.getSignature();
        Logger log = LoggerFactory.getLogger(signature.getDeclaringType());
        log.error(buildLog(jp, e));
    }

}