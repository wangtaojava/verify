/**
 * llin 2019年4月13日下午8:36:23
 */
package cn.com.hf.contller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Encoder;

/**
 * @author llin
 */
@Controller
public class ResetPasswordContller {
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping("/resetPassword")
	public String register(ModelMap map) {
		return "resetPassword";
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(str2base64("admin@llin"));
	}
	
	/**
	 * 将转换成base64编码格式的字符串
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private static String str2base64(String str) throws Exception {
		if (StringUtils.isBlank(str))
			return null;

		BASE64Encoder encoder = new BASE64Encoder();
		byte[] bytes = str.getBytes("UTF-8");
		return encoder.encode(bytes);
	}

}
