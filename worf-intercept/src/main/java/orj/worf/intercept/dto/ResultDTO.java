package orj.worf.intercept.dto;

import orj.worf.intercept.constant.InterceptResultEnum;

public class ResultDTO {

    private int status;

    private String message;
    
    private Object data;

	public ResultDTO(int status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ResultDTO(InterceptResultEnum enums) {
		this.status = enums.getStatus();
		this.message = enums.getMessage();
		this.data = null;
	}
	
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
}
