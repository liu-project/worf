package orj.worf.redis.util;


public class TimedValue {

	private long expiredTime = -1;
	
	private String value;

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
