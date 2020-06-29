/**
 * llin 2019年4月11日上午9:38:49
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
 * @author llin 企业信息鉴权
 */
@Component
public class VerifyEntInfos implements VerifyConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String uri = "/VerifyEntInfos";

	@Autowired
	SignTool signTool;
	@Autowired
	HttpClientUtils httpClientUtils;
	@Autowired
	VerifyTradeListDao verifyTradeListDao;

	/**
	 * @param entValueType	企业信息值类型(01:企业注册号;02:企业社会信用代码;03:企业名称)
	 * @param entValue	企业信息值
	 * @param verifyValueType	验证值类型(01:企业注册号;02:企业社会信用代码;03:企业名称)
	 * @param verifyValue	验证值
	 * @param tradeTrace	请求流水，保证唯一
	 * @return
	 */
	public String verifyEntInformation(String entValueType, String entValue, String verifyValueType,
			String verifyValue, String tradeTrace, String loginUserName) {
		String respMsg = StringUtils.EMPTY;

		if (StringUtils.isBlank(tradeTrace) || StringUtils.isBlank(entValueType) || StringUtils.isBlank(entValue)
				|| StringUtils.isBlank(verifyValueType) || StringUtils.isBlank(verifyValue))
			return respMsg;

		Map<String, String> map = new HashMap<String, String>();
		map.put(VERIFY_NODE_channelId, VERIFY_VALUE_channelId);
		map.put(VERIFY_NODE_merId, VERIFY_VALUE_merId);
		map.put(VERIFY_NODE_termId, VERIFY_VALUE_termId);
		map.put(VERIFY_NODE_version, VERIFY_VALUE_version_1_0);

		map.put(VERIFY_NODE_entValueType, entValueType);// 企业信息值类型
		map.put(VERIFY_NODE_entValue, entValue);// 企业信息值
		map.put(VERIFY_NODE_verifyValueType, verifyValueType);// 验证值类型
		map.put(VERIFY_NODE_verifyValue, verifyValue);// 验证值

		map.put(VERIFY_NODE_tradeTrace, tradeTrace);

		map.put(VERIFY_NODE_sign, signTool.getSign(VERIFY_VALUE_signKey, map));

		try {
			logger.info(loginUserName + ">请求数据:"+map.toString());
			respMsg = httpClientUtils.httpSend(VERIFY_VALUE_postUrl + uri, map, VERIFY_VALUE_socketTimeout,
					VERIFY_VALUE_connTimeout);
			logger.info(loginUserName + "<鉴权应答数据:"+respMsg);
			verifyTradeListDao.runPreservation(loginUserName, VerifyConfig.VERIFY_TYPE_EntInfos, tradeTrace, map);
		} catch (Exception e) {
			logger.error("请求企业信息鉴权异常", e);
		}

		return respMsg;
	}

}
