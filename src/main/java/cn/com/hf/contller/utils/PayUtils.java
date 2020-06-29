/**
 * llin 2019年6月28日下午1:56:54
 */
package cn.com.hf.contller.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.hf.service.HttpClientUtils;
import cn.com.hf.verify.tools.SignTool;

/**
 * @author llin
 */
@Component
public class PayUtils {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SignTool signTool;
	@Autowired
	private HttpClientUtils httpClientUtils;

	private static final String payUrl = "https://notify-test.eycard.cn:7443/EasypayMPSystem/PayServet";
	private static final String channelId = "17612175031";
	private static final String merId = "17612175031";
	private static final String termId = "180423";
	private static final String signKey = "tejybeq5unbkbub2fknbyrzbzklpstz5";
	
	public String pay(String orderType, String orderAmt) {
		String codeUrl = StringUtils.EMPTY;
		
		if(StringUtils.isBlank(orderAmt))
			return codeUrl;
		if(StringUtils.isBlank(orderType))
			orderType = "apPreOrder";

		Map<String, String> map = new HashMap<String, String>();
		map.put("channelId", channelId);// 商户固定参数
		map.put("merId", merId);// 商户固定参数
		map.put("termId", termId);// 商户固定参数
		map.put("version", "1.0");
		
		map.put("orderType",orderType);// 交易类型（支付宝：apPreOrder；微信：wxPreOrder；银联二维码：upPreOrder）
		map.put("tradeTrace", System.currentTimeMillis()+"pay");// 请求订单号（保证唯一）
		
		map.put("orderAmt",orderAmt);// 订单金额（单位：分）
		map.put("body","test");// 订单描述（可顺便传什么）
		map.put("notifyUrl","https://notify-test.eycard.cn/EasypayMPSystem/test");// 交易成功通知地址（可公网访问）
		
		map.put("sign", signTool.getSign(signKey, map));

		try {
			String respMsg = httpClientUtils.httpSend(payUrl, map, 15, 15);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> reqMap = mapper.readValue(respMsg, Map.class);
			codeUrl = reqMap.get("codeUrl");// 付款二维码
		} catch (Exception e) {
			logger.error("预下单异常", e);
		}

		return codeUrl;
	}

}
