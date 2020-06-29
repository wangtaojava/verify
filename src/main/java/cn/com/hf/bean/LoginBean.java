/**
 * llin 2019年4月10日下午10:12:26
 */
package cn.com.hf.bean;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import cn.com.hf.verify.tools.MD5EcryptUtil;

/**
 * @author llin
 */
public class LoginBean {

	private String userName;
	private String passWord;
	private String loginState;
	private String loginIpSite;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the passWord
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * @param passWord
	 *            the passWord to set
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @return the loginState
	 */
	public String getLoginState() {
		return loginState;
	}

	/**
	 * @param loginState
	 *            the loginState to set
	 */
	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	/**
	 * @return the loginIpSite
	 */
	public String getLoginIpSite() {
		return loginIpSite;
	}

	/**
	 * @param loginIpSite
	 *            the loginIpSite to set
	 */
	public void setLoginIpSite(String loginIpSite) {
		this.loginIpSite = loginIpSite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String passWordStr = "***";
		try {
			passWordStr = MD5EcryptUtil.md5ToUpperCase(passWord.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "LoginBean [userName=" + userName + ", passWord=" + passWordStr + ", loginState=" + loginState
				+ ", loginIpSite=" + loginIpSite + "]";
	}

}
