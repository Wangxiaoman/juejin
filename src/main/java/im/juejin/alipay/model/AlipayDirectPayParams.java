package im.juejin.alipay.model;

/* *
 *类名：AlipayDirectPayParams
 *功能：即时到帐请求参数
 */
public class AlipayDirectPayParams {

	// 接口名称
	private String service = "create_direct_pay_by_user";

	// 支付类型
	private String paymentType = "1"; // 1：商品购买，4：捐赠，47：电子卡券

	// 付款金额 必填>0
	private String totalFee;

	// 商户网站订单系统中唯一订单号 必填
	private String outTradeNo;

	// 商品名称 必填
	private String subject;

	// 商品详情
	private String body;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
