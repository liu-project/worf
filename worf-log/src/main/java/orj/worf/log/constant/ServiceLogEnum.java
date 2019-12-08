package orj.worf.log.constant;

/**
 * 要记录的日志类型
 * @author LiuZhenghua
 * 2018年4月17日 上午11:45:27
 */
public enum ServiceLogEnum {

	pay_online_need_pay(1, 1001, "待支付"),
	pay_online_paying(1, 1002, "支付中"),
	pay_online_error(1, 1003, "支付错误"),
	pay_online_fail(1, 1004, "支付失败"),
	pay_online_success(1, 1005, "支付成功"),
	pay_online_not_done(1, 1006, "支付未完成"),
	
	push_borrow_sucess(2, 1101, "推标成功"),
	push_borrow_fail(2, 1102, "推标失败"),
	publish_borrow_success(2, 1103, "出标成功"),
	publish_borrow_fail(2, 1104, "出标失败"),
	
	login_success(3, 1201, "登陆成功"),
	login_fail(3, 1202, "登陆失败"),
	reg_sucess(4, 1203, "注册成功"),
	reg_fail(4, 1204, "注册失败"),
	open_account_success(5, 1205, "开户发起成功"),
	open_account_fail(5, 1206, "开户发起失败"),
	open_account_callback(6, 1207, "开户回调"),
	
	withdraw_need_confirm(7, 1301, "待确认"),
	withdraw_accepted(7, 1302, "已受理"),
	withdraw_remitting(7, 1303, "出款中"),
	withdraw_success(7, 1304, "提现成功"),
	withdraw_accept_fail(7, 1305, "受理失败"),
	withdraw_fail(7, 1306, "失败"),
	
	borrower_repay_normal(8,1801,"正常还款"),
	borrower_repay_earlysettle(8,1802,"提前结清"),
	borrower_repay_advance(8,1803,"还代偿款"),
	borrower_repay_partner_advance(8,1804,"合作方代偿"),
	borrower_repay_jsd_normal(8,1805,"周转借正常还款"),
	borrower_repay_jsd_overdue(8,1806,"周转借逾期还款"),
	borrower_repay_jsd_earlysettle(8,1807,"周转借提前结清"),
	;
	
	private Integer type;
	private Integer code;
	private String desc;
	private ServiceLogEnum(Integer type, Integer code, String desc) {
		this.setType(type);
		this.code = code;
		this.desc = desc;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
