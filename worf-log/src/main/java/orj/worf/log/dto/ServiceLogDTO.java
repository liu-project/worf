package orj.worf.log.dto;

import java.util.Date;

import orj.worf.log.constant.ServiceLogEnum;

public class ServiceLogDTO {

	protected Integer type;
	protected Integer code;
	protected String desc;
	protected Long timestamp;
	
	public void init(ServiceLogEnum serviceLogEnum) {
		this.type = serviceLogEnum.getType();
		this.code = serviceLogEnum.getCode();
		this.desc = serviceLogEnum.getDesc();
		this.timestamp = new Date().getTime();
	}
	
}
