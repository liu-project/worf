/*******************************************************************************
 * @Title: TAManager.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

/**   
 * @Title: TAManager.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年6月7日 下午3:42:15
 * @version V1.0   
 */
public class TAManager {

	private boolean master;
	
	private DTXMsgSender taMsgSender;
	
	private DTXService dtxService;

	/**
	 * @return the master
	 */
	
	public boolean isMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}

	
	
	public DTXService getTaService() {
		return dtxService;
	}

	/**
	 * @param taService the taService to set
	 */
	public void setTaService(DTXService taService) {
		this.dtxService = taService;
	}

	/**
	 * @return the taMsgSender
	 */
	
	public DTXMsgSender getTaMsgSender() {
		return taMsgSender;
	}

	/**
	 * @param taMsgSender the taMsgSender to set
	 */
	public void setTaMsgSender(DTXMsgSender taMsgSender) {
		this.taMsgSender = taMsgSender;
	}
	
	
	
}
