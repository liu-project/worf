/*******************************************************************************
 * @Title: TXReq.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import java.util.Map;

/**   
 * @Title: TXReq.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年6月14日 下午2:52:59
 * @version V1.0   
 */
public class TXReq {

	  /**事务id */
    private String tid;
    
    /**business id */
    private String bid;
    
    private String serviceName;
    
   

	/**
	 * @return the operationName
	 */
	private String operationName;
    
    private Map<String,String> paramMap;
    
    
    private long timeoutSeconds;/*超时时间*/
    
    private long reqTime;
    
    /**
	 * @return the tid
	 */
	
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the bid
	 */
	
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the serviceName
	 */
	
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the operationName
	 */
	
	public String getOperationName() {
		return operationName;
	}

	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * @return the paramMap
	 */
	
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	/**
	 * @param paramMap the paramMap to set
	 */
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	/**
	 * @return the timeoutSeconds
	 */
	
	public long getTimeoutSeconds() {
		return timeoutSeconds;
	}

	/**
	 * @param timeoutSeconds the timeoutSeconds to set
	 */
	public void setTimeoutSeconds(long timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	/**
	 * @return the reqTime
	 */
	
	public long getReqTime() {
		return reqTime;
	}

	/**
	 * @param reqTime the reqTime to set
	 */
	public void setReqTime(long reqTime) {
		this.reqTime = reqTime;
	}

	/**
	 * @return the completeTime
	 */
	
	public long getCompleteTime() {
		return completeTime;
	}

	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * @return the exception
	 */
	
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * @return the status
	 */
	
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	private long completeTime;
    
    
    private String exception;
    
    private int status;
    
}
