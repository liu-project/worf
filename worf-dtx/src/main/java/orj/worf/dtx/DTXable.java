/*******************************************************************************
 * @Title: TAable.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

/**   
 * @Title: TAable.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年5月24日 下午5:42:43
 * @version V1.0   
 */
public interface DTXable {

    /**
     * @Title: rollback
     * @Description: 回滚操作
     * @param @param ta   
     * @return void 
     * @throws
     */
    void rollbackTA(TXLog ta);
    
    
    /**
     * 用于在特殊情况下，确认事务是否完成
     * @Title: hasCompleted
     * @Description: TODO
     * @param @param ta
     * @param @return   
     * @return boolean 
     * @throws
     */
    boolean hasCompleted(TXLog ta);
    
    /* 暂不支持 */
    //void redo(TAContext ta);
}
