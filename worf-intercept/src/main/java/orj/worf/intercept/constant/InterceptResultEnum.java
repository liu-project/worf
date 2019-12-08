package orj.worf.intercept.constant;

public enum InterceptResultEnum {

	success(0,"成功"),
	fail(1,"失败");
	InterceptResultEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
    
    private int status;
    private String message;
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}
