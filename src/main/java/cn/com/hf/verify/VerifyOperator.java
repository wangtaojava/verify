/**
 * llin 2019年4月11日上午9:37:50
 */
package cn.com.hf.verify;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.hf.service.HttpClientUtils;
import cn.com.hf.verify.config.VerifyConfig;
import cn.com.hf.verify.tools.SignTool;
import cn.com.hf.verify.tools.VerifyTradeListDao;

/**
 * @author llin 通讯运营商信息鉴权
 */
@Component
public class VerifyOperator implements VerifyConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String uri = "/VerifyOperatorServet";

	@Autowired
	SignTool signTool;
	@Autowired
	HttpClientUtils httpClientUtils;
	@Autowired
	VerifyTradeListDao verifyTradeListDao;

	public String verifyOperatorInformation(String accName, String certNo, String mobileNo, String tradeTrace, String loginUserName) {
		String respMsg = StringUtils.EMPTY;

		if (StringUtils.isBlank(tradeTrace) || StringUtils.isBlank(accName) || StringUtils.isBlank(certNo)
				|| StringUtils.isBlank(mobileNo))
			return respMsg;

		Map<String, String> map = new HashMap<String, String>();
		map.put(VERIFY_NODE_channelId, VERIFY_VALUE_channelId);
		map.put(VERIFY_NODE_merId, VERIFY_VALUE_merId);
		map.put(VERIFY_NODE_termId, VERIFY_VALUE_termId);
		map.put(VERIFY_NODE_version, VERIFY_VALUE_version_3_0);

		map.put(VERIFY_NODE_accName, accName);
		map.put(VERIFY_NODE_mobileNo, mobileNo);
		map.put(VERIFY_NODE_certNo, certNo);
		map.put(VERIFY_NODE_certType, "01");

		map.put(VERIFY_NODE_tradeTrace, tradeTrace);

		map.put(VERIFY_NODE_sign, signTool.getSign(VERIFY_VALUE_signKey, map));

		try {
			logger.info(loginUserName + ">请求数据:"+map.toString());
			respMsg = httpClientUtils.httpSend(VERIFY_VALUE_postUrl + uri, map, VERIFY_VALUE_socketTimeout,
					VERIFY_VALUE_connTimeout);
			logger.info(loginUserName + "<鉴权应答数据:"+respMsg);
			verifyTradeListDao.runPreservation(loginUserName, VerifyConfig.VERIFY_TYPE_Bank, tradeTrace, map);
		} catch (Exception e) {
			logger.error("请求通讯运营商信息鉴权异常", e);
		}

		return respMsg;
	}

}
