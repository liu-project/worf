package orj.worf.log.dto;

import java.math.BigDecimal;

/**
 * 推标
 * @author LiuZhenghua
 * 2018年4月19日 上午11:43:02
 */
public class BorrowPushDTO extends ServiceLogDTO {
	/**借款类型*/
	private String loanKind;
	/**借款子类型*/
	private String loanSubKind;
	/**合作方*/
	private String partner;//合作方
	/**借款金额*/
	private BigDecimal borrowMoney;//借款金额
	public String getLoanKind() {
		return loanKind;
	}
	public void setLoanKind(String loanKind) {
		this.loanKind = loanKind;
	}
	public String getLoanSubKind() {
		return loanSubKind;
	}
	public void setLoanSubKind(String loanSubKind) {
		this.loanSubKind = loanSubKind;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	public BorrowPushDTO(String loanKind, String loanSubKind, String partner,
			BigDecimal borrowMoney) {
		super();
		this.loanKind = loanKind;
		this.loanSubKind = loanSubKind;
		this.partner = partner;
		this.borrowMoney = borrowMoney;
	}
	
	public BorrowPushDTO() {
		super();
	}
	@Override
	public String toString() {
		return "{\"loanKind\":\"" + loanKind + "\",\"loanSubKind\":\""
				+ loanSubKind + "\",\"partner\":\"" + partner
				+ "\",\"borrowMoney\":\"" + borrowMoney + "\",\"type\":\""
				+ type + "\",\"code\":\"" + code + "\",\"desc\":\"" + desc
				+ "\",\"timestamp\":\"" + timestamp + "\"}";
	}
	
	
}
