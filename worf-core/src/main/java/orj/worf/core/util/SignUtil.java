package orj.worf.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密
 * @author LiuZhenghua
 * 2016年12月5日 下午5:04:05
 */
public class SignUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(SignUtil.class);
	
	private static final String member_money_log_secret = "axwcswdsfwesxzl";
	
	private static final String last_verify_money_log_secret = "1xasfjdio2392x.s2";
	
	/*bug导致没用这个签名，不能改了*/
	//private static final String invest_detail_secret = "xwljelijioj232x.";
	
	private static final String props_secret = "xdfwsderwer23423..x!";
	
	/**
	 * member_moneylog表
	 * @param uid
	 * @param affectMoney
	 * @return
	 * @author LiuZhenghua 2016年12月5日 下午5:12:18
	 */
	public static String getMemberMoneyLogSign(Integer id, BigDecimal affectMoney) {
		String sign = null;
		try {
			sign = MD5Util.md5(MD5Util.md5(member_money_log_secret + id) + affectMoney.setScale(2, RoundingMode.HALF_UP));
		} catch (Exception e) {
			logger.error("------------>获取member_moneylog表sign失败", e);
		}
		return sign;
	}
	
	/**
	 * 获取LastVerifyMoneyLog表的sign
	 * @author LiuZhenghua
	 * 2016年12月14日 上午9:54:52
	 */
	public static String getLastVerifyMoneyLogSign(Integer uid, Integer relateId, BigDecimal accountMoney) {
		String sign = null;
		try {
			sign = MD5Util.md5(MD5Util.md5(last_verify_money_log_secret + uid) + relateId + accountMoney.setScale(2, RoundingMode.HALF_UP));
		} catch (Exception e) {
			logger.error("------------>获取member_moneylog表sign失败", e);
		}
		return sign;
	}
	
	/**
	 * 获取investor_deatail标的sign
	 * @author LiuZhenghua
	 * 2017年2月27日 下午3:57:16
	 */
	public static String getInvestDetailSign(Integer id, BigDecimal capital, BigDecimal interest, Integer deadline) {
		String sign = null;
		try {
			sign = MD5Util.md5(MD5Util.md5(last_verify_money_log_secret + id) + capital.setScale(2, RoundingMode.HALF_UP) 
					+ interest.setScale(2, RoundingMode.HALF_UP) + deadline);
		} catch (Exception e) {
			logger.error("------------>获取investor_deatail表sign失败", e);
		}
		return sign;
	}
	
	/**
	 * 获取道具的sign
	 * @author LiuZhenghua
	 * 2017年4月10日 上午10:08:30
	 */
	public static String getPropsSign(Integer id, Integer uid, Integer expireTime, BigDecimal value, Byte useStatus) {
		String sign = null;
		try {
			sign = MD5Util.md5(MD5Util.md5(props_secret + id) + uid + expireTime + value.setScale(2, RoundingMode.HALF_UP) 
					+ useStatus);
		} catch (Exception e) {
			logger.error("------------>获取props sign失败", e);
		}
		return sign;
	}
	
	public static void main(String[] args) {
		System.out.println(getMemberMoneyLogSign(6196248, new BigDecimal(4.5)));
	}
}
