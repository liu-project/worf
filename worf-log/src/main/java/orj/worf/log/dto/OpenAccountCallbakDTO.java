package orj.worf.log.dto;

import java.math.BigDecimal;

/**
 * 开户回调
 * @author LiuZhenghua
 * 2018年4月19日 下午4:34:46
 */
public class OpenAccountCallbakDTO extends ServiceLogDTO {

	/**用户编号*/ 
	private Integer uid; 
	/**Y S 平台用户编号*/ 
	private String platformUserNo;
	/**Y E 证件类型 {@link IDTypeEnum}*/ 
	private String idCardType;
	/**Y E 银行编码 {@link BankCodeEnum}*/ 
	private String bankcode;
	/**Y E 鉴权通过类型 {@link AccessTypeEnum}*/ 
	private String accessType;
	/**Y E 审核状态 {@link AuditStatusEnum}*/ 
	private String auditStatus;
	/**N S 集团账户编号*/ 
	private String groupAccountNO;
	/** N A 授权金额 */ 
	private BigDecimal amount;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getPlatformUserNo() {
		return platformUserNo;
	}
	public void setPlatformUserNo(String platformUserNo) {
		this.platformUserNo = platformUserNo;
	}
	public String getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getGroupAccountNO() {
		return groupAccountNO;
	}
	public void setGroupAccountNO(String groupAccountNO) {
		this.groupAccountNO = groupAccountNO;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public OpenAccountCallbakDTO(Integer uid, String platformUserNo,
			String idCardType, String bankcode, String accessType,
			String auditStatus, String groupAccountNO, BigDecimal amount) {
		super();
		this.uid = uid;
		this.platformUserNo = platformUserNo;
		this.idCardType = idCardType;
		this.bankcode = bankcode;
		this.accessType = accessType;
		this.auditStatus = auditStatus;
		this.groupAccountNO = groupAccountNO;
		this.amount = amount;
	}
	public OpenAccountCallbakDTO() {
		super();
	}
	@Override
	public String toString() {
		return "{\"uid\":\"" + uid + "\",\"platformUserNo\":\""
				+ platformUserNo + "\",\"idCardType\":\"" + idCardType
				+ "\",\"bankcode\":\"" + bankcode + "\",\"accessType\":\""
				+ accessType + "\",\"auditStatus\":\"" + auditStatus
				+ "\",\"groupAccountNO\":\"" + groupAccountNO
				+ "\",\"amount\":\"" + amount + "\",\"type\":\"" + type
				+ "\",\"code\":\"" + code + "\",\"desc\":\"" + desc
				+ "\",\"timestamp\":\"" + timestamp + "\"}";
	}

}
