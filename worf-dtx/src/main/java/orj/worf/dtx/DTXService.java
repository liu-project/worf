/*******************************************************************************
 * Created on 2017年5月24日 下午3:12:39
 * Copyright (c) 2014 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import java.util.Map;

/**
 * @Title: TAService.java
 * @Description: 分布式事务接口
 * @author zouxuejun
 * @date 2017年5月24日 下午3:27:50
 * @version V1.0
 */
public interface DTXService {

    /**
     * @Title: startTA
     * @Description:  开启事务
     * @param @param params
     * @param @return   
     * @return TAContext 
     * @throws
     */
    TXLog startTA(String bid, String serviceName, String operation, Map<String,String> params);
    
    TXLog findTA(String tid);
    
    /**
     * @Title: rollback
     * @Description: 回滚操作
     * @param @param ta   
     * @return void 
     * @throws
     */
    void rollbackTA(TXLog ta);
    
    
    
    /**
     * @Title: txCompleted
     * @Description: 事务完成
     * @param @param ta   
     * @return void 
     * @throws
     */
    void txCompleted(String tid);
    
    /**
     * @Title: txCompleted
     * @Description: 事务已回滚
     * @param @param ta   
     * @return void 
     * @throws
     */
    void txRollbacked(String tid);
    
    /**
     * @Title: txFailed
     * @Description: 事务完成
     * @param @param ta   
     * @return void 
     * @throws
     */
    void txFailed(String tid, String reason);
    
    /**
     * @Title: txTimout
     * @Description: 事务超时，状态将设置为未知
     * @param @param ta   
     * @return void 
     * @throws
     */
    void txTimout(String tid, String reason);
    
    /**
     * 
     * @Title: tagTA
     * @Description: 事务异常
     * @param @param tid
     * @param @param reason   
     * @return void 
     * @throws
     */
    void tagTA(String tid, String reason);
    

}
