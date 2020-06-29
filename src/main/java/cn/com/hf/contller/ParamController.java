/**  
 * All rights Reserved, Designed By www.tydic.com
 * @Title: ParamController.java   
 * @Package: cn.com.hf.contller   
 * @Description: 
 * @author: wangtao 
 * @date: 2019年11月14日 上午10:50:04
 */
package cn.com.hf.contller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.hf.contller.utils.ParamUtil;
import cn.com.hf.service.FileUtils;
import cn.com.hf.service.MailUtil;
import cn.com.hf.verify.config.VerifyConfig;

/**
 * @ClassName: ParamController
 * @Description:
 * @author: wangtao
 * @date: 2019年11月14日 上午10:50:04
 */
@Controller
public class ParamController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParamUtil paramUtil;

	@RequestMapping("/param")
	public String param(ModelMap mode, HttpServletRequest request) {
		String session_loginUser = (String) request.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		if (StringUtils.isEmpty(session_loginUser)) {
			mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
			return "index";
		}
		return "param";
	}

	@RequestMapping("/getParam")
	@ResponseBody
	public Map<String, String> getParam(HttpServletRequest req) throws Exception {
		String chk = req.getParameter("chk");
		String mername = req.getParameter("mername");
		String email = req.getParameter("email");
		System.out.println("mername=" + mername);
		System.out.println("email=" + email);
		if (StringUtils.isBlank(mername)) {
			logger.info("商户名为空");
			return null;
		}
		String username = (String) req.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		Map<String, String> map = paramUtil.getParam(chk, username, mername);
		if (StringUtils.isNotBlank(email)) {
			try {
				new Thread() {
					public void run() {
						// 获取文件路径
						String filepath = "";
						try {
							filepath = FileUtils.getFilePath(map);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 发送邮件
						MailUtil.send(email, filepath, mername);
						// 删除文件
						File file = new File(filepath);
						if (file.exists()) {
							file.delete();
						}
					}
				}.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		String str = paramUtil.getSequence();
		return str;
	}
}
