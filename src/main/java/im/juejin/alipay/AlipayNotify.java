package im.juejin.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.juejin.alipay.sign.RSA;

/*
 * * 类名：AlipayNotify 功能：支付宝通知处理类 详细：处理支付宝各接口通知返回
 */
public class AlipayNotify {
  private static final Logger LOGGER = LoggerFactory.getLogger(AlipayNotify.class);
  /**
   * 支付宝消息验证地址
   */
  private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";
  
  private static final String PAY_STATUS_SUCCESS = "TRADE_SUCCESS";
  private static final String PAY_STATUS_FINISH = "TRADE_FINISHED";

  /**
   * 验证消息是否是支付宝发出的合法消息
   * @param params 通知返回来的参数数组
   * @return 验证结果
   */
  public static boolean verify(Map<String, String[]> parameterMap) {
    Map<String, String> params = convertParams(parameterMap);
    // LOGGER.info(mapToString(params));
    // 判断responsetTxt是否为true，isSign是否为true
    // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
    // isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    String responseTxt = "true";
    if (params.get("notify_id") != null) {
      String notify_id = params.get("notify_id");
      responseTxt = verifyResponse(notify_id);
    }
    String sign = "";
    if (params.get("sign") != null) {
      sign = params.get("sign");
    }
    String tradeStatus = "";
    if (params.get("trade_status") != null) {
      tradeStatus = params.get("trade_status");
      LOGGER.info("tradeStatus："+tradeStatus);
    }
    boolean isSign = getSignVeryfy(params, sign);
    LOGGER.info("isSign："+isSign+",responseTxt="+responseTxt);
    if (isSign && responseTxt.equals("true") && (tradeStatus.equals(PAY_STATUS_SUCCESS) || tradeStatus.equals(PAY_STATUS_FINISH))) {
      return true;
    } else {
      return false;
    }
  }
  
//  private static String mapToString(Map<String, String> params){
//    StringBuffer sbSql = new StringBuffer();
//    for(Entry<String, String> entry : params.entrySet()){
//      sbSql.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
//    }
//    return sbSql.toString();
//  }

  /**
   * 将ServletRequest中ParameterMap的格式进行转换
   * @param parameterMap
   * @return
   */
  private static Map<String, String> convertParams(Map<String, String[]> parameterMap) {
    // 获取支付宝GET过来反馈信息
    Map<String, String> params = new HashMap<String, String>();
    for (Iterator<String> iter = parameterMap.keySet().iterator(); iter.hasNext();) {
      String name = (String) iter.next();
      String[] values = (String[]) parameterMap.get(name);
      String valueStr = "";
      for (int i = 0; i < values.length; i++) {
        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
      }
      // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
      // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
      params.put(name, valueStr);
    }
    return params;
  }

  /**
   * 根据反馈回来的信息，生成签名结果
   * @param Params 通知返回来的参数数组
   * @param sign 比对的签名结果
   * @return 生成的签名结果
   */
  private static boolean getSignVeryfy(Map<String, String> Params, String sign) {
    // 过滤空值、sign与sign_type参数
    Map<String, String> sParaNew = ThirdpayUtils.paraFilter(Params);
    // 获取待签名字符串
    String preSignStr = ThirdpayUtils.createLinkString(sParaNew);
    // 获得签名验证结果
    boolean isSign = false;
    if (AlipayConfig.SIGN_TYPE.equals("RSA")) {
      isSign = RSA.verify(preSignStr, sign, AlipayConfig.ALI_PUBLIC_KEY, AlipayConfig.INPUT_CHARSET);
    }
    return isSign;
  }

  /**
   * 获取远程服务器ATN结果,验证返回URL
   * @param notify_id 通知校验ID
   * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true 返回正确信息 false
   *         请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
   */
  private static String verifyResponse(String notify_id) {
    // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
    String partner = AlipayConfig.PARTNER;
    String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
    return checkUrl(veryfy_url);
  }

  /**
   * 获取远程服务器ATN结果
   * @param urlvalue 指定URL路径地址
   * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true 返回正确信息 false
   *         请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
   */
  private static String checkUrl(String urlvalue) {
    String inputLine = "";
    BufferedReader in = null;
    try {
      URL url = new URL(urlvalue);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      inputLine = in.readLine();
    } catch (Exception e) {
      LOGGER.warn("", e);
      inputLine = "";
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return inputLine;
  }
}
