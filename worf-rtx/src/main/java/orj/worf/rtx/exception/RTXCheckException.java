package orj.worf.rtx.exception;

import orj.worf.exception.FastRuntimeException;

/**
 * RTX check 异常类
 * @author linruzhou
 *
 */
public class RTXCheckException extends FastRuntimeException {

	
	public RTXCheckException(String code, Throwable cause) {
		super(code, cause);
		this.code = code;
		this.msg = "";
	}

	private static final long serialVersionUID = 8530032657859140725L;

	private String code;
	
	private String msg;
	
	public RTXCheckException(String code, String msg) {
		super(code, msg);
		this.code = code;
		this.msg = msg;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
