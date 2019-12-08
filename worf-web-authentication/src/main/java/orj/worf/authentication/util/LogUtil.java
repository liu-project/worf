package orj.worf.authentication.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.authentication.constant.SessionConstant;
import orj.worf.exception.FastRuntimeException;
import orj.worf.exception.annotation.SecureLog;

/**
 * 打印用户轨迹日志
 * @author LiuZhenghua
 * 2018年1月25日 下午7:33:39
 */
public class LogUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

	public static void info(MethodSignature signature, String uid) {
		String data = RequestUtil.get(SessionConstant.DATA_JSON);
		SecureLog secureLog = signature.getMethod().getAnnotation(SecureLog.class);
		if (secureLog != null) {
			data = "该方法有Secure注解，不打印参数。";
		}
		if (StringUtils.isNotBlank(RequestUtil.get(SessionConstant.APP_VERSION))) {
			logger.info("uid:{}, ip:{}, platform:{}({}), class:{}, method:{}, data:{}", uid, RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM),
					RequestUtil.get(SessionConstant.APP_VERSION), signature.getDeclaringType().getName(), signature.getMethod().getName(), data);	
		} else {
			logger.info("uid:{}, ip:{}, platform:{}, class:{}, method:{}, data:{}", uid, RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM)
					,signature.getDeclaringType().getName(), signature.getMethod().getName(), data);
		}
		
	}
	
	public static void error(MethodSignature signature, String uid, Throwable e) {
		/**
		 * 1.这里只打印异常错误的信息，不打印堆栈，堆栈由各自的业务类的logger打印，这里只是为了统计错误摘要
		 * 2.不打印FastRuntimeException，那个属于已知异常，特殊业务需要监控ServiceException直接监控对应的业务类的异常
		 */
		logger.error("uid:{}, ip:{}, platform:{}, class:{}, method:{}, errClass:{}, errMsg:{}", uid, RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM)
				, signature.getDeclaringType().getName(), signature.getMethod().getName(), e.getClass().getName(), e.getMessage());
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void warn(MethodSignature signature, FastRuntimeException e) {
		logger.warn("uid:{}, ip:{}, platform:{}, class:{}, method:{}, code:{}, message:{}", RequestUtil.get(SessionConstant.UID)
				, RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM)
				, signature.getDeclaringType().getName(), signature.getMethod().getName(), e.getCode(), e.getMessage());
	}
}
