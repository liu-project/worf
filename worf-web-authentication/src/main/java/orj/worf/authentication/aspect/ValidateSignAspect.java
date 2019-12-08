package orj.worf.authentication.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import orj.worf.authentication.config.SessionConfig;
import orj.worf.authentication.constant.SessionConstant;
import orj.worf.authentication.dto.BaseRequest;
import orj.worf.authentication.dto.Request;
import orj.worf.authentication.util.EncryptUtil;
import orj.worf.authentication.util.RequestUtil;
import orj.worf.exception.FastRuntimeException;
import orj.worf.redis.RedisClientTemplate;
import orj.worf.util.JsonUtils;

/**
 * 校验本次调用签名是否正确
 * @author LiuZhenghua
 * 2017年11月30日 下午12:00:19
 */
@Aspect
@Order(72)
public class ValidateSignAspect {
	
	@Autowired
	private RedisClientTemplate redisService;
	@Autowired
	private SessionConfig sessionConfig;

	@SuppressWarnings({"unchecked" })
	@Around("@annotation(orj.worf.authentication.annotation.ValidateSign)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		//1.find parameter
		int requestBaseParamIndex = -1;
		Object[] args = pjp.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof BaseRequest) {
				requestBaseParamIndex = i;
				break;
			}
		}
		Signature signature = pjp.getSignature();
		if (requestBaseParamIndex == -1) {
			LoggerFactory.getLogger(signature.getDeclaringType()).error("------------->method:{}, error:方法必须包含一个实现了orj.worf.authentication.dto.Request参数", signature.getName());
			throw new FastRuntimeException("9999", "系统正在加载中……");
		}
		BaseRequest requestBase = (BaseRequest)args[requestBaseParamIndex];
		//2.return session_invalid if user not login.
		if (requestBase.getUid() == -1) {
			throw new FastRuntimeException("-1", "请先登录");
		}
		//3.validate sign
		String secretKey = RequestUtil.getMethodSecretKey(redisService, RequestUtil.get(SessionConstant.SESSION_TOKEN));
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, Object> params = null;
		String sign = RequestUtil.get(SessionConstant.SIGN);
		if ("GET".equals(request.getMethod())) {
			params = JsonUtils.objectToMap(requestBase);
		} else {
			if (requestBase instanceof Request) {
				String dataJsonStr = RequestUtil.get(SessionConstant.DATA_JSON);
				if (StringUtils.isNotBlank(dataJsonStr)) {
					params = JsonUtils.parseJSON(dataJsonStr, Map.class);
				}
			} else if (requestBase instanceof BaseRequest) {
				params = JsonUtils.objectToMap(requestBase);
			} else {
				LoggerFactory.getLogger(signature.getDeclaringType()).error("------------->method:{}, error:方法必须包含一个实现了orj.worf.authentication.dto.Request参数", signature.getName());
				throw new FastRuntimeException("9999", "系统正在加载中……");
			}
		}
		boolean signValid = EncryptUtil.validateSign(RequestUtil.getRequestURL(), params, secretKey, sign);
		if (!signValid) {
			if ("1".equals(sessionConfig.getCheckMethodSign())) {
				throw new FastRuntimeException("-2", "请先登录");
			}
		}
		//4.invoking the target method
		return pjp.proceed(args);
	}
	
}
