package orj.worf.log.test;

import java.math.BigDecimal;

import org.junit.Test;

import orj.worf.log.constant.ServiceLogEnum;
import orj.worf.log.dto.BorrowPushDTO;
import orj.worf.log.dto.LoginDTO;
import orj.worf.log.dto.OpenAccountCallbakDTO;
import orj.worf.log.dto.OpenAccountDTO;
import orj.worf.log.dto.PayOnlineDTO;
import orj.worf.log.dto.RegDTO;
import orj.worf.log.util.ServiceLogUtil;

public class ServiceLogUtilTest {


	@Test
	public void testInfo() {
//		ServiceLogUtil.info(ServiceLogEnum.pay_online_success, new BigDecimal("50"));
		ServiceLogUtil.info(ServiceLogEnum.pay_online_success, new PayOnlineDTO(new BigDecimal("50")));
		ServiceLogUtil.info(ServiceLogEnum.push_borrow_sucess, new BorrowPushDTO("201", "20101", "rjstzgl", new BigDecimal("180000")));
		ServiceLogUtil.info(ServiceLogEnum.push_borrow_fail, new BorrowPushDTO("201", "20101", "rjstzgl", new BigDecimal("150000")));
		
		LoginDTO loginDTO = new LoginDTO("jackr1", "13811112222", "web", "127.0.0.1");
		RegDTO regDTO = new RegDTO("13811112222", "ysw", (byte)1, "ios", "127.0.0.1", "xxxxx", "promote", "baidu");
		OpenAccountDTO openAccountDTO = new OpenAccountDTO(123, "127.0.0.1", (byte)0, (byte)0);
		OpenAccountCallbakDTO openAccountCallbakDTO = new OpenAccountCallbakDTO(123,"RJS4400100123", "1", "6226622322316414", "1", "2", "RJS4401", new BigDecimal("120000"));
		ServiceLogUtil.info(ServiceLogEnum.login_success, loginDTO);
		ServiceLogUtil.info(ServiceLogEnum.login_fail, loginDTO);
		ServiceLogUtil.info(ServiceLogEnum.reg_sucess, regDTO);
		ServiceLogUtil.info(ServiceLogEnum.reg_fail, regDTO);
		ServiceLogUtil.info(ServiceLogEnum.open_account_success, openAccountDTO);
		ServiceLogUtil.info(ServiceLogEnum.open_account_fail, openAccountDTO);
		ServiceLogUtil.info(ServiceLogEnum.open_account_callback, openAccountCallbakDTO);
		
	}
}
