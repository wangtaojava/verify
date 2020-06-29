/**
 * llin 2019年6月28日上午11:34:49
 */
package cn.com.hf.contller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.hf.contller.utils.GetRequestIpAddress;
import cn.com.hf.contller.utils.PayUtils;
import cn.com.hf.contller.utils.VerifyLoginUserUtil;
import cn.com.hf.dao.UserInfoDao;
import cn.com.hf.verify.config.VerifyConfig;

/**
 * @author llin
 */
@Controller
public class PayController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserInfoDao userInfoDao;
	@Autowired
	VerifyLoginUserUtil verifyLoginUserUtil;
	@Autowired
	PayUtils payUtils;

	@RequestMapping("/pay")
	public String register(ModelMap map) {
		return "pay";
	}

	@PostMapping(value = "/pay")
	public void login(ModelMap mode, HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		String session_loginUser = (String) session.getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		String ipAddress = GetRequestIpAddress.getIPAddress(request);
		
		logger.info(ipAddress + " - " + session_loginUser);

		// /*
		// * 判断用户是否登录
		// */
		// if(StringUtils.isBlank(session_loginUser)){
		// mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
		// return "login";
		// }else{
		// try {
		// verifyLoginUserUtil.verifyLoginUser(session_loginUser, ipAddress);
		// } catch (Exception e) {
		// if(e instanceof SystemException)
		// mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG,
		// ((SystemException)e ).getMsg());
		// return "login";
		// }
		// }

		String userAgent = request.getHeader("user-agent").toLowerCase();
		String orderType = judgeUserAgent(userAgent);

		String orderAmt = ((int)(1+Math.random()*(100-1+1))) + "";// 随机金额(分)
		
		// 请求统一下单
		String payUrl = payUtils.pay(orderType, orderAmt);
		if (StringUtils.isNotBlank(payUrl))
			response.sendRedirect(payUrl);
		else
			return ;
	}

	private static String judgeUserAgent(String userAgent) {
		String opt = StringUtils.EMPTY;
		if (userAgent.toLowerCase().indexOf("micromessenger") >= 0) {
			// 微信
			// opt = "wxPreOrder";
		} else if (userAgent.toLowerCase().indexOf("alipayclient") >= 0) {
			// 支付宝
			opt = "apPreOrder";
		} else if (userAgent.toLowerCase().indexOf("unionpay") >= 0) {
			// 银联二维码UnionPay/<版本号><App标识>
			// opt = "upPreOrder";
		} else {
		}
		return opt;
	}

}
