/**
 * llin 2019年4月11日下午12:59:17
 */
package cn.com.hf.verify.tools;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.hf.verify.config.VerifyConfig;

/**
 * @author llin
 * 对鉴权应答数据解析工具
 */
@Component
public class RespVerifyMsgTool implements VerifyConfig{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final ObjectMapper mapper = new ObjectMapper();
	public static final String verifyState_ERROR = "服务器异常";
	public static final String verifyState_isNUll = "鉴权渠道异常，鉴权结果为空";
	public static final String verifyState_isERROR = "请求服务器异常";
	public static final String verifyState_isERROR_30 = "请求字段有误";
	public static final String verifyState_00 = "鉴权信息一致";
	public static final String verifyState_01 = "鉴权信息不一致";

	public String handleRespkMsg(String respMsg){
		String verifyState = StringUtils.EMPTY;
		
		if(StringUtils.isBlank(respMsg))
			return verifyState_isNUll;
		
		try {
			Map<String, String> reqMap = mapper.readValue(respMsg, Map.class);
			
			String resultCode = reqMap.get(RESP_NODE_resultCode);
			String resultMsg = reqMap.get(RESP_NODE_resultMsg);
			if(RESP_VALUE_00.equals(resultCode)){
				String respVerifyState = reqMap.get(RESP_NODE_verifyState);
				if(RESP_VALUE_00.equals(respVerifyState)){
					verifyState = verifyState_00;
				}else if(RESP_VALUE_01.equals(respVerifyState)){
					verifyState = verifyState_01;
				}else if(RESP_VALUE_06.equals(respVerifyState)){
					verifyState = "验证受限";
				}else{
					verifyState = verifyState_isERROR;
				}
			}else if(RESP_VALUE_30.equals(resultCode)){
				verifyState = verifyState_isERROR_30;
			}else{
				verifyState = resultCode+"|"+resultMsg;
			}
			
		} catch (Exception e) {
			logger.error(null, e);
			verifyState = verifyState_ERROR;
		}
		
		return verifyState;
	}
	
//	public String handleRespVerifyBankMsg(String respMsg){
//		String verifyState = StringUtils.EMPTY;
//		
//		if(StringUtils.isBlank(respMsg))
//			return verifyState_isNUll;
//		
//		try {
//			Map<String, String> reqMap = mapper.readValue(respMsg, Map.class);
//			
//			String resultCode = reqMap.get(RESP_NODE_resultCode);
//			String resultMsg = reqMap.get(RESP_NODE_resultMsg);
//			if(RESP_VALUE_00.equals(resultCode)){
//				String respVerifyState = reqMap.get(RESP_NODE_verifyState);
//				if(RESP_VALUE_00.equals(respVerifyState)){
//					verifyState = verifyState_00;
//				}else if(RESP_VALUE_01.equals(respVerifyState)){
//					verifyState = verifyState_01;
//				}else{
//					verifyState = verifyState_isERROR;
//				}
//			}else if(RESP_VALUE_30.equals(resultCode)){
//					verifyState = verifyState_isERROR_30;
//			}else{
//				verifyState = verifyState_isERROR;
//			}
//			
//		} catch (Exception e) {
//			logger.error(null, e);
//			verifyState = verifyState_ERROR;
//		}
//		
//		return verifyState;
//	}
//	
//	public String handleRespVerifyOperatorMsg(String respMsg){
//		return handleRespVerifyBankMsg(respMsg);
//	}
//	
//	public String handleRespVerifyPoliceMsg(String respMsg){
//		return handleRespVerifyBankMsg(respMsg);
//	}
	
}
