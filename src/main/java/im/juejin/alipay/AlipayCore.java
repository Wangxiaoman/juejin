package im.juejin.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.juejin.alipay.model.AlipayDirectPayParams;
import im.juejin.alipay.model.AlipayMobilePayParams;
import im.juejin.alipay.sign.MD5;
import im.juejin.alipay.sign.RSA;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 */

public class AlipayCore {

	private static Logger logger = LoggerFactory.getLogger(AlipayCore.class);
	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	/**
	 * 即时到帐支付
	 * 
	 * @param payRequest
	 * @return
	 */
	public static String directPay(AlipayDirectPayParams payRequest) {
		logger.info("AlipaySubmit:directPay:out_trade_no="
				+ payRequest.getOutTradeNo() + ",total_fee="
				+ payRequest.getTotalFee() + ",seller_id="
				+ AlipayConfig.SELLER_ID);
		Map<String, String> sParaTemp = buildDirectPayParams(payRequest);
		return buildRequest(sParaTemp);
	}

	private static Map<String, String> buildDirectPayParams(
			AlipayDirectPayParams payRequest) {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", payRequest.getService());
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("seller_email", AlipayConfig.SELLER_ID);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("payment_type", payRequest.getPaymentType());
		sParaTemp.put("return_url", AlipayConfig.DIRECT_PAY_RETURN_URL);
		sParaTemp.put("notify_url", AlipayConfig.DIRECT_PAY_NOTIFY_URL);
		sParaTemp.put("out_trade_no", payRequest.getOutTradeNo());
		sParaTemp.put("subject", payRequest.getSubject());
		sParaTemp.put("body", payRequest.getBody());
		sParaTemp.put("total_fee", payRequest.getTotalFee());
		return sParaTemp;
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认），输出到页面后会直接提交请求到支付宝
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	private static String buildRequest(Map<String, String> sParaTemp) {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
				+ ALIPAY_GATEWAY_NEW
				+ "_input_charset="
				+ AlipayConfig.INPUT_CHARSET + "\" method=\"" + "get" + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + "确定"
				+ "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	private static String buildRequestMysign(Map<String, String> sPara) {
		String prestr = ThirdpayUtils.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		if (AlipayConfig.SIGN_TYPE.equals("RSA")) {
			mysign = RSA.sign(prestr, AlipayConfig.KEY,
					AlipayConfig.INPUT_CHARSET);
		}
		if (AlipayConfig.SIGN_TYPE.equals("MD5")) {
			mysign = MD5.sign(prestr, AlipayConfig.KEY,
					AlipayConfig.INPUT_CHARSET);
		}
		return mysign;
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	private static Map<String, String> buildRequestPara(
			Map<String, String> sParaTemp) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = ThirdpayUtils.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", AlipayConfig.SIGN_TYPE);

		return sPara;
	}

	/**
	 * 移动支付：将传过来的参数构造成支付宝请求链接，包括加密
	 * 
	 * @param params
	 * @return app sdk的pay接口参数
	 */
	public static String buildMobilePayinfo(AlipayMobilePayParams params) {
		String orderInfo = buildOrderInfo(params);
		String sign = sign(orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(
					"AlipaySubmit:buildPayinfo error,outTradeNo="
							+ params.getOutTradeNo() + ",totalFee="
							+ params.getTotalFee(), e);
			throw new RuntimeException("构造支付信息错误");
		}
		final String payInfo = orderInfo + "&sign=\"" + sign
				+ "\"&sign_type=\"" + AlipayConfig.SIGN_TYPE + "\"";
		logger.info("AlipaySubmit:buildMobilePayinfo success,out_trade_no="
				+ params.getOutTradeNo() + ",total_fee=" + params.getTotalFee()
				+ ",seller_id=" + AlipayConfig.SELLER_ID);
		return payInfo;
	}

	private static String buildOrderInfo(AlipayMobilePayParams params) {
		StringBuffer sb = new StringBuffer();
		sb.append("partner=\"").append(AlipayConfig.PARTNER).append("\"");
		sb.append("&seller_id=\"").append(AlipayConfig.SELLER_ID).append("\"");
		sb.append("&out_trade_no=\"").append(params.getOutTradeNo())
				.append("\"");
		sb.append("&subject=\"").append(params.getSubject()).append("\"");
		sb.append("&body=\"").append(params.getBody()).append("\"");

		sb.append("&total_fee=\"").append(params.getTotalFee()).append("\"");
		sb.append("&notify_url=\"").append(AlipayConfig.MOBILE_PAY_NOTIFY_URL)
				.append("\"");
		sb.append("&service=\"").append(params.getService()).append("\"");
		sb.append("&payment_type=\"").append(params.getPaymentType())
				.append("\"");
		sb.append("&_input_charset=\"").append(AlipayConfig.INPUT_CHARSET)
				.append("\"");
		sb.append("&it_b_pay=\"").append(params.getItBPay()).append("\"");
//		sb.append("&return_url=\"").append(AlipayConfig.MOBILE_PAY_RETURN_URL)
//				.append("\"");
		return sb.toString();
	}

	private static String sign(String orderInfo) {
		return RSA
				.sign(orderInfo, AlipayConfig.KEY, AlipayConfig.INPUT_CHARSET);
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * 
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	// public static String query_timestamp(AlipayConfig config)
	// throws MalformedURLException, DocumentException, IOException {
	//
	// // 构造访问query_timestamp接口的URL串
	// String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner="
	// + config.partner + "&_input_charset" + config.inputCharset;
	// StringBuffer result = new StringBuffer();
	//
	// SAXReader reader = new SAXReader();
	// Document doc = reader.read(new URL(strUrl).openStream());
	//
	// List<Node> nodeList = doc.selectNodes("//alipay/*");
	//
	// for (Node node : nodeList) {
	// // 截取部分不需要解析的信息
	// if (node.getName().equals("is_success")
	// && node.getText().equals("T")) {
	// // 判断是否有成功标示
	// List<Node> nodeList1 = doc
	// .selectNodes("//response/timestamp/*");
	// for (Node node1 : nodeList1) {
	// result.append(node1.getText());
	// }
	// }
	// }
	//
	// return result.toString();
	// }
}
