/*******************************************************************************
 * @Title: DTXSerivceImpl.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx.impl;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import orj.worf.dtx.DTXService;
import orj.worf.dtx.TXLog;
import orj.worf.dtx.dao.TXLogDAO;

/**   
 * @Title: DTXSerivceImpl.java
 * @Description: 分布式事务处理
 * @author zouxuejun
 * @date 2017年6月13日 下午2:51:05
 * @version V1.0   
 */

public class DTXSerivceImpl implements DTXService {
 
	@Autowired
	TXLogDAO txLogDAO;
	
	/**
	 * <p>Description:开始事务 </p>
	 * @author dell 2017年6月13日 
	 * @param bid
	 * @param serviceName
	 * @param operation
	 * @param params
	 * @return
	 * @see orj.worf.dtx.DTXService#startTA(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public TXLog startTA(String bid, String serviceName, String operation, Map<String, String> params) {
		TXLog txLog = TXLog.buildTA(bid, serviceName, operation, params, null);
		txLog.setTid(generateId());
		txLogDAO.addLog(txLog);
		return txLog;
	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @return
	 * @see orj.worf.dtx.DTXService#findTA(java.lang.String)
	 */
	@Override
	public TXLog findTA(String tid) {
		return txLogDAO.findById(tid);
	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param ta
	 * @see orj.worf.dtx.DTXService#rollbackTA(orj.worf.dtx.TXLog)
	 */
	@Override
	public void rollbackTA(TXLog ta) {

	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @see orj.worf.dtx.DTXService#txCompleted(java.lang.String)
	 */
	@Override
	public void txCompleted(String tid) {
		TXLog txlog = txLogDAO.selectForUpdate(tid);
		txlog.setStatus(TXLog.TA_COMPLETED);
		txlog.setCompleteTime(System.currentTimeMillis());
		txLogDAO.modifyLog(txlog);
	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @see orj.worf.dtx.DTXService#txRollbacked(java.lang.String)
	 */
	@Override
	public void txRollbacked(String tid) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @param reason
	 * @see orj.worf.dtx.DTXService#txFailed(java.lang.String, java.lang.String)
	 */
	@Override
	public void txFailed(String tid, String reason) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @param reason
	 * @see orj.worf.dtx.DTXService#txTimout(java.lang.String, java.lang.String)
	 */
	@Override
	public void txTimout(String tid, String reason) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>Description:TODO </p>
	 * @author dell 2017年6月13日 
	 * @param tid
	 * @param reason
	 * @see orj.worf.dtx.DTXService#tagTA(java.lang.String, java.lang.String)
	 */
	@Override
	public void tagTA(String tid, String reason) {
		// TODO Auto-generated method stub

	}

	private String generateId(){
		return UUID.randomUUID().toString();
	}
}
