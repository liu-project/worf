/*******************************************************************************
 * @Title: TALogDAO.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx.dao;

import org.springframework.stereotype.Repository;

import orj.worf.dtx.TXLog;

/**   
 * @Title: TXLogDAO.java
 * @Description: 事务日志DAO
 * @author zouxuejun
 * @date 2017年6月7日 下午3:25:25
 * @version V1.0   
 */
@Repository
public interface TXLogDAO {
	
	
	void addLog(TXLog log);
	
	void modifyLog(TXLog log);
	
	TXLog selectForUpdate(String tid);
	
	TXLog findById(String tid);
	
}
