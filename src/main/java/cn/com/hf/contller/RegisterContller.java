/**
 * llin 2019年4月10日下午9:49:09
 */
package cn.com.hf.contller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.hf.bean.RegisterBean;
import cn.com.hf.contller.utils.DisposePagePostDataUtil;
import cn.com.hf.contller.utils.GetRequestIpAddress;
import cn.com.hf.dao.UserDao;
import cn.com.hf.dao.UserInfoDao;
import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.config.VerifyConfig;

/**
 * @author llin
 * 注册
 */
@Controller
public class RegisterContller {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserDao userDao;
	@Autowired
	UserInfoDao userInfoDao;
	
	@RequestMapping("/register")
	public String register(ModelMap mode) {
		return "register";
	}
	
	@PostMapping(value = "/register")
	public String register(RegisterBean bean, ModelMap mode, HttpServletRequest request, HttpSession session) {
		String reqIp = GetRequestIpAddress.getIPAddress(request);
		logger.info(reqIp + " - " + bean.toString());
		
		if(bean==null)
			return "register";
		
		// 对注册数据进行正则判断，或者过滤，防止SQL注入
		try {
			DisposePagePostDataUtil.disposePageBean(bean);
		} catch (SystemException e) {
			mode.addAttribute(VerifyConfig.SYSTEM_REGISTER_ERRORMSG, e.getMsg());
			bean = null;
			return "register";
		}
		
		String userName = bean.getUserName().trim();
		
		// 判断用户是否存在
		boolean insertUserFlag = userInfoDao.selectUserInfoManage(userName);
		if(insertUserFlag){
			mode.addAttribute(VerifyConfig.SYSTEM_REGISTER_ERRORMSG, "用户已存在");
			bean = null;
			return "register";
		}
		
		String passWord = bean.getPassWord().trim();
		String passWord2 = bean.getPassWord2().trim();
		if(!passWord.equals(passWord2)){
			mode.addAttribute(VerifyConfig.SYSTEM_REGISTER_ERRORMSG, "密码不一致,请重新输入!");
			bean = null;
			return "register";
		}
			
		String userPhone = bean.getUserPhone().trim();
		// 将数据存入数据库中
		try {
			userInfoDao.insertUserInfoManage(userName, passWord, userPhone);
			userDao.insertUserManage(userName, reqIp);
		} catch (SystemException e) {
			mode.addAttribute(VerifyConfig.SYSTEM_REGISTER_ERRORMSG, e.getMsg());
			bean = null;
			return "register";
		}
		
		return "login";
	}

}
