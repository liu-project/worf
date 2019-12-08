/*******************************************************************************
 * Created on 2017年5月24日 下午3:19:09
 * Copyright (c) 2014 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @Title: TAContext.java
 * @Description: 分布式事务
 * @author zouxuejun
 * @date 2017年5月24日 下午3:20:50
 * @version V1.0
 */
public class TXLog extends BaseTALog{

    /** 开启，已完成、失败、异常 */
    public static final int TA_STARTED = 1, TA_COMPLETED = 10, TA_FAILED = 20, TA_EXCEPTION = -1;

   
    
    private List<String> subServices;
    
    
    /**
     * @return the subLogs
     */
    
    public List<String> getSubServices() {
        return subServices;
    }

    /**
     * @param subLogs the subLogs to set
     */
    public void setSubServices(List<String> subServices) {
        this.subServices = subServices;
    }
    
 

    public static TXLog buildTA(String bid, String service, String operation, Map<String, String> params, List<String> subServices){
    	TXLog taLog = new TXLog();
    	
    	taLog.setBid(bid);
    	taLog.setServiceName(service);
    	taLog.setOperationName(operation);
    	taLog.setParamMap(params);
    	taLog.setSubServices(subServices);
    	taLog.setStatus(TA_STARTED);
    	return taLog;
    }
    
    public TXLog clone(){
    	try {
			TXLog co =  (TXLog)super.clone();
			co.subServices = this.subServices;
			return co;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
}
