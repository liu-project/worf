package orj.worf.intercept.constant;

public enum InterceptStatusEnum {

	not_intercept((byte)0,"不拦截"),
	intercept((byte)1,"拦截");
	
	private InterceptStatusEnum(byte code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private byte code;
	private String message;

	public String getString() {
		return message;
	}
	public void setString(String message) {
		this.message = message;
	}
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
}
