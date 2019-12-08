package orj.worf.log.adapter;

import org.slf4j.Logger;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

/**
 * 事务提交后执行
 * @author LiuZhenghua
 * 2018年4月19日 上午11:58:43
 */
public class ServiceLogAdapter extends TransactionSynchronizationAdapter {

	private Logger logger;
	private String msg;
	public ServiceLogAdapter(Logger logger, String msg) {
		super();
		this.logger = logger;
		this.msg = msg;
	}
	@Override
	public void afterCommit() {
		logger.info(msg);
	}
	
	
}
