package orj.worf.authentication.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 需要参与签名校验的参数必须实现此接口
 * @author LiuZhenghua
 * 2018年1月12日 上午11:36:23
 */
public interface BaseRequest extends Serializable {

	/**
	 * 用户uid
	 * @author LiuZhenghua
	 * 2018年1月11日 下午6:02:02
	 */
	public void setUid(Integer uid);
	
	@JsonIgnore
	public Integer getUid();
}
