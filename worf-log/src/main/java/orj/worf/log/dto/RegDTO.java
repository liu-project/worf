package orj.worf.log.dto;

public class RegDTO extends ServiceLogDTO {

	/**手机号码 */
	private String userPhone; 
	/**注册渠道 */
	private String userFrom; 
	/**用户类型，1：个人用户，2：企业用户*/ 
	private Byte userType; 
	/**注册平台*/ 
	private String platform; 
	/**IP */
	private String ip; 
	/**第三方来源*/ 
	private String requestCode; 
	/**市场使用*/ 
	private String promote; 
	/**渠道 */
	private String channel;
	
	public RegDTO() {
		super();
	}
	public RegDTO(String userPhone, String userFrom, Byte userType,
			String platform, String ip, String requestCode, String promote,
			String channel) {
		super();
		this.userPhone = userPhone;
		this.userFrom = userFrom;
		this.userType = userType;
		this.platform = platform;
		this.ip = ip;
		this.requestCode = requestCode;
		this.promote = promote;
		this.channel = channel;
	}


	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public Byte getUserType() {
		return userType;
	}
	public void setUserType(Byte userType) {
		this.userType = userType;
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
	public String getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}
	public String getPromote() {
		return promote;
	}
	public void setPromote(String promote) {
		this.promote = promote;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@Override
	public String toString() {
		return "{\"userPhone\":\"" + userPhone + "\",\"userFrom\":\""
				+ userFrom + "\",\"userType\":\"" + userType
				+ "\",\"platform\":\"" + platform + "\",\"ip\":\"" + ip
				+ "\",\"requestCode\":\"" + requestCode + "\",\"promote\":\""
				+ promote + "\",\"channel\":\"" + channel + "\",\"type\":\""
				+ type + "\",\"code\":\"" + code + "\",\"desc\":\"" + desc
				+ "\",\"timestamp\":\"" + timestamp + "\"}";
	}
	
	
}
