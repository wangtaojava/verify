/**  
 * All rights Reserved, Designed By www.tydic.com
 * @Title: FileUtils.java   
 * @Package: cn.com.hf.service   
 * @Description: 
 * @author: wangtao 
 * @date: 2019年11月18日 上午10:53:51
 */
package cn.com.hf.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: FileUtils
 * @Description:
 * @author: wangtao
 * @date: 2019年11月18日 上午10:53:51
 */
public class FileUtils {

	public static void main(String[] args) throws IOException {
//		String word = "aaaaaa\r\n";
//		word += "bbbbb";
//		FileOutputStream fileOutputStream = null;
//		File file = new File("C:\\test.txt");
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		fileOutputStream = new FileOutputStream(file);
//		fileOutputStream.write(word.getBytes("gbk"));
//		fileOutputStream.flush();
//		fileOutputStream.close();
		Map<String, String> map = new HashMap<String, String>();
		map.put("a呵呵", "aaaaa");
		map.put("b哈哈", "bbbbb");
		map.put("c嘿嘿", "ccccc");
		getFilePath(map);
	}

	public static String getFilePath(Map<String, String> map) throws IOException {
		String str = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			if ("address".equals(key)) {
				key = "请求地址";
			} else if ("channelid".equals(key)) {
				key = "渠道编号";
			} else if ("merid".equals(key)) {
				key = "商户编号";
			} else if ("termid".equals(key)) {
				key = "终端编号";
			} else if ("channelkey".equals(key)) {
				key = "渠道密钥";
			}
			String value = entry.getValue();
			str += key + "：" + value + "\n\r";
		}
		System.out.println(str);
		FileOutputStream fileOutputStream = null;
		String id = UUID.randomUUID().toString();
		String filepath = "/home/llin/VerifySystem_8816/" + id + ".txt";
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(str.getBytes("utf-8"));
		fileOutputStream.flush();
		fileOutputStream.close();
		return filepath;
	}

}
