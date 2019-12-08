package orj.worf.log.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import orj.worf.log.adapter.ServiceLogAdapter;
import orj.worf.log.constant.ServiceLogEnum;
import orj.worf.log.dto.PayOnlineDTO;
import orj.worf.log.dto.ServiceLogDTO;

/**
 * 打印关键service的日志，便于流计算
 * @author LiuZhenghua
 * 2018年4月17日 上午11:43:16
 */
public class ServiceLogUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ServiceLogUtil.class);
	
	public static void info(ServiceLogEnum serviceLogEnum, ServiceLogDTO msg) {
		msg.init(serviceLogEnum);
		info(msg);
	}
	
	/**
	 * 充值
	 * @deprecated {@link ServiceLogUtil#info(ServiceLogEnum, PayOnlineDTO)}
	 * @author LiuZhenghua
	 * 2018年4月17日 上午11:49:24
	 */
	@Deprecated
	public static void info(ServiceLogEnum serviceLogEnum, BigDecimal money) {
		PayOnlineDTO moneyServiceLogDTO = new PayOnlineDTO(money);
		moneyServiceLogDTO.init(serviceLogEnum);
		info(moneyServiceLogDTO);
	}
	
	private static void info(ServiceLogDTO msg) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new ServiceLogAdapter(logger, msg.toString()));
		} else {
			logger.info(msg.toString());
		}
	}
	
}
