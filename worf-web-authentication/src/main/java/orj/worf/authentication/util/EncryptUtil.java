package orj.worf.authentication.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.slf4j.LoggerFactory;

import orj.worf.authentication.constant.SessionConstant;
import orj.worf.core.util.MD5Util;

/**
 * 前端请求接口加密工具类
 * sign = md5(请求路径 + '&' + md5(参数字母升序) + '&' + 加密key)
 * @author LiuZhenghua
 * 2017年11月30日 上午11:19:27
 */
public class EncryptUtil {
	
	public static boolean validateSign(String url, Map<String, Object> params, String key, String sign) throws Exception {
		String calcSign = getSign(url, params, key);
		if (calcSign.equals(sign)) {
			return true;
		}
		LoggerFactory.getLogger(EncryptUtil.class).warn("签名错误, uid:{}, ip:{}, platform:{}, url:{}, params：{}, key:{}, sign:{}, calcSign:{}"
				,RequestUtil.get(SessionConstant.UID), RequestUtil.get(SessionConstant.IP), RequestUtil.get(SessionConstant.PLATFORM)
				,url, map2LinkString(params, null), key, sign, calcSign);
		return false;
	}

	/**
	 * 生成签名
	 * @author LiuZhenghua 2017年11月30日 上午11:33:54
	 */
	private static String getSign(String url, Map<String, Object> params, String key) throws Exception {
		return MD5Util.lowerCaseMD5(url + '&' + MD5Util.lowerCaseMD5(map2LinkString(params, null)) + '&' + key);
	}
	
	/**
     * 讲map拼接成xxx=xxx&xxx=xxx的形式
     * @param map
     * @param ignore
     * @return
     */
    private static String map2LinkString(Map<String, Object> map, String[] ignore) {
    	if (map == null) {
    		return "";
    	}
		ArrayList<String> mapKeys = new ArrayList<String>(map.keySet());
        Collections.sort(mapKeys);
        StringBuilder link = new StringBuilder();
        boolean first = true;
        for_map_keys:
        for(String key: mapKeys) {
            Object value = map.get(key);
            if (value==null || "".equals(value.toString().trim())) continue;
            if (ignore != null) {
	            for(String i: ignore) {
	                if (i.equalsIgnoreCase(key)) continue for_map_keys;
	            }
            }

            if (!first) link.append("&");
            link.append(key).append("=").append(value);
            if (first) first = false;
        }
        return link.toString();
	}
    
}
