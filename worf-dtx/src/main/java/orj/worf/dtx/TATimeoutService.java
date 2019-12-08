/*******************************************************************************
 * @Title: TATimeoutService.java
 *
 * @Copyright (c) 2017 深圳前海融金所互联网金融服务有限公司 版权所有. 粤ICP备13026617号
 * 注意：本内容仅限于深圳前海融金所互联网金融服务有限公司 内部传阅，禁止外泄以及用于其他商业目的!
 ******************************************************************************/
package orj.worf.dtx;

import org.springframework.scheduling.annotation.Async;

/**   
 * @Title: TATimeoutService.java
 * @Description: TODO
 * @author zouxuejun
 * @date 2017年5月24日 下午5:12:18
 * @version V1.0   
 */
public interface TATimeoutService {

    @Async
    void checkTAWillTimeout();
    
    
}
