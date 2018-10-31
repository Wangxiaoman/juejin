package im.juejin.alipay;

/* *
 * 
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径等
 */

public class AlipayConfig {

	// 签名方式 不能修改 移动支付不能使用MD5
	public final static String SIGN_TYPE = "RSA";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public final static String INPUT_CHARSET = "utf-8";

	// 支付宝的公钥，无需修改该值，只有重新上传过RSA私钥和公钥之后，更新该值
	public final static String ALI_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public final static String PARTNER = "2088221500684729";

	// 收款支付宝账号
	public final static String SELLER_ID = "xxx";

	// 商户的私钥
	public final static String KEY = "xxx";

	// 移动支付页面跳转同步通知页面路径，默认支付完成3秒后自动跳转到此页面
	public final static String MOBILE_PAY_RETURN_URL = "";

	// 移动支付服务器异步通知页面路径
	public final static String MOBILE_PAY_NOTIFY_URL = "/alipay/notify";

	// 即时到帐页面跳转同步通知页面路径，默认支付完成3秒后自动跳转到此页面
	public final static String DIRECT_PAY_RETURN_URL = "";

	// 即时到帐服务器异步通知页面路径
	public final static String DIRECT_PAY_NOTIFY_URL = "";

}
