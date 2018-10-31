package im.juejin.alipay.model;

public class AlipayMobilePayParams {
	// 接口名称，不需要修改
	private String service = "mobile.securitypay.pay";

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

	/**
	 * 设置未付款交易的超时时间,取值范围：1m～15d. m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）
	 * 该参数数值不接受小数点，如1.5h，可转换为90m
	 */
	private String itBPay = "30m";

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

	public String getItBPay() {
		return itBPay;
	}

	public void setItBPay(String itBPay) {
		this.itBPay = itBPay;
	}

}
