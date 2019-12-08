package orj.worf.rtx.persistence;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import orj.worf.rtx.model.Transaction;

public class TransactionPersist {

	private Persistence persistence;
	
//	@Autowired
//	private TransactionCache transactionCache;
	
	public void addTrans(Transaction transaction) {
		persistence.addTrans(transaction);
//		transactionCache.putToCache(transaction);
	}
	
	public void deleteTrans(Transaction transaction) {
		persistence.deleteTrans(transaction);
//		transactionCache.deleteFromCache(transaction.getId());
	}
	
	public void updateTrans(Transaction transaction) {
		persistence.updateTrans(transaction);
//		transactionCache.putToCache(transaction);
	}
	
	public Transaction queryTrans(String id) {
//		Transaction queryObj = transactionCache.getFromCache(id);
//		// 不用加锁
//		if (queryObj == null) {
//			queryObj = persistence.queryTrans(id);
//			if(queryObj != null) {
//				transactionCache.putToCache(queryObj);
//			}
//		}
		return persistence.queryTrans(id);
	}

	/**
	 * 查找超时的事务，由于去中心化， 此处只查找自己项目发起的超时事务
	 */
	public Set<String> findAllUnCommit(Date date) {
		return persistence.findAllUnCommit(date.getTime());
	}
	
	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}
	
}
