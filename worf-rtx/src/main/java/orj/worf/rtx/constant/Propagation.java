package orj.worf.rtx.constant;

public enum Propagation {

	/**
	 * 有事务沿用原来的事务，没事务则创建一个事务
	 */
	REQUIRED(1);
	
//	/**
//	 * 不管原来有没有事务，都新建一个事务
//	 */
//	REQUIRES_NEW(2);
	
	private int id;

	private Propagation(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
