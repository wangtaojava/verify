package cn.com.hf.contller.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection.Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Pay {
	public static void main(String[] args) {
//		method1();
//		method2();
//		method3();
//		method4();
//		method5();
//		method6();
//		method7();
//		method8();
//		method9();
	}

	// 请求签名参数接口测试
	public static void method1() {
		String channelid = "616161616161618";
		String opt = "getSign";
		String str = "channelid=" + channelid + "&opt=" + opt;
		String key = "d5riddkald4k4did";
		String sign = getSign(str, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("opt", opt);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"opt":"getSign","sign":"B4569B3EA8D339AD3D7835B32108F2C4","resultcode":"00","key":"jdvaf10odd5v8dva47klidd5ddkiladd","channelid":"616161616161618"}
	}

	// 统一下单接口(NATIVE)
	public static void method2() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "apPreOrder";
		String tradetype = "NATIVE";
		String tradetrace = "2vddj55kki5d4dk8dj4ir2f3d5kf2oa2";
		String tradeamt = "1";
		String body = "示例报文";
		String notifyurl = "https://www.baidu.com";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt + "&tradetype="
				+ tradetype + "&tradetrace=" + tradetrace + "&tradeamt=" + tradeamt + "&body=" + body + "&notifyurl="
				+ notifyurl;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetype", tradetype);
		param.put("tradetrace", tradetrace);
		param.put("tradeamt", tradeamt);
		param.put("body", body);
		param.put("notifyurl", notifyurl);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"codeurl":"weixin://wxpay/bizpayurl?pr=W6XbPKj","tradetype":"NATIVE","termid":"W0001081","opt":"wxPreOrder","tradetrace":"2vddj55kki5d4dk8dj4ir2f3d5kf2oa2","returnmsg":"","wtorderid":"11520190506104642063118","sign":"D6F84952C95CD3BF22B0760885E9CF72","merid":"W00000000001001","prepayid":"","resultcode":"00","channelid":"616161616161618"}
	}

	// 统一下单接口(JSAPI)
	public static void method3() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "apPreOrder";
		String tradetype = "JSAPI";
		String tradetrace = "df4adiiddd5l7ddfd1did54dflldk3fl";
		String tradeamt = "1";
		String body = "娃哈哈";
		String notifyurl = "https://www.baidu.com";
		String openid = "2088912121113355";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt + "&openid="
				+ openid + "&tradetype=" + tradetype + "&tradetrace=" + tradetrace + "&tradeamt=" + tradeamt + "&body="
				+ body + "&notifyurl=" + notifyurl;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetype", tradetype);
		param.put("tradetrace", tradetrace);
		param.put("tradeamt", tradeamt);
		param.put("body", body);
		param.put("notifyurl", notifyurl);
		param.put("openid", openid);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"tradetype":"JSAPI","termid":"W0001081","opt":"wxPreOrder","tradetrace":"df4adiiddd5l7ddfd1did54dflldk3fl","returnmsg":"","wtorderid":"11620190506105323063121","sign":"495D6140A0E9B065ABF70F51D54FD4C5","merid":"W00000000001001","prepayid":"{\"timeStamp\":\"1557111204\",\"package\":\"prepay_id=wx06105324848383696b35d03e1322815783\",\"paySign\":\"jo5CJ6xgHhUzRMdOufMoDbiy8FpwQ7U0B0o8RaUZjvNQvkP53ceiVzWKvw/CZ/PRje8fi1uygSAt4DVkWh8tmL3PXLoGjckFeJDEXYflGZcBQ9OTBeBlqeD0T2njw5bnhRkes80eb9/bC9axTG5CAc1dbv68EoBxwPm2a4zwVkArMNqgbq84Ukcll4FiAUff4PtijOoK2vjpZTYUnJBQ1dmeQUZ5ua9jqhXeXC19+cg3gkahn0TDqyL4+b4Jb+Ef36b2C7W1VfperMfiVDT8hgcMds4YTvaEipdfUo9CT1GklQf9dEJH4GtVjWdse9pFC2ZBR26pZLLYY/uFMMrXYA==\",\"appId\":\"wx89d180221359e985\",\"signType\":\"RSA\",\"nonceStr\":\"Px3HhYNrrKTvEEexP3TGk0tuGjXtNzHM\"}","resultcode":"00","channelid":"616161616161618"}
	}

	// 统一下单接口(APP)--下单失败，待测试
	public static void method4() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "wxPreOrder";
		String tradetype = "APP";
		String tradetrace = "EP" + System.currentTimeMillis();
		String tradeamt = "1";
		String body = "娃哈哈";
		String notifyurl = "https://www.baidu.com";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt + "&tradetype="
				+ tradetype + "&tradetrace=" + tradetrace + "&tradeamt=" + tradeamt + "&body=" + body + "&notifyurl="
				+ notifyurl;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetype", tradetype);
		param.put("tradetrace", tradetrace);
		param.put("tradeamt", tradeamt);
		param.put("body", body);
		param.put("notifyurl", notifyurl);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}

