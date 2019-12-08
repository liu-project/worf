package orj.worf.authentication.configure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import orj.worf.authentication.constant.SessionConstant;
import orj.worf.authentication.util.RequestUtil;
import orj.worf.core.util.ApplicationContextHolder;

/**
 * 验证相关配置
 * @author LiuZhenghua
 * 2018年1月12日 下午2:31:43
 */
public class WebConfigure {
	
	@Value("${auth.project.name:RJS}")
	private String authProjectName;
	private String zkHost;
	/**
	 * cookie中存储sessionTokenKey
	 * 多用户系统，用逗号分隔，如：session_token,session_token_dp
	 * 代码端调用{@link RequestUtil#get(String)}
	 * 参数传入{@link SessionConstant#SESSION_KEY_IN_COOKIE}获取当前请求是走的哪一套用户系统
	 * 如果两个用户系统都登陆了，则默认取第一个。
	 */
	@Value("${cookie.session.token.key:session_token}")
	private String cookieSessionKey;
	/**
	 * 有的系统以sessionToken做为key存uid的时候，前台还加了个前缀，建议不加
	 */
	@Value("${session.token.prefix:}")
	private String sessionTokenPrefix;
	/**
	 * 方法加密密钥前缀，redis以 prefix+sessionToken作为key
	 */
	@Value("${method.secret.prefix:secret_key_}")
	private String methodSecretPrefix;
	
	//sessionTokenKey按逗号分隔拆分成的数组
	private String[] cookieSessionKeyArr = null;
	
	public String getAuthProjectName() {
		return authProjectName;
	}
	public void setAuthProjectName(String authProjectName) {
		this.authProjectName = authProjectName;
	}
	public String getZkHost() {
		return zkHost;
	}
	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}
	public String getSessionTokenPrefix() {
		return sessionTokenPrefix;
	}
	public void setSessionTokenPrefix(String sessionTokenPrefix) {
		this.sessionTokenPrefix = sessionTokenPrefix;
	}
	public String getMethodSecretPrefix() {
		return methodSecretPrefix;
	}
	public void setMethodSecretPrefix(String methodSecretPrefix) {
		this.methodSecretPrefix = methodSecretPrefix;
	}
	public String getCookieSessionKey() {
		return cookieSessionKey;
	}
	public void setCookieSessionKey(String cookieSessionTokenKey) {
		this.cookieSessionKey = cookieSessionTokenKey;
	}
	public String[] getCookieSessionKeyArr() {
		return cookieSessionKeyArr;
	}
	
	@PostConstruct
	public void init() {
		cookieSessionKeyArr = cookieSessionKey.split(",");
	}
	
	public static WebConfigure getInstance() {
		return ApplicationContextHolder.get().getBean(WebConfigure.class);
	}

}
