package orj.worf.rtx.manage;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import orj.worf.rtx.constant.TransactionConstant;
import orj.worf.rtx.constant.TransactionStatus;
import orj.worf.rtx.model.Branchs;
import orj.worf.rtx.model.Transaction;
import orj.worf.rtx.model.TransactionConfig;
import orj.worf.rtx.model.TransactionContext;
import orj.worf.rtx.persistence.TransactionPersist;
import orj.worf.rtx.util.RTXUtil;

@Service("rtxtransactionManager")
public class TransactionManager {

	public static final ThreadLocal<Deque<Transaction>> threadLocalTransaction = new ThreadLocal<Deque<Transaction>>();

	private ExecutorService servicePool = Executors.newFixedThreadPool(20);
	
	// 持久化
	@Autowired
	private TransactionPersist transactionPersist;
	
	/**
	 * @Description
	 * @param config
	 * @param context
	 * @param slaveNode 是否为从节点
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月24日
	 */
	public Transaction startTrans(TransactionConfig config, TransactionContext context, boolean slaveNode) {
		if(threadLocalTransaction.get() == null) {
			initQueue();
		}
		
		Transaction transaction = new Transaction();
		if(context != null) {
			transaction.setId(context.getId());
			if(!StringUtils.isBlank(context.getSlaveId())) {
				transaction.setId(transaction.getId() + context.getSlaveId());
			}
		} else {
			transaction.setId(RTXUtil.getTransactionId(TransactionConstant.SERVER_NAME));
		}
		transaction.setSlaveNode(slaveNode);
		transaction.setPropagation(config.getPropagation());
		transaction.setRetryTimes(config.getRetryTimes());
		transaction.setHasRetryTimes(0);
		transaction.setTimeOut(config.getTimeOut());
		transaction.setStartTime(System.currentTimeMillis());
		transaction.setEndTime(transaction.getStartTime() + transaction.getTimeOut()*1000);
		transaction.setStatus(TransactionStatus.TRYING);
		transaction.setAsyncCancel(config.isAsyncCancel());
		transaction.setAsyncConfirm(config.isAsyncConfirm());
		//持久化
		transactionPersist.addTrans(transaction);
		threadLocalTransaction.get().push(transaction);
		return transaction;
	}
	
	/**
	 * @Description 主节点提交时才视注解配置是否走异步，从节点一律同步调用
	 * @param transaction
	 * @param isAsync
	 * @author linruzhou 
	 * @date 2018年10月24日
	 */
	public void commitTrans(final Transaction transaction, boolean isAsync) {
		if(transaction != null) {
			transaction.setStatus(TransactionStatus.CONFIRMING);
			transactionPersist.updateTrans(transaction);
			if(transaction.isAsyncConfirm() && isAsync) {
				servicePool.execute(new Runnable(){
					@Override
					public void run() {
						doCommit(transaction);						
					}
				});
			} else {
				doCommit(transaction);
			}
		}
	}
	
	public void rollbackTrans(final Transaction transaction, boolean isAsync) {
		if(transaction != null) {
			transaction.setStatus(TransactionStatus.CANCELING);
			transactionPersist.updateTrans(transaction);
			if(transaction.isAsyncConfirm() && isAsync) {
				servicePool.execute(new Runnable(){
					@Override
					public void run() {
						doRollback(transaction);				
					}
				});
			} else {
				doRollback(transaction);
			}
		}
	}
	
	private void doCommit(Transaction transaction) {
		List<Branchs> branchs = transaction.getBranchsList();
		if(branchs != null && branchs.size() >0) {
			for(Branchs branch : branchs) {
				if(TransactionConstant.SERVER_NAME.equals(branch.getProjectName())) {
					branch.commit();	
				}
			}
		}
		transactionPersist.deleteTrans(transaction);
	}
	
	private void doRollback(Transaction transaction) {
		List<Branchs> branchs = transaction.getBranchsList();
		if(branchs != null && branchs.size() >0) {
			for(Branchs branch : branchs) {
				if(TransactionConstant.SERVER_NAME.equals(branch.getProjectName())) {
					branch.rollback();
				}
			}
		}
		transactionPersist.deleteTrans(transaction);
	}
	
    public Deque<Transaction> initQueue() {
	   Deque<Transaction> deque = new LinkedList<Transaction>();
	   threadLocalTransaction.set(new LinkedList<Transaction>());
	   return deque;
    }

	public TransactionPersist getTransactionPersist() {
		return transactionPersist;
	}

	public void setTransactionPersist(TransactionPersist transactionPersist) {
		this.transactionPersist = transactionPersist;
	}
    
}
