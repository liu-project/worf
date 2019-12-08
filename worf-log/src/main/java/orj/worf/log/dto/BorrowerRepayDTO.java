package orj.worf.log.dto;

import java.math.BigDecimal;

/**
 * 借款人还款
 * @author WenJia
 * 2018年12月19日 下午2:26:36
 *
 */
public class BorrowerRepayDTO extends ServiceLogDTO {
	
	/**标的类型*/
	private byte borrowType;
	
	/**还款方式：0-线上还款  1-线下还款*/
	private byte repayType;
	
	/**合作方*/
	private String partner;//合作方
	
	/**还款金额*/
	private BigDecimal repayAmount;

	public byte getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(byte borrowType) {
		this.borrowType = borrowType;
	}

	public byte getRepayType() {
		return repayType;
	}

	public void setRepayType(byte repayType) {
		this.repayType = repayType;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public BorrowerRepayDTO(byte borrowType, byte repayType, String partner, BigDecimal repayAmount) {
		super();
		this.borrowType = borrowType;
		this.repayType = repayType;
		this.partner = partner;
		this.repayAmount = repayAmount;
	}

	@Override
	public String toString() {
		return "{\"borrowType\":\"" + borrowType + "\",\"repayType\":\""
				+ repayType + "\",\"partner\":\"" + partner + "\",\"repayAmount\":\""
				+ repayAmount + "\",\"type\":\"" + type + "\",\"code\":\"" + code
				+ "\",\"desc\":\"" + desc + "\",\"timestamp\":\"" + timestamp
				+ "\"}";
	}

	
}
