package orj.worf.authentication.aspect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import orj.worf.authentication.annotation.SessionKey;
import orj.worf.authentication.config.SessionConfig;
import orj.worf.authentication.configure.WebConfigure;
import orj.worf.authentication.constant.PlatformEnum;
import orj.worf.authentication.constant.SessionConstant;
import orj.worf.authentication.dto.BaseRequest;
import orj.worf.authentication.dto.Request;
import orj.worf.authentication.exception.ForbbidenException;
import orj.worf.authentication.util.LogUtil;
import orj.worf.authentication.util.RequestUtil;
import orj.worf.exception.FastRuntimeException;
import orj.worf.redis.RedisClientTemplate;
import orj.worf.util.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

/**
 * 每次用户请求的时候，延长session有效期
 * @author LiuZhenghua
 * 2017年12月4日 下午3:43:51
 */
@Aspect
@Order(71)
public class SessionAspect {
	
	@Autowired
	private RedisClientTemplate redisService;
	@Autowired
	private SessionConfig sessionConfig;
	@Autowired
	private WebConfigure webConfigure;

	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		//1.get parameter from GET request.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String sessionToken = request.getParameter(SessionConstant.SESSION_TOKEN);
		String platform = request.getParameter(SessionConstant.PLATFORM);
		String appVersion = request.getParameter(SessionConstant.APP_VERSION);
		String sign = request.getParameter(SessionConstant.SIGN);
		String paramStr = null;
		BaseRequest requestBase = null;
		Object[] args = pjp.getArgs();
		
