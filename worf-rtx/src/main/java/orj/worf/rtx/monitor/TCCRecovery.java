package orj.worf.rtx.monitor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import orj.worf.redis.annotation.RedisDS;
import orj.worf.redis.constant.RedisDSEnum;
import orj.worf.redis.impl.RedisClientTemplateImpl;
import orj.worf.rtx.constant.TransactionConstant;
import orj.worf.rtx.constant.TransactionStatus;
import orj.worf.rtx.manage.TransactionManager;
import orj.worf.rtx.model.Transaction;
import ch.qos.logback.core.status.Status;

@RedisDS(RedisDSEnum.ACTIVITY_DATA_SOURCE)
@Service("rtxTCCRecovery")
public class TCCRecovery {

	public static final String RECOVERY_PREFIX = "rtx_recovery_transaction_";
	
	@Autowired
	TransactionManager transactionManager;
	@Autowired
	private RedisClientTemplateImpl redisClient;
	
	public void recover(Transaction transaction) {
		if(transaction != null) {
			//防止分布式部署时的并行执行
			String setResult = redisClient.setWithNxAndPx(RECOVERY_PREFIX  + TransactionConstant.SERVER_NAME + transaction.getId(), "", 60000);
			if(StringUtils.isBlank(setResult)) {
				return;
			}
			
			transaction.setHasRetryTimes(transaction.getHasRetryTimes() + 1);
			if(transaction.getHasRetryTimes() > transaction.getRetryTimes()) {
				return;  //直接返回。
			}
			/** 在规定的重试次数内还没转成CONFIRMING、CANCELING的。存在下面情况
			 *  1、try已经执行完成（失败或成功），但状态未被改成cancel或confirm -- 不管哪种情况，直接rollback
			 *  2、try还在执行中，直接rollback，try跟cancel同时执行， 有重大风险。
			 *  所以必须保证try在 timeout+retryTimes*retry时间间隔  时间内完成
			 *  换言之，出现异常情况需要cancel被执行时，最长时间为timeout+retryTimes*retry
			 */
			if(TransactionStatus.TRYING.equals(transaction.getStatus())) {
				if(transaction.getHasRetryTimes() == transaction.getRetryTimes()) {
					rollback(transaction);
				}
			} else if(TransactionStatus.CONFIRMING.equals(transaction.getStatus())) {
				commit(transaction);
			} else {
				rollback(transaction);
			}
		}
	}
	
	public void commit(Transaction transaction) {
		transactionManager.commitTrans(transaction, false);
	}
	
	public void rollback(Transaction transaction) {
		transactionManager.rollbackTrans(transaction, false);
	}

}
