/**
 * llin 2019年4月3日下午2:17:59
 */
package cn.com.hf.contller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.hf.bean.LoginBean;
import cn.com.hf.contller.utils.DisposePagePostDataUtil;
import cn.com.hf.contller.utils.GetRequestIpAddress;
import cn.com.hf.contller.utils.VerifyLoginUserUtil;
import cn.com.hf.dao.UserInfoDao;
import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.config.VerifyConfig;

/**
 * @author llin
 */
@Controller
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserInfoDao userInfoDao;
	@Autowired
	VerifyLoginUserUtil verifyLoginUserUtil;

	@RequestMapping("/login")
	public String login(ModelMap map) {

		return "login";
	}

	@PostMapping(value = "/login")
	public String login(LoginBean bean, ModelMap mode, HttpServletRequest request, HttpSession session) {
		bean.setLoginIpSite(GetRequestIpAddress.getIPAddress(request));
		logger.info("PostLoginController:" + bean.toString());
		String returnPage = "login";
		String userName = bean.getUserName().trim();
		String passWord = bean.getPassWord().trim();
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord)) {// 判断用户密码是否为空
			
			try {
				DisposePagePostDataUtil.disposePageBean(bean);// 对bean数据进行正则判断处理
				verifyLoginUserUtil.verifyLoginUser(bean);
			} catch (SystemException e) {
				mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, e.getMsg());
				return "login";
			}
			
			// 登录成功更改用户登录数据信息
			// ...
			
			verifyLoginUserUtil.setLoginUser(bean, session);
			
			returnPage = "index";
		}
		return returnPage;
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		verifyLoginUserUtil.removeLoginUser(session);
		return "index";
	}

}
