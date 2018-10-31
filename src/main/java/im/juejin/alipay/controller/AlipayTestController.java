package im.juejin.alipay.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import im.juejin.alipay.AlipayCore;
import im.juejin.alipay.AlipayNotify;
import im.juejin.alipay.model.AlipayDirectPayParams;
import im.juejin.alipay.model.AlipayMobilePayParams;

/**
 * 测试类，开发时可参考
 * https://docs.open.alipay.com/54/106370/
 * https://zhuanlan.zhihu.com/p/33548735
 * @author zhangjiayu
 *
 */
@Controller
@RequestMapping("alipay")
public class AlipayTestController {
	private static Logger logger = LoggerFactory.getLogger(AlipayTestController.class);

	/**
	 * 移动支付：将传过来的参数构造成支付宝请求链接，包括加密
	 *  
	 * 业务逻辑在调用此之前或调用支付宝接口之前处理，比如生成第三方支付记录
	 * 
	 * @param params
	 * @return app sdk的pay接口参数
	 */
	@RequestMapping("/mobile/buildPayinfo")
	@ResponseBody
	public String buildMobilePayinfo(AlipayMobilePayParams params) {
		// TODO 必填参数检测
		return AlipayCore.buildMobilePayinfo(params);
	}

	/**
	 * PC和wap即时到帐接口
	 * 
	 * @param params
	 * @param model
	 * @return
	 */
	@RequestMapping("/direct/pay")
	public String directPay(AlipayDirectPayParams params, Model model) {
		// TODO 必填参数检测
		String alipayHtml = AlipayCore.directPay(params);
		model.addAttribute("alipayHtml", alipayHtml);
		return "alipay/pay";
	}

	/**
	 * 支付宝支付异步通知接口，支付成功后，支付宝会请求此接口，所以要保证外网能够访问到
	 * 处理成功后，回写"success"，失败"fail"，不能为其他值，如果为fail，则支付宝会重复请求（中间会间隔一定时间）
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/notify")
	public void payNotify(ServletRequest request, ServletResponse response,
			Model model) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		try {
			if (CollectionUtils.isEmpty(parameterMap)) {
				logger.error("alipay notify error,parameterMap is empty");
				response.getWriter().println("fail");
			}
			boolean result = AlipayNotify.verify(parameterMap);
			logger.info("verify result=" + result);
			if (result) {
				// TODO 在此处添加支付成功后的业务代码
				response.getOutputStream().println("success");
			} else {
				response.getOutputStream().println("fail");
			}
		} catch (IOException e) {
			logger.error("alipay notify error:", e);
		}

	}

}
