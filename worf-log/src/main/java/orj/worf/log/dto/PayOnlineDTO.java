package orj.worf.log.dto;

import java.math.BigDecimal;

/**
 * 充值
 * @author LiuZhenghua
 * 2018年4月17日 上午11:51:07
 */
public class PayOnlineDTO extends ServiceLogDTO {

	private BigDecimal money;
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public PayOnlineDTO(BigDecimal money) {
		super();
		this.money = money;
	}
	
	public PayOnlineDTO() {
		super();
	}
	
	@Override
	public String toString() {
		return "{\"money\":\"" + money + "\",\"type\":\"" + type
				+ "\",\"code\":\"" + code + "\",\"desc\":\"" + desc
				+ "\",\"timestamp\":\"" + timestamp + "\"}";
	}
	
}
