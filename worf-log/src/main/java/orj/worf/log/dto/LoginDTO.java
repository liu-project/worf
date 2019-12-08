package orj.worf.log.dto;

/**
 * 登陆
 * @author LiuZhenghua
 * 2018年4月19日 下午3:13:36
 */
public class LoginDTO extends ServiceLogDTO {

	/**用户名或手机号码*/ 
	private String userName; 
	/**手机号码 */
	private String userPhone;
	/**平台类型 android, ios, wap, web*/ 
	private String platform; 
	/**IP地址 */
	private String ip;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public LoginDTO(String userName, String userPhone, String platform,
			String ip) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.platform = platform;
		this.ip = ip;
	}
	public LoginDTO() {
		
	}
	@Override
	public String toString() {
		return "{\"userName\":\"" + userName + "\",\"userPhone\":\""
				+ userPhone + "\",\"platform\":\"" + platform + "\",\"ip\":\""
				+ ip + "\",\"type\":\"" + type + "\",\"code\":\"" + code
				+ "\",\"desc\":\"" + desc + "\",\"timestamp\":\"" + timestamp
				+ "\"}";
	}
	
}
