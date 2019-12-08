package orj.worf.rtx.model;

import orj.worf.rtx.annotation.TCCTransactional;
import orj.worf.rtx.constant.Propagation;
import orj.worf.rtx.constant.TransactionConstant;

public class TransactionConfig {

	public Propagation propagation;
	
	public int timeOut = TransactionConstant.TRANSACTION_TIME_OUT;

	public int retryTimes = TransactionConstant.TRANSACTION_RETRY_TIMES;  //间隔时间如果为1分钟，retryTimes分钟后还没执行成功，默认失败，不再处理，供人工介入(如何提醒？（待优化）)
	
	public boolean asyncConfirm;  //确认是否走异步
	
	public boolean asyncCancel; //取消是否走异步
	
	private TCCTransactional transactional;

	public Propagation getPropagation() {
		return propagation;
	}

	public void setPropagation(Propagation propagation) {
		this.propagation = propagation;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public boolean isAsyncConfirm() {
		return asyncConfirm;
	}

	public void setAsyncConfirm(boolean asyncConfirm) {
		this.asyncConfirm = asyncConfirm;
	}

	public boolean isAsyncCancel() {
		return asyncCancel;
	}

	public void setAsyncCancel(boolean asyncCancel) {
		this.asyncCancel = asyncCancel;
	}

	public TCCTransactional getTransactional() {
		return transactional;
	}

	public void setTransactional(TCCTransactional transactional) {
		this.transactional = transactional;
	}
	
}
