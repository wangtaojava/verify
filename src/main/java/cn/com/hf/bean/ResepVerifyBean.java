/**
 * llin 2019年4月12日下午10:59:44
 */
package cn.com.hf.bean;

/**
 * @author llin
 *	鉴权结果页面数据实体类
 */
public class ResepVerifyBean {

	private String accName;
	private String phoneNo;
	private String accNo;
	private String certNo;
	private String verifyType;
	private String verifyMsg;

	/**
	 * @return the accName
	 */
	public String getAccName() {
		return accName;
	}

	/**
	 * @param accName
	 *            the accName to set
	 */
	public void setAccName(String accName) {
		this.accName = accName;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo
	 *            the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return accNo;
	}

	/**
	 * @param accNo
	 *            the accNo to set
	 */
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	/**
	 * @return the certNo
	 */
	public String getCertNo() {
		return certNo;
	}

	/**
	 * @param certNo
	 *            the certNo to set
	 */
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	/**
	 * @return the verifyType
	 */
	public String getVerifyType() {
		return verifyType;
	}

	/**
	 * @param verifyType
	 *            the verifyType to set
	 */
	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}

	/**
	 * @return the verifyMsg
	 */
	public String getVerifyMsg() {
		return verifyMsg;
	}

	/**
	 * @param verifyMsg
	 *            the verifyMsg to set
	 */
	public void setVerifyMsg(String verifyMsg) {
		this.verifyMsg = verifyMsg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResepVerifyBean [accName=" + accName + ", phoneNo=" + phoneNo + ", accNo=" + accNo + ", certNo="
				+ certNo + ", verifyType=" + verifyType + ", verifyMsg=" + verifyMsg + "]";
	}

}