//		{"termid":"W0001081","opt":"wxPreOrder","tradetrace":"k53vs3fod5fddadk2dls5aalk7odd5d7","returnmsg":"下单失败","wtorderid":"11720190506110005063125","sign":"1E32BAC4934A9904160B6261472FC80B","merid":"W00000000001001","resultcode":"06","channelid":"616161616161618"}

	}

	// 后台通知接口--待测
	public static void method5() {
		String tradetrace = "2dosdrdr0k8d222aldfri7d28l3f854s";
		String wtorderid = "11620190505172145063100";
		String wxopenid = "oH3sNty4Dqw3yOyF_Qdi4yhY6Pj8";
		String wxtransactionid = "wx051721468116800a14ed7b6b3264314360";
		String wxtimeend = "20190505172146";
		String acctype = "E";
		String str1 = "tradetrace=" + tradetrace + "&wtorderid=" + wtorderid + "&wxopenid=" + wxopenid
				+ "&wxtransactionid=" + wxtransactionid + "&wxtimeend=" + wxtimeend + "&acctype=" + acctype;
		String key = "k5kfffflldriid0ikdkjlf2d47dlk7ij";
		String sign = getSign(str1, key);
		System.out.println(sign);

	}

	// 提交被扫支付接口
	public static void method6() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "scanPay";
		String tradetrace = "k53vs3fod5fddadk2dls5aalk7odd5d7";
		String tradeamt = "1";
		String authcode = "289209722522404861";
		String body = "测试";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt
				+ "&tradetrace=" + tradetrace + "&tradeamt=" + tradeamt + "&body=" + body + "&authcode=" + authcode;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetrace", tradetrace);
		param.put("tradeamt", tradeamt);
		param.put("authcode", authcode);
		param.put("body", body);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"wxtimeend":"20190506111451","wtorderid":"21420190506111449063139","sign":"7C1905834C5F3B569F09E10B251E3253","acctype":"U","termid":"W0001081","opt":"scanPay","tradetrace":"k53vs3fod5fddadk2dls5aalk7odd5d7","wxtransactionid":"2019050622001413351040829584","returnmsg":"Success","merid":"W00000000001001","resultcode":"00","wxopenid":"2088912121113355","channelid":"616161616161618"}

	}

	// 单笔交易查询接口
	public static void method7() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "tradeQuery";
		String tradetrace = "21520190506135842063153";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt
				+ "&tradetrace=" + tradetrace;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetrace", tradetrace);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"termid":"W0001081","opt":"tradeQuery","tradetrace":"k53vs3fod5fddadk2dls5aalk7odd5d7","returnmsg":"二维码已过期，请退出重试","wtorderid":"11420190506111301063138","sign":"704BF893ADDECE3EDE296D62D155683E","merid":"W00000000001001","resultcode":"06","channelid":"616161616161618"}
	}

	// 分订单接口--出错，待测
	public static void method8() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "orderDetail";
		String tradetrace = "k53vs3fod5fddadk2dls5aalk7odd5d7";
		JSONArray suborders = new JSONArray();
		JSONObject obj1 = new JSONObject();
		obj1.put("subhandingfee", "10");
		obj1.put("subtradetrace", "ss11");
		obj1.put("subtradeamt", "100");
		obj1.put("submerid", "W00000000001001");
		obj1.put("subtermid", "W0001081");
		suborders.add(obj1);
		JSONObject obj2 = new JSONObject();
		obj2.put("subhandingfee", "20");
		obj2.put("subtradetrace", "ss22");
		obj2.put("subtradeamt", "200");
		obj2.put("submerid", "W00000000001001");
		obj2.put("subtermid", "W0001081");
		suborders.add(obj2);
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt
				+ "&tradetrace=" + tradetrace + "&suborders=" + suborders.toJSONString();
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetrace", tradetrace);
		param.put("suborders", suborders.toJSONString());
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		{"termid":"W0001081","opt":"orderDetail","tradetrace":"k53vs3fod5fddadk2dls5aalk7odd5d7","returnmsg":"出错","sign":"53124CC08F5D65C9DFAE814B9A3FE095","merid":"W00000000001001","resultcode":"06","channelid":"616161616161618"}
	}

	// 二维码立招接口--调不通，连接拒绝
	public static void method9() {
		String channelid = "616161616161618";
		String merid = "W00000000001001";
		String termid = "W0001081";
		String opt = "dirBankPay";
		String tradetype = "44";
		String tradetrace = "k53vs3fod5fddadk2dls5aalk7odd5d7";
		String tradeamt = "1";
		JSONObject json = new JSONObject();
		json.put("name", "支付");
		json.put("desc", "测试");
		String body = json.toJSONString();
		String notifyurl = "http://192.168.5.202:8080/apsaccess/apppayacc";
		String returnurl = "http://192.168.5.202:8080/apsaccess/apppayacc";
		String str1 = "channelid=" + channelid + "&merid=" + merid + "&termid=" + termid + "&opt=" + opt + "&tradetype="
				+ tradetype + "&tradetrace=" + tradetrace + "&tradeamt=" + tradeamt + "&body=" + body + "&notifyurl="
				+ notifyurl + "&returnurl=" + returnurl;
		String key = "jdvaf10odd5v8dva47klidd5ddkiladd";
		String sign = getSign(str1, key);
		System.out.println(sign);

		Map<String, String> param = new HashMap<String, String>();
		param.put("channelid", channelid);
		param.put("merid", merid);
		param.put("termid", termid);
		param.put("opt", opt);
		param.put("tradetype", tradetype);
		param.put("tradetrace", tradetrace);
		param.put("tradeamt", tradeamt);
		param.put("body", body);
		param.put("notifyurl", notifyurl);
		param.put("returnurl", returnurl);
		param.put("sign", sign);
		try {
			Response result = HttpUtils.post("https://notify-test.eycard.cn/WorthTech_Access_AppPaySystemV2/apppayacc",
					param);
			System.out.println(result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 测试获取sign和md5
	public static void testSign() {
		String str1 = "channelid=channel01&opt=tradeQuery&tradetrace=1234567890&merid=merid01";
		String key = "1234567890ABCDEFGHIJKLMNOPQRSTUV";
		getSign(str1, key);
	}

	// 获取sign
	public static String getSign(String str1, String key) {
		String[] strs = str1.split("&");
		List<String> mlist = Arrays.asList(strs);
		Collections.sort(mlist);
//		for (int i = 0; i < strs.length; i++) {
//			for (int j = i + 1; j < strs.length; j++) {
//				if (strs[i].compareTo(strs[j]) > 0) {
//					String temp = strs[i];
//					strs[i] = strs[j];
//					strs[j] = temp;
//				}
//			}
//		}
		String str2 = "";
		for (int i = 0; i < mlist.size(); i++) {
			str2 += mlist.get(i) + "&";
		}
		str2 += "key=" + key;
		System.out.println(str2);
		str2 = MD5(str2);
		return str2;
	}

	// md5加密
	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result.toUpperCase();
	}
}
