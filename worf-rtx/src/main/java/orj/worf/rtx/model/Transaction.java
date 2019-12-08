package orj.worf.rtx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import orj.worf.rtx.constant.Propagation;
import orj.worf.rtx.constant.TransactionStatus;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 4815384146811738470L;
	
	public String id; // 事务ID
	
	public boolean slaveNode; //是否为从节点
	
	public Propagation propagation; //事务传播特性
	
	public TransactionStatus status;
	
	public int timeOut;  //事务超时时间， 超时即重试
	
	public int retryTimes; //重试次数
	
	public int hasRetryTimes; //已经重试次数
	
	public long startTime;  //开始时间，精确到毫秒
	
	public long endTime;  //结束时间，精确到毫秒
	
	public List<Branchs> branchsList = new ArrayList<Branchs>();

	public boolean asyncConfirm = false;  //确认是否走异步
	
	public boolean asyncCancel = false; //取消是否走异步

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Propagation getPropagation() {
		return propagation;
	}

	public void setPropagation(Propagation propagation) {
		this.propagation = propagation;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
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

	public int getHasRetryTimes() {
		return hasRetryTimes;
	}

	public void setHasRetryTimes(int hasRetryTimes) {
		this.hasRetryTimes = hasRetryTimes;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<Branchs> getBranchsList() {
		return branchsList;
	}

	public void setBranchsList(List<Branchs> branchsList) {
		this.branchsList = branchsList;
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

	public boolean isSlaveNode() {
		return slaveNode;
	}

	public void setSlaveNode(boolean slaveNode) {
		this.slaveNode = slaveNode;
	}
	
}
