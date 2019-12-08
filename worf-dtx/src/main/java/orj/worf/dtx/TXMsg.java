/*******************************************************************************
 * @Title: TxMsg.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import java.util.Map;

/**   
 * @Title: TxMsg.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年6月7日 下午2:11:36
 * @version V1.0   
 */
public class TXMsg {

	private String ltid;
	
	private String tid;
	
	private String bid;
	
	private Map<String,String> paramMap;
	
	private int state = 0;
	
	private boolean rollback;
	
	private int sendTime;
	
	private int sendTimes;

	/**
	 * @return the ltid
	 */
	
	public String getLtid() {
		return ltid;
	}

	/**
	 * @param ltid the ltid to set
	 */
	public void setLtid(String ltid) {
		this.ltid = ltid;
	}

	/**
	 * @return the tid
	 */
	
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the bid
	 */
	
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the paramMap
	 */
	
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	/**
	 * @param paramMap the paramMap to set
	 */
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	/**
	 * @return the state
	 */
	
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the rollback
	 */
	
	public boolean isRollback() {
		return rollback;
	}

	/**
	 * @param rollback the rollback to set
	 */
	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}

	/**
	 * @return the sendTime
	 */
	
	public int getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the sendTimes
	 */
	
	public int getSendTimes() {
		return sendTimes;
	}

	/**
	 * @param sendTimes the sendTimes to set
	 */
	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}
	
}
