package orj.worf.intercept.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import orj.worf.intercept.constant.InterceptResultEnum;
import orj.worf.intercept.constant.InterceptStatusEnum;
import orj.worf.intercept.dto.ResultDTO;
import orj.worf.intercept.model.RequestIntercept;
import orj.worf.intercept.util.HttpUtil;
import orj.worf.intercept.util.JsonUtil;
import orj.worf.redis.annotation.RedisDS;
import orj.worf.redis.constant.RedisDSEnum;
import orj.worf.redis.impl.RedisClientTemplateImpl;

@Service("interceptService")
@RedisDS(RedisDSEnum.ACTIVITY_DATA_SOURCE)
public class InterceptService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String CACH_INTERCEPT = "cachIntercept_";
	
	public static final String PRE_STRING = "intercept_";
	
	@Autowired
	private RedisClientTemplateImpl redisClientTemplateImpl;
	
	/**
	 * if request is null return directly, just intercept http request from the outside system
	 * 
	 * @param request
	 * @return
	 * 
	 */
	public ResultDTO intercept(HttpServletRequest request) {
		
		if(request == null) {
			return new ResultDTO(InterceptResultEnum.success);
		}
		
		try {
			String requestString = request.getRequestURI();
			
//			logger.info("uri={},param={}",requestString,getParam(request));
			
			if(requestString.indexOf(".json") > 0) {
				requestString = requestString.substring(0, requestString.indexOf(".json"));
			}
			
			//访问的数据在redis中缓存5分钟
			redisClientTemplateImpl.zadd(CACH_INTERCEPT + requestString, 
					System.currentTimeMillis() * 1000 + (int)(Math.random()*1000), String.valueOf(System.currentTimeMillis()));
			redisClientTemplateImpl.expire(CACH_INTERCEPT + requestString, 300);
			//删除五分钟前的数据
			redisClientTemplateImpl.zremrangeByScore(CACH_INTERCEPT + requestString, 0,
					System.currentTimeMillis() * 1000 - 300*1000000);
//			logger.info("requestString=" + requestString);
			String interceptJson = redisClientTemplateImpl.getVal(PRE_STRING + requestString);
//			logger.info("interceptJson=" + interceptJson);
			if(!StringUtils.isBlank(interceptJson)) {
				RequestIntercept intercept = JsonUtil.jsonStrToObject(interceptJson, RequestIntercept.class);
				if(intercept.getInterceptStatus() == InterceptStatusEnum.intercept.getCode()) {
					return new ResultDTO(11, intercept.getInterceptInfo(), null);
				} else if(!StringUtils.isBlank(intercept.getInterceptIp())) {
					String ip = "";
					ip = HttpUtil.getIpAddr(request);
					if (StringUtils.isBlank(ip)) {
			            ip = request.getRemoteAddr();
			        }
					
					if(("," + intercept.getInterceptIp() + ",").indexOf("," + ip + ",") >= 0) {
						return new ResultDTO(11, intercept.getInterceptInfo(), null);
					}
				}
			}
			
		} catch (Exception e) {
			// do nothing
			logger.error("异常", e);
		}
		return new ResultDTO(InterceptResultEnum.success);
	}


	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	private String getParam(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		Enumeration<String> e = request.getParameterNames();
		while(e.hasMoreElements()){
			String param = (String)e.nextElement();
			stringBuffer.append(param)
						.append(":")
						.append(request.getParameter(param));
			
			if(e.hasMoreElements()) {
				stringBuffer.append(",");
			}
		}
		return stringBuffer.toString();
	}
}
