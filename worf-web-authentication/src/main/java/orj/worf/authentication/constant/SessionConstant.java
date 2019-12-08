package orj.worf.authentication.constant;

public class SessionConstant {
    
	//BaseRequest参数定义了接口，getSession_token, getPlatform,所以此处参数名是固定死的
    public static final String SESSION_TOKEN = "session_token";
    
    public static final String PLATFORM = "platform";
    
    //Request中，data部分的JSON，会作为加密内容体
    public static final String DATA_JSON = "data_json";
    
    //cookie中存储的session key, 主要用户多用户系统的情况
    public static final String SESSION_KEY_IN_COOKIE = "session_key_in_cookie";
    
    public static final String COOKIES = "cookies";
    
    //本次方法调用的签名
    public static final String SIGN = "sign";
    
    //当前登陆的用户uid
    public static final String UID = "uid";
    
    //访问者的ip
    public static final String IP = "ip";
    
    public static final String APP_VERSION = "app_version";
}
