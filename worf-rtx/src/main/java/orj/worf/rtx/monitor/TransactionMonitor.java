package orj.worf.rtx.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import orj.worf.rtx.manage.TransactionManager;
import orj.worf.rtx.model.Transaction;

@Service("rtxtransactionMonitor")
public class TransactionMonitor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TransactionManager transactionManager;
	@Autowired
	private TCCRecovery tccRecovery;
	
	@PostConstruct
	private void init() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						List<Transaction> transactionList = findUnCommit(new Date());
						if(transactionList != null && transactionList.size() > 0) {
							for(Transaction transaction : transactionList) {
								tccRecovery.recover(transaction);
							}
						}
					} catch (Exception e) {
						// do nothing
						logger.error("recover thread error", e);
					}
					
					try {
						Thread.sleep(120*1000);  //间隔两分钟执行
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public List<Transaction> findUnCommit(Date date) {
		List<Transaction> list = new ArrayList<Transaction>();
		Set<String> unCommitSet = transactionManager.getTransactionPersist().findAllUnCommit(date);
		if(unCommitSet != null && unCommitSet.size() >0) {
			for(String xid : unCommitSet) {
				try {
					Transaction transaction = transactionManager.getTransactionPersist().queryTrans(xid);
					if(transaction != null) {
						//主节点才需要加载，提交或回滚由主节点发起
						if(!transaction.slaveNode) {
							int intervalMin = new Double(Math.pow(2, transaction.getHasRetryTimes() + 2) - 2).intValue();
							if(transaction.getEndTime() + intervalMin*60*1000 >= System.currentTimeMillis()) {
								list.add(transaction);	
							}
						}
					}
				} catch (Exception e) {
					//相互之间最好不要影响
				}
			}
		}
		return list;
	}
}
