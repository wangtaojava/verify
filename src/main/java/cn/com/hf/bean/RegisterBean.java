/**
 * llin 2019年4月10日下午10:12:26
 */
package cn.com.hf.bean;

/**
 * @author llin
 */
public class RegisterBean {
	
	private String userName;
	private String passWord;
	private String userPhone;
	private String passWord2;

	/**
	 * @return the passWord2
	 */
	public String getPassWord2() {
		return passWord2;
	}

	/**
	 * @param passWord2 the passWord2 to set
	 */
	public void setPassWord2(String passWord2) {
		this.passWord2 = passWord2;
	}

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
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}

	/**
	 * @param userPhone
	 *            the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegisterBean [userName=" + userName + ", passWord=" + passWord + ", userPhone=" + userPhone + "]";
	}

}
