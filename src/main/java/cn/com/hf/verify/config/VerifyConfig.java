/**
 * llin 2019年4月11日下午12:14:40
 */
package cn.com.hf.verify.config;

/**
 * @author llin
 */
public interface VerifyConfig {

	
	public static final String SYSTEM_SESSION_NAME		= "verifyUser";// 用于存放session user
	public static final String SYSTEM_LOGIN_ERRORMSG		= "loginMsg";//  login page errorMsg
	public static final String SYSTEM_REGISTER_ERRORMSG		= "registerMsg";//  register page errorMsg

	public static final String VERIFY_TYPE_Bank		= "101";
	public static final String VERIFY_TYPE_Operator		= "102";
	public static final String VERIFY_TYPE_Police	= "103";
	public static final String VERIFY_TYPE_EntInfos	= "104";

	
//	public static final String VERIFY_VALUE_channelId		= "EV0000000000";
//	public static final String VERIFY_VALUE_merId		= "EM0000000000000";
//	public static final String VERIFY_VALUE_termId		= "ET000000";
//	public static final String VERIFY_VALUE_signKey		= "ipiytnvjdfcqqpgqcti5qbwzudlan5jv";
	public static final String VERIFY_VALUE_channelId		= "EV0000000033";
	public static final String VERIFY_VALUE_merId		= "EM0000000000033";
	public static final String VERIFY_VALUE_termId		= "ET000033";
	public static final String VERIFY_VALUE_signKey		= "232dwqgf435f4g545h645h645hegfw5";
	public static final String VERIFY_VALUE_postUrl		= "https://auth.eycard.cn:8443/EasypayMPSystem";
//	public static final String VERIFY_VALUE_channelId		= "WXAQQ0000000YDD";
//	public static final String VERIFY_VALUE_merId		= "MER023415300001";
//	public static final String VERIFY_VALUE_termId		= "TER9A001";
//	public static final String VERIFY_VALUE_signKey		= "spsgelbufzislltfnof3ppxywakdjok3";
//	public static final String VERIFY_VALUE_postUrl		= "https://notify-test.eycard.cn/EasypayMPSystem";
	public static final String VERIFY_VALUE_version_1_0		= "1.0";
	public static final String VERIFY_VALUE_version_3_0		= "3.0";
	public static final Integer VERIFY_VALUE_socketTimeout		= 10;
	public static final Integer VERIFY_VALUE_connTimeout		= 10;
	
	public static final String VERIFY_NODE_queryValue		= "queryValue";
	public static final String VERIFY_NODE_queryValueType		= "queryValueType";
	public static final String VERIFY_NODE_channelId		= "channelId";
	public static final String VERIFY_NODE_merId            = "merId";
	public static final String VERIFY_NODE_merCode           = "merCode";
	public static final String VERIFY_NODE_termId           = "termId";
	public static final String VERIFY_NODE_tradeTrace       = "tradeTrace";
	public static final String VERIFY_NODE_merchantInfosList           = "merchantInfosList";
	public static final String VERIFY_NODE_contractName           = "contractName";
	public static final String VERIFY_NODE_accNo           = "accNo";
	public static final String VERIFY_NODE_accType           = "accType";
	public static final String VERIFY_NODE_accName           = "accName";
	public static final String VERIFY_NODE_mobileNo           = "mobileNo";
	public static final String VERIFY_NODE_certNo           = "certNo";
	public static final String VERIFY_NODE_certType           = "certType";
	public static final String VERIFY_NODE_entValueType          = "entValueType";
	public static final String VERIFY_NODE_entValue           = "entValue";
	public static final String VERIFY_NODE_verifyValueType           = "verifyValueType";
	public static final String VERIFY_NODE_verifyValue           = "verifyValue";
	public static final String VERIFY_NODE_version		= "version";
	public static final String VERIFY_NODE_sign		= "sign";
	
	public static final String RESP_VALUE_00		= "00";
	public static final String RESP_VALUE_01		= "01";
	public static final String RESP_VALUE_30		= "30";
	public static final String RESP_VALUE_06		= "06";
	public static final String RESP_NODE_resultCode		= "resultCode";
	public static final String RESP_NODE_resultMsg		= "resultMsg";
	public static final String RESP_NODE_verifyState		= "verifyState";
	
}
