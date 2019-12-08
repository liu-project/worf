package orj.worf.log.dto;

/**
 * 开户
 * @author LiuZhenghua
 * 2018年4月19日 下午3:53:12
 */
public class OpenAccountDTO extends ServiceLogDTO {

	/** 用户编号*/ 
	private Integer uid; 
	/** 用户角色 
	private Byte userRole; 
	/** IP地址 */
	private String ip; 
	/** 开户类型，1：普通用户开户，2：合作方开户 */
	private Byte flag; 
	/**是否开通集团账户 0未开通 1开通 */
	private Byte isOpGroupAccount;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Byte getFlag() {
		return flag;
	}
	public void setFlag(Byte flag) {
		this.flag = flag;
	}
	public Byte getIsOpGroupAccount() {
		return isOpGroupAccount;
	}
	public void setIsOpGroupAccount(Byte isOpGroupAccount) {
		this.isOpGroupAccount = isOpGroupAccount;
	}
	public OpenAccountDTO(Integer uid, String ip, Byte flag,
			Byte isOpGroupAccount) {
		super();
		this.uid = uid;
		this.ip = ip;
		this.flag = flag;
		this.isOpGroupAccount = isOpGroupAccount;
	}
	public OpenAccountDTO() {
		super();
	}
	@Override
	public String toString() {
		return "{\"uid\":\"" + uid + "\",\"ip\":\"" + ip + "\",\"flag\":\""
				+ flag + "\",\"isOpGroupAccount\":\"" + isOpGroupAccount
				+ "\",\"type\":\"" + type + "\",\"code\":\"" + code
				+ "\",\"desc\":\"" + desc + "\",\"timestamp\":\"" + timestamp
				+ "\"}";
	}
	
}
