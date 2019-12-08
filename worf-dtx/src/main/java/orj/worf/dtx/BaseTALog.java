/*******************************************************************************
 * @Title: BaseTALog.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import java.util.Map;

/**   
 * @Title: BaseTALog.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年5月24日 下午8:19:46
 * @version V1.0   
 */
public abstract class BaseTALog {
    
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
    
    private long completeTime;
    
    private long rollbackTime;
    
    
    private int status;
    
    private String exception;
    
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
     * @return the rollbackTime
     */
    
    public long getRollbackTime() {
        return rollbackTime;
    }

    /**
     * @param rollbackTime the rollbackTime to set
     */
    public void setRollbackTime(long rollbackTime) {
        this.rollbackTime = rollbackTime;
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
}
