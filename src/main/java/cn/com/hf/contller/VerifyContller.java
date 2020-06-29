/**
 * llin 2019年3月6日下午4:07:36
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

import cn.com.hf.bean.ResepVerifyBean;
import cn.com.hf.bean.VerifyBean;
import cn.com.hf.contller.utils.DisposePagePostDataUtil;
import cn.com.hf.contller.utils.GetRequestIpAddress;
import cn.com.hf.contller.utils.VerifyLoginUserUtil;
import cn.com.hf.dao.TradeTraceSeqDao;
import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.BankVerify;
import cn.com.hf.verify.VerifyEntInfos;
import cn.com.hf.verify.VerifyOperator;
import cn.com.hf.verify.VerifyPolice;
import cn.com.hf.verify.config.VerifyConfig;
import cn.com.hf.verify.tools.RespVerifyMsgTool;
import cn.com.hf.verify.tools.TimeTool;

/**
 * @author llin
 */
@Controller
public class VerifyContller {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	RespVerifyMsgTool respVerifyMsgTool;
	@Autowired
	BankVerify bankVerify;
	@Autowired
	VerifyOperator verifyOperator;
	@Autowired
	VerifyPolice verifyPolice;
	@Autowired
	VerifyEntInfos verifyEntInfos;
	@Autowired
	TradeTraceSeqDao tradeTraceSeqDao;
	@Autowired
	VerifyLoginUserUtil verifyLoginUserUtil;
	
	
	@PostMapping(value = "/verify")
	public String verify(VerifyBean bean, ModelMap mode, HttpServletRequest request, HttpSession session) {
		String session_loginUser = (String) session.getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		String ipAddress = GetRequestIpAddress.getIPAddress(request);
		
		logger.info(ipAddress + " - " + session_loginUser + ":" + bean.toString());
		
		/*
		 * 判断用户是否登录
		 */
		if(StringUtils.isBlank(session_loginUser)){
			mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
			return "login";
		}else{
			try {
				verifyLoginUserUtil.verifyLoginUser(session_loginUser, ipAddress);
			} catch (Exception e) {
				if(e instanceof SystemException)
					mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, ((SystemException)e ).getMsg());
				return "login";
			}
		}
		
		// 对请求鉴权数据过正则判断，或者做简单的过滤，防止SQL注入
		try {
			DisposePagePostDataUtil.disposePageBean(bean);
		} catch (SystemException e) {
			mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, e.getMsg());
			return "verifyResult";
		}
		
		
		// 根据请求的鉴权类型，做相应的鉴权处理
		String verifyMsg = postVerify(bean, session_loginUser);
		
		ResepVerifyBean respBean = disposeVerifyBean2ResepVerifyBean(bean, verifyMsg);
		mode.addAttribute("resepVerifyBean", respBean);
		return "verifyResult";
	}
	
	private String postVerify(VerifyBean bean, String loginUserName){
		String accName = bean.getAccName().trim();
		String accNo = bean.getAccNo().trim();
		String certNo = bean.getCertNo().trim();
		String phoneNo = bean.getPhoneNo().trim();
		String verifyType = bean.getVerifyType().trim();
		
		/*
		 * 设置请求流水格式（3位交易类型  + 自增序列  + 14位时间[yyyyMMddHHmmss]）
		 */
		String trace = tradeTraceSeqDao.getTraceSeq();// 获取数据库中自增序列
		String timeStr = TimeTool.getNowTime(TimeTool.FORMAT02);// 获取当前时间
		
		
		String verifyMsg = StringUtils.EMPTY;
		try {
			String respMsg = StringUtils.EMPTY;
			if("银行卡信息要素鉴权".equals(verifyType)){
				respMsg = bankVerify.verifyBankInformation(accNo, accName, certNo, phoneNo, VerifyConfig.VERIFY_TYPE_Bank + trace + timeStr ,loginUserName);
			}else if("通讯运营商信息要素鉴权".equals(verifyType)){
				respMsg = verifyOperator.verifyOperatorInformation(accName, certNo, phoneNo, VerifyConfig.VERIFY_TYPE_Operator + trace + timeStr ,loginUserName);
			}else if("公安信息要素鉴权".equals(verifyType)){
				respMsg = verifyPolice.verifyPoliceInformation(accName, certNo,  VerifyConfig.VERIFY_TYPE_Police + trace + timeStr ,loginUserName);
			}else if("企业信息要素鉴权".equals(verifyType)){
				respMsg = verifyEntInfos.verifyEntInformation("03", accName, "02", certNo, VerifyConfig.VERIFY_TYPE_EntInfos + trace + timeStr ,loginUserName);
			}else{
				throw new SystemException("请求鉴权类型有误");
			}
			
			verifyMsg = respVerifyMsgTool.handleRespkMsg(respMsg);// 根据请求鉴权后应答做相应的鉴权结果处理
		} catch (Exception e) {
			if(e instanceof SystemException)
				return ((SystemException)e ).getMsg();
			else
				logger.error(null, e);
		}
		
		return verifyMsg;
	}
	
	private ResepVerifyBean disposeVerifyBean2ResepVerifyBean(VerifyBean verifyBean, String verifyMsg){
		if(verifyBean==null)
			return null;
		
		ResepVerifyBean resepVerifyBean = new ResepVerifyBean();
		
		String accName = verifyBean.getAccName();
		if(StringUtils.isNotBlank(accName))
			resepVerifyBean.setAccName(accName);
		
		String accNo = verifyBean.getAccNo();
		if(StringUtils.isNotBlank(accNo))
			resepVerifyBean.setAccNo(accNo);
		
		String certNo = verifyBean.getCertNo();
		if(StringUtils.isNotBlank(certNo))
			resepVerifyBean.setCertNo(certNo);
		
		String phoneNo = verifyBean.getPhoneNo();
		if(StringUtils.isNotBlank(phoneNo))
			resepVerifyBean.setPhoneNo(phoneNo);
		
		String verifyType = verifyBean.getVerifyType();
		if(StringUtils.isNotBlank(verifyType))
			resepVerifyBean.setVerifyType(verifyType);
		
		if(StringUtils.isNotBlank(verifyMsg))
			resepVerifyBean.setVerifyMsg(verifyMsg);
		else
			resepVerifyBean.setVerifyMsg("鉴权请求异常,结果为空!");
		
		return resepVerifyBean;
	}


}
