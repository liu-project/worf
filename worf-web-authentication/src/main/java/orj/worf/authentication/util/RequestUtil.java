package orj.worf.authentication.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import orj.worf.authentication.configure.WebConfigure;
import orj.worf.exception.FastRuntimeException;
import orj.worf.redis.RedisClientTemplate;

public class RequestUtil {
	//存储框架跟应用程序之间交互的一些变量
	private static ThreadLocal<Map<String, String>> threadMap = new ThreadLocal<Map<String, String>>();
	
	public static void put(String key, String value) {
		Map<String, String> map = threadMap.get();
		if (map == null) {
			map = new HashMap<String, String>();
			threadMap.set(map);
		}
		map.put(key, value);
	}
	
	public static String get(String key) {
		Map<String, String> map = threadMap.get();
		if (map != null) {
			return map.get(key);
		}
		return null;
	}
	
	public static void clear() {
		threadMap.remove();
	}

	/**
	 * InputStream无法重复读取，规范的要求。虽然可以自己继承HttpServletWrapper和ServletInputStream, 使用ByteArrayInputStream去实现，在filter里替换request。
	 * @author LiuZhenghua 2018年7月13日 上午10:59:59
	 */
	public static String getRequestParam(HttpServletRequest request) throws Exception {
		if (request.getContentLength() == -1) {
			return null;
		}
		byte[] buf = new byte[request.getContentLength()];
		int len = 0, totalLength = 0;
		while ( (len = request.getInputStream().read(buf, totalLength, buf.length - totalLength)) != -1) {
			totalLength += len;
		}
		if (totalLength == 0) {
			return null;
		}
        return new String(buf, "utf-8");
	}
	
	/**
	 * Some request mapping of method are end with ".json" while others are not,
	 * so it's better get url from request. {@link RequestUtil#getRequestURL()}} 
	 * @author LiuZhenghua 2017年12月5日 下午2:07:41
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static String getRequestURL(ProceedingJoinPoint pjp) {
		MethodSignature signature = ((MethodSignature)pjp.getSignature());
		RequestMapping clzzRequestMapping = (RequestMapping) signature.getDeclaringType().getAnnotation(RequestMapping.class);
		RequestMapping methodRequestMapping = signature.getMethod().getAnnotation(RequestMapping.class);
		StringBuffer urlBuffer = new StringBuffer();
		//class url
		if (clzzRequestMapping.value() == null) {
			urlBuffer.append("/");
			urlBuffer.append(signature.getClass().getName());
		} else {
			if (clzzRequestMapping.value()[0].charAt(0) != '/') {
				urlBuffer.append("/");
			}
			urlBuffer.append(clzzRequestMapping.value()[0]);
		}
		//method url
		if (methodRequestMapping.value() == null) {
			urlBuffer.append("/");
			urlBuffer.append(signature.getMethod().getName());
			urlBuffer.append(".json");
		} else {
			if (methodRequestMapping.value()[0].charAt(0) != '/') {
				urlBuffer.append("/");
			}
			urlBuffer.append(methodRequestMapping.value()[0]);
			if (!methodRequestMapping.value()[0].endsWith(".json")) {
				urlBuffer.append(".json");
			}
		}
		
		return urlBuffer.toString();
	}
	
	public static String getRequestURL() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURI().substring(request.getContextPath().length());
	}
			
	/**
	 * 获取当前用户的加密key
	 * @author LiuZhenghua 2017年12月3日 下午5:22:20
	 */
	public static String getMethodSecretKey(RedisClientTemplate redisService, String sessionToken) {
		if (StringUtils.isEmpty(sessionToken)) {
			throw new FastRuntimeException(-1 + "", "请先登录");
		}
		String secretKey = redisService.getVal(WebConfigure.getInstance().getMethodSecretPrefix() + sessionToken);
		if (StringUtils.isEmpty(secretKey)) {
			throw new FastRuntimeException(-1 + "", "请先登录");
		}
		return secretKey;
	}
	
	/**
	 * 根据sessionToken获取uid
	 * @author LiuZhenghua 2017年12月4日 下午9:11:03
	 */
	public static Integer getUidBySessionToken(String sessionToken, RedisClientTemplate redisService) {
		String uid = redisService.getVal(sessionToken);
		if (uid == null) {
			return -1;
		}
		return Integer.valueOf(uid);
	}
	
	public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
        	ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}
