package cn.com.hf.verify.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SignTool {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 获取签名值
	 * @param key
	 * @param map
	 * @return
	 */
	public String getSign(String key, Map<String, String> map) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!"sign".equals(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
					list.add(entry.getKey() + "=" + entry.getValue() + "&");// key=value+
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		try {
//			logger.info("md5_before：\n"+result);
			result = MD5EcryptUtil.md5ToUpperCase(result.getBytes("UTF-8"));
//			logger.info("verify:sign_value："+result);
			
		} catch (Exception e) {
			logger.error(null,e);
		}
		return result;
	}
	
	/**
	 * 将Map 转成 key=value+ ... key=value 的形式
	 * @param map
	 * @return
	 */
	public String getMap2String(Map<String, String> map){
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (StringUtils.isNotBlank(entry.getValue())) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");// key=value&
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		return sb.toString().substring(0, sb.toString().length()-1);//去掉末尾的“&”
	}
	
	public boolean verifySign(Map<String, String> recvMap,String key) {
		String sign = getSign(key,recvMap);
		if(sign.equals(recvMap.get("sign"))){
			return true;
		}else{
			return false;
		}
	}
	
//	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {//
//		String s = "accname=杨泓&certno=110104197211112036&certype=01&channelid=17612175031&merid=12175031&opt=verifyPolice&termid=180510&tradetrace=270100073920001&version=1.0.0&key=ffacgr3f8e4tr6agcngykj9vu5m7y";
//		System.out.println(MD5EcryptUtil.md5ToUpperCase(s.getBytes("UTF-8")));
//	}
}
