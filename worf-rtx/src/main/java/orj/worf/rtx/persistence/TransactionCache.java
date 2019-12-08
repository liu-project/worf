package orj.worf.rtx.persistence;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import orj.worf.rtx.model.Transaction;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service("rtxtransactionCache")
public class TransactionCache {

	private static Cache<String, Transaction>  transactionCache = CacheBuilder.newBuilder().expireAfterAccess(86400, TimeUnit.SECONDS).maximumSize(Long.MAX_VALUE).build();;
	
	/**
	 * @Description 新增或更新
	 * @param transaction
	 * @author linruzhou 
	 * @date 2018年10月18日
	 */
	public void putToCache(Transaction transaction) {
		transactionCache.put(transaction.getId(), transaction);
	}
	
	/**
	 * @Description 查詢
	 * @param id
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月18日
	 */
	public Transaction getFromCache(String id) {
		return transactionCache.getIfPresent(id);
	}
	
	/**
	 * @Description 刪除
	 * @param id
	 * @author linruzhou 
	 * @date 2018年10月18日
	 */
	public void deleteFromCache(String id) {
		transactionCache.invalidate(id);
	}
}
