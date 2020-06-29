/**
 * llin 2019年4月13日下午1:56:35
 */
package cn.com.hf.contller.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.hf.exception.SystemException;

/**
 * @author llin
 * 处理页面请求过来的数据
 */
public class DisposePagePostDataUtil {
	
	public static final ObjectMapper mapper = new ObjectMapper();
	private final static List<String> filtrationKeyList = new ArrayList<String>(){
		{
			// 关键词过滤
			add(" ");
			add("'");
			add("=");
		}
	};
	
	public static void disposePageBean(Object bean) throws SystemException{
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(mapper.writeValueAsString(bean), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(map!=null && map.size()>0){
//			getFiltratioString(map, null);// 过滤
			disposeMap2Value(map);// 拦截抛异常
		}
		
	}
	
	public static void disposeMap2Value(Map<String, String> map) throws SystemException{
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			if (StringUtils.isNotBlank(value)) {
				for(String listStr : filtrationKeyList){
					if(value.equals(listStr))
						throw new SystemException("输入数据格式有误");
				}
				
			}
		}
	}


	/**
	 * 对value做过滤操作,简单SQLSQL注入防护措施
	 * 
	 * @param reqMap
	 *            请求map
	 * @param filtrationKey
	 *            关键词
	 */
	public static void getFiltratioString(Map<String, String> reqMap, String filtrationKey) {

		for (Map.Entry<String, String> entry : reqMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (StringUtils.isNotBlank(filtrationKey)) {
				value = value.replace(filtrationKey, StringUtils.EMPTY);
				reqMap.put(key, value);
			} else {
				value = filtrationString(value, null);
			}
			reqMap.put(key, value);
		}
	}
	
	/**
	 * 对字符串过滤（简单的SQL注入防护措施）
	 */
	private static String filtrationString(String filtrationStr, String filtrationKey) {
		String ret = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(filtrationKey)) {
			ret = filtrationStr.replace(filtrationKey, StringUtils.EMPTY);
		} else {

			for (String strKey : filtrationKeyList) {
				ret = filtrationStr.replace(strKey, StringUtils.EMPTY);
			}
		}

		return ret;
	}

}
