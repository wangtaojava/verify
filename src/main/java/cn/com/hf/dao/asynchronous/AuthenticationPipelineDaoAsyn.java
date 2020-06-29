/**
 * llin 2019年4月15日下午5:00:24
 */
package cn.com.hf.dao.asynchronous;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.hf.dao.AuthenticationPipelineDao;
import cn.com.hf.exception.SystemException;

/**
 * @author llin
 *
 */
public class AuthenticationPipelineDaoAsyn implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String userName;
	private String verifyType;
	private String tradeTrace;
	private Map<String, String> verifyDataMap;
	private AuthenticationPipelineDao authenticationPipelineDao;

	public AuthenticationPipelineDaoAsyn(String userName, String verifyType, String tradeTrace,
			Map<String, String> verifyDataMap, AuthenticationPipelineDao authenticationPipelineDao) {
		this.userName = userName;
		this.verifyType = verifyType;
		this.tradeTrace = tradeTrace;
		this.verifyDataMap = verifyDataMap;
		this.authenticationPipelineDao = authenticationPipelineDao;
	}

	@Override
	public void run() {
		try {
			authenticationPipelineDao.insertAuthenticationPipelineManage(userName, verifyType, tradeTrace,
					verifyDataMap);
		} catch (SystemException e) {
			logger.error(null, e);
		}
//		logger.info("保存流水正常");
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
	 * @return the tradeTrace
	 */
	public String getTradeTrace() {
		return tradeTrace;
	}

	/**
	 * @param tradeTrace
	 *            the tradeTrace to set
	 */
	public void setTradeTrace(String tradeTrace) {
		this.tradeTrace = tradeTrace;
	}

	/**
	 * @return the verifyDataMap
	 */
	public Map<String, String> getVerifyDataMap() {
		return verifyDataMap;
	}

	/**
	 * @param verifyDataMap
	 *            the verifyDataMap to set
	 */
	public void setVerifyDataMap(Map<String, String> verifyDataMap) {
		this.verifyDataMap = verifyDataMap;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return the authenticationPipelineDao
	 */
	public AuthenticationPipelineDao getAuthenticationPipelineDao() {
		return authenticationPipelineDao;
	}

	/**
	 * @param authenticationPipelineDao
	 *            the authenticationPipelineDao to set
	 */
	public void setAuthenticationPipelineDao(AuthenticationPipelineDao authenticationPipelineDao) {
		this.authenticationPipelineDao = authenticationPipelineDao;
	}

}