		//1.1 find if there is a BaseRequest type parameter.
		int requestBaseParamIndex = -1;
        for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof BaseRequest) {
				requestBaseParamIndex = i;
				requestBase = (BaseRequest)args[requestBaseParamIndex];
				break;
			}
		}
        //1.2 get parameter from POST request
        MethodSignature signature = (MethodSignature)pjp.getSignature();
    	if ("POST".equals(request.getMethod()) && requestBaseParamIndex != -1) { //对资产系统提供的接口没有RequestBase,读取流会导致它们读不到。
    		//if post request has no data, it would be null. 
    		paramStr = RequestUtil.getRequestParam(request);
    		if (StringUtils.isNotBlank(paramStr)) {
    			JsonNode node = JsonUtils.readTree(paramStr);
    			if (node == null) {
    				LoggerFactory.getLogger(signature.getDeclaringType()).warn("------------->method:{}, error:参数不是一个正确的json字符串, json:{}", signature.getName(), paramStr);
    				throw new ForbbidenException();
    			}
				if (args[requestBaseParamIndex] instanceof Request) {
					Type type = ((MethodSignature)pjp.getSignature()).getMethod().getGenericParameterTypes()[requestBaseParamIndex];
					requestBase = (Request)JsonUtils.parseJSON(paramStr, args[requestBaseParamIndex].getClass(), getGenericType(type));
					RequestUtil.put(SessionConstant.DATA_JSON, JsonUtils.toJSON(node.get("data")));
				} else {
					requestBase = (BaseRequest)JsonUtils.parseJSON(paramStr, args[requestBaseParamIndex].getClass());
					RequestUtil.put(SessionConstant.DATA_JSON, JsonUtils.toJSON(node));
				}
				args[requestBaseParamIndex] = requestBase;
				if (args[requestBaseParamIndex] == null) {
					LoggerFactory.getLogger(signature.getDeclaringType()).warn("------------->method:{}, error:部分RequestBase成员变量参数类型不合法, json:{}", signature.getName(), paramStr);
					throw new ForbbidenException();
				}
	    		sign = getNewValueIfNotEmpty(sign, JsonUtils.toJSON(node.get(SessionConstant.SIGN)));
	    		sessionToken = getNewValueIfNotEmpty(sessionToken, JsonUtils.toJSON(node.get(SessionConstant.SESSION_TOKEN)));
				platform = getNewValueIfNotEmpty(platform, JsonUtils.toJSON(node.get(SessionConstant.PLATFORM)));
				appVersion = getNewValueIfNotEmpty(appVersion, JsonUtils.toJSON(node.get(SessionConstant.APP_VERSION)));
    		}
    	}
		//2.if user not login, use other's uid and use null secret key to post data, it's will be passed.
		if (requestBase != null) {
			requestBase.setUid(-1);
		}
		//3.if session token is empty, get parameter from cookie.
		if (StringUtils.isEmpty(sessionToken)) {
			Cookie[] cookies = request.getCookies();
			Map<String, String> cookieMap = convertCookieToMap(cookies);
			// get session_token from cookie
			SessionKey sessionKey = signature.getMethod().getAnnotation(SessionKey.class);
			if (sessionKey != null) {
				sessionToken = cookieMap.get(sessionKey.value());
				RequestUtil.put(SessionConstant.SESSION_KEY_IN_COOKIE, sessionKey.value());
			} else {
				for (String cookieKey : webConfigure.getCookieSessionKeyArr()) {
					if (cookieMap.get(cookieKey) != null) {
						sessionToken = cookieMap.get(cookieKey);
						RequestUtil.put(SessionConstant.SESSION_KEY_IN_COOKIE, cookieKey);
						break;
					}
				}	
			}
			//get platform from cookie
			platform = getNewValueIfNotEmpty(platform, cookieMap.get(SessionConstant.PLATFORM));
			appVersion = getNewValueIfNotEmpty(appVersion, cookieMap.get(SessionConstant.APP_VERSION));
		}
		//extension 1.system maintenance add by zhangkailei 2018年4月10日10:09:58
        if (PlatformEnum.IOS.getCode().equalsIgnoreCase(platform) 
        		|| PlatformEnum.ANDROID.getCode().equalsIgnoreCase(platform)
        			|| PlatformEnum.WEB.getCode().equalsIgnoreCase(platform)
        				|| PlatformEnum.WAP.getCode().equalsIgnoreCase(platform)) {
        	String maintenanceStatus = sessionConfig.getMaintenanceStatus();
        	if("1".equals(maintenanceStatus)) {
        		String time = sessionConfig.getMaintenanceTime();
        		String desc = sessionConfig.getMaintenanceDesc();
        		JsonObject json = new JsonObject();
        		json.addProperty("time", time);
        		json.addProperty("desc", desc);
        		throw new FastRuntimeException("9998", json.toString());
        	}
        }
		//4.Lengthening the session token and the method secret key expired time.
		RequestUtil.put(SessionConstant.IP, RequestUtil.getIpAddr(request));
		RequestUtil.put(SessionConstant.SIGN, sign);
		RequestUtil.put(SessionConstant.PLATFORM, platform);
		RequestUtil.put(SessionConstant.APP_VERSION, appVersion);
		if (StringUtils.isNotEmpty(sessionToken)) {
			Integer uid = RequestUtil.getUidBySessionToken(webConfigure.getSessionTokenPrefix() + sessionToken, redisService);
			if (uid != -1) {
				//lengthening the session token and the method secret key expired time.
				//默认走web登陆超时时间
	            Integer expireTime = null;
	            if (PlatformEnum.IOS.getCode().equalsIgnoreCase(platform)) {
	            	expireTime = Integer.valueOf(sessionConfig.getIosLoginTimeout());
	            } else if (PlatformEnum.ANDROID.getCode().equalsIgnoreCase(platform)) {
	            	expireTime = Integer.valueOf(sessionConfig.getAndroidLoginTimeout());
	            } else if (PlatformEnum.WEB.getCode().equalsIgnoreCase(platform)) {
	            	expireTime = Integer.valueOf(sessionConfig.getWebLoginTimeout());
	            } else if (PlatformEnum.WX.getCode().equalsIgnoreCase(platform)) {
	            	expireTime = Integer.valueOf(sessionConfig.getWxLoginTimeout());
	            } else if (PlatformEnum.WAP.getCode().equalsIgnoreCase(platform)) {
	            	expireTime = Integer.valueOf(sessionConfig.getWapLoginTimeout());
	            }
	            RequestUtil.put(SessionConstant.SESSION_TOKEN, sessionToken);
	    		RequestUtil.put(SessionConstant.UID, String.valueOf(uid));
	            //if the session expired at that time, then it throws an exception, and needed to log in at next request.
	            if (expireTime != null) {
	            	redisService.expire(webConfigure.getSessionTokenPrefix() + sessionToken, expireTime);
	            	redisService.expire(webConfigure.getMethodSecretPrefix() + sessionToken, expireTime);
	            } else {
	            	LogUtil.getLogger().warn("platform不合法，此次不调整session有效期, uid:{}, ip:{}, platform:{}, class:{}, method:{}"
	            			,RequestUtil.get(SessionConstant.UID), RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM)
	        				, signature.getDeclaringType().getName(), signature.getMethod().getName());
	            }
	    		if (requestBase != null) {
	    			requestBase.setUid(uid);
	    		}
	    		LogUtil.info(signature, RequestUtil.get(SessionConstant.UID));
			}
		}
		//5.invoking the target method
		return pjp.proceed(args);
	}
	
	private static Class<?> getGenericType(Type type) {
		if (type instanceof ParameterizedType) {
			return (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
		} else {
			return (Class<?>)type;
		}
	}

	private Map<String, String> convertCookieToMap(Cookie[] cookies) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}	
		}
		return cookieMap;
	}
	
	private String getNewValueIfNotEmpty(String oldValue, String newValue) {
		if (StringUtils.isNotEmpty(newValue)) {
			return newValue;
		} else {
			return oldValue;
		}
	}
}
