package orj.worf.rtx.model;

import java.io.Serializable;

import orj.worf.rtx.constant.TransactionStatus;

public class TransactionContext implements Serializable{

	private static final long serialVersionUID = 8934992586822909650L;

	private String id;  //主事务ID
	
	private String slaveId; //从节点Id(此处的节点非物理节点，一个try代表一个节点)
	
	private TransactionStatus status = TransactionStatus.TRYING;  //传递状态，默认TRYING

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}

}
