package orj.worf.authentication.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public interface Request extends BaseRequest {
    /**
     * 平台类型 android/ios/web
     * @author LiuZhenghua
     * 2018年1月11日 下午5:58:37
     */
    public String getPlatform();
    
    /**
     * 签名信息
     * @author LiuZhenghua
     * 2018年1月11日 下午5:59:10
     */
    public String getSign();
	
	public String getSession_token();
    
	/**
	 * 获取app版本号
	 * @author LiuZhenghua
	 * 2019年8月6日 上午11:36:05
	 */
	public String getApp_version();
}
