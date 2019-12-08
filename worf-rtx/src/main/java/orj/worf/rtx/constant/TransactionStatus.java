package orj.worf.rtx.constant;

public enum TransactionStatus {
	/**
	 * try 预留资源阶段
	 */
	TRYING(1),
	
	/**
	 * 提交 阶段
	 */
	CONFIRMING(2),
	
	/**
	 * 回滚 阶段
	 */
	CANCELING(3);
	
	private int id;

	private TransactionStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
