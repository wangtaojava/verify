package cn.com.hf.contller.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection.Response;;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Authentication {
//	private static final String url = "https://notify-test.eycard.cn:7443/EasypayMPSystem";
//	private static final String signKey = "tejybeq5unbkbub2fknbyrzbzklpstz5";
//	private static final String channelid = "17612175031";
//	private static final String merid = "17612175031";
//	private static final String termid = "180423";
	private static final String url = "https://auth.eycard.cn:8443/EasypayMPSystem";
//	private static final String signKey = "232dwqgf435f4g545h645h645hegfw5";
//	private static final String channelid = "EV0000000033";
//	private static final String merid = "EM0000000000033";
//	private static final String termid = "ET000033";
	private static final String signKey = "ffacgr3f8e4ndctr6agcngykj9vu5m7y";
	private static final String channelid = "EV0000000006";
	private static final String merid = "EM0000000000006";
	private static final String termid = "ET000006";

	public static void main(String[] args) throws Exception {
//		method1();
//		method2();
//		method3();
//		method4();
//		method5();
//		method6();// todo 未接短信通道
		method7("01", "91310105671121989J");// todo
//		method9();// todo
//		method10();
//		method11();
//		method12();
//		method13("C:\\Users\\wt\\Desktop\\营业执照副本\\营业执照副本_00.png");
//		method14();
	}

	// 银行卡信息鉴权
	public static void method1() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP" + System.currentTimeMillis();
		String accNo = "621452001003644071";
		String accName = "刘文敬";
		String certNo = "120104198506170424";
		String mobileNo = "13920743024";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&accNo=" + accNo + "&accName=" + accName + "&certNo=" + certNo + "&version=" + version+"&mobileNo="+mobileNo;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("accNo", accNo);
		param.put("accName", accName);
		param.put("certNo", certNo);
		param.put("version", version);
		param.put("mobileNo", mobileNo);
		param.put("sign", sign);
		try {
			System.out.println("银行卡信息鉴权发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/VerifyBankServet", param);
			System.out.println("银行卡信息鉴权返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		银行卡信息鉴权发送参数===》{termId=180423, tradeTrace=EP1557127602010, certNo=34022119950914785X, accNo=6228481998092075179, accName=王涛, sign=D274BBC676000FE4B94B34879F7D9649, merId=17612175031, version=3.0, channelId=17612175031}
//		银行卡信息鉴权返回数据===》{"resultCode":"00","merorderId":"90120190506152642342952","sign":"9279206DCB3F07EBD97D44D5CBC12FCC","verifyState":"00"}
	}

	// 通讯运营商信息鉴权
	public static void method2() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP" + System.currentTimeMillis();
		String accName = "王涛";
		String mobileNo = "15178534169";
		String certNo = "34022119950914785X";
		String certType = "01";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&accName=" + accName + "&mobileNo=" + mobileNo + "&certNo=" + certNo + "&certType=" + certType
				+ "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("accName", accName);
		param.put("mobileNo", mobileNo);
		param.put("certNo", certNo);
		param.put("certType", certType);
		param.put("version", version);
		param.put("sign", sign);
		try {
			System.out.println("通讯运营商信息鉴权发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/VerifyOperatorServet", param);
			System.out.println("通讯运营商信息鉴权返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		通讯运营商信息鉴权发送参数===》{termId=180423, tradeTrace=EP1557128089824, certNo=34022119950914785X, certType=01, accName=王涛, sign=2F88631C3FAE360D8BA0156C7AA8E0E7, merId=17612175031, mobileNo=15178534169, version=3.0, channelId=17612175031}
//		通讯运营商信息鉴权返回数据===》{"resultCode":"00","merorderId":"90220190506153449342956","sign":"2ED2D879FD03BDDFDBAC7E534380CEF9","verifyState":"01"}
	}

	// 公安信息验证
	public static void method3() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP" + System.currentTimeMillis();
		String accName = "王涛";
		String certNo = "34022119950914785X";
		String certType = "01";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&accName=" + accName + "&certNo=" + certNo + "&certType=" + certType + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("accName", accName);
		param.put("certNo", certNo);
		param.put("certType", certType);
		param.put("version", version);
		param.put("sign", sign);
		try {
			System.out.println("公安信息验证发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/VerifyPoliceServet", param);
			System.out.println("公安信息验证返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		公安信息验证发送参数===》{termId=180423, tradeTrace=EP1557128432372, certNo=34022119950914785X, certType=01, accName=王涛, sign=5B1CF8A9212E73C4B704739D8A6848EC, merId=17612175031, version=3.0, channelId=17612175031}
//		公安信息验证返回数据===》{"resultCode":"00","merorderId":"90320190506154032342958","sign":"C72A7D23C2C096165EF48198BC45BF9D","verifyState":"00"}
	}

	// 鉴权信息查询
	public static void method4() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String merorderId = "90320190506154032342958";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&merorderId=" + merorderId
				+ "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("merorderId", merorderId);
		param.put("version", version);
		param.put("sign", sign);
		try {
			System.out.println("鉴权信息查询发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/VerifyQueryServet", param);
			System.out.println("鉴权信息查询返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		鉴权信息查询发送参数===》{termId=180423, merorderId=90320190506154032342958, sign=B7C0141D5B1490AF0DE4BFE123F959D9, merId=17612175031, version=3.0, channelId=17612175031}
//		鉴权信息查询返回数据===》{"tradeTrace":"EP1557128432372","resultCode":"00","merorderId":"90320190506154032342958","sign":"E442C6FFEC9D8C1A009FA52162458DC1","verifyState":"00"}
	}

	// 风险信息鉴权
	public static void method5() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String opt = "verifyRiskIdcard";
		String tradeTrace = "EP" + System.currentTimeMillis();
		String riskNumber = "34022119950914785X";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&opt=" + opt + "&tradeTrace="
				+ tradeTrace + "&riskNumber=" + riskNumber + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("opt", opt);
		param.put("tradeTrace", tradeTrace);
		param.put("riskNumber", riskNumber);
		param.put("version", version);
		param.put("sign", sign);
		try {
			System.out.println("风险信息鉴权发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/QueryRiskServet", param);
			System.out.println("风险信息鉴权返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		风险信息鉴权发送参数===》{termId=180423, tradeTrace=EP1557130159721, opt=verifyRiskIdcard, sign=A4BBF574CAE790A6F0CA135BDD8202F6, merId=17612175031, riskNumber=34022119950914785X, version=3.0, channelId=17612175031}
//		风险信息鉴权返回数据===》{"resultCode":"00","merorderId":"91220190506160920342960","sign":"4B6126F8A392E90026A14B74CCA8DCFB","resultMsg":"Success"}
	}

	// 短信通知类接口
	public static void method6() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP" + System.currentTimeMillis();
		String content = "您好";
		String mobileNo = "15178534169";
		String version = "1.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&content=" + content + "&mobileNo=" + mobileNo + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("content", content);
		param.put("mobileNo", mobileNo);
		param.put("version", version);
		param.put("sign", sign);
		try {
			System.out.println("短信通知类接口发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/SendingSMSServet", param);
			System.out.println("短信通知类接口返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 企业信息查询接口
//	01	企业注册号
//	02	企业社会信用代码
//	03	企业名称
	public static String method7(String type, String number) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = UUID.randomUUID().toString().replace("-", "");
		String version = "1.0";
		String optType = "queryBasic";
		String queryValueType = type;
		String queryValue = number;
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&version=" + version + "&optType=" + optType + "&queryValueType=" + queryValueType + "&queryValue="
				+ queryValue;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("optType", optType);
		param.put("queryValueType", queryValueType);
		param.put("queryValue", queryValue);
		param.put("sign", sign);
		String result = "";
		System.out.println("企业信息查询接口发送参数===》" + param.toString());
		Response response = HttpUtils.post(url + "/QueryEntInfos", param);
		result = response.body();
		System.out.println("企业信息查询接口返回数据===》" + result);
		return result;
//		企业信息查询接口发送参数===》{optType=queryBasic, termId=180423, tradeTrace=EP1557391323711, sign=AC19EA331F0A8DEDFD5ED330D9FAA85E, queryValueType=01, merId=17612175031, queryValue=371400228016303, version=1.0, channelId=17612175031}
//		企业信息查询接口返回数据===》{"entInfos":"{\"regNo\":\"371400228016303\",\"entName\":\"山东祥瑞广告装饰有限公司\",\"city\":\"德州市\",\"historyName\":null,\"county\":\"德城区\",\"frName\":\"张海宽\",\"revDate\":null,\"industryCode\":\"7240\",\"entType\":\"有限责任公司(自然人投资或控股)\",\"creditCode\":\"91371400761856209M\",\"regCapCur\":\"人民币\",\"province\":\"山东省\",\"orgCode\":\"761856209\",\"regCap\":\"300.000000\",\"ancheYear\":\"2017\",\"industryPhyCode\":\"L\",\"industryPhyName\":\"租赁和商务服务业\",\"industryName\":\"广告业\",\"canDate\":null,\"address\":\"山东省德州市德城区广川街道办事处广川路滨海首府1号门市\",\"openTo\":\"0000-00-00\",\"recCap\":\" \",\"apprDate\":\"2019-03-13\",\"openFrom\":\"2004-04-12\",\"esDate\":\"2004-04-12\",\"areaCode\":\"371400\",\"operateScope\":\"设计、制作、发布国内各类广告业务（含固定形式印刷品广告）；企业营销策划；LED灯光亮化及楼宇照明工程安装施工；加工、制作、安装、销售LED灯具；五金电料；广告材料；高低压电器；开关插座等销售（依法须经批准的项目，经相关部门批准后方可开展经营活动）。\",\"regOrg\":\"德州市市场监督管理局\",\"entStatus\":\"在营（开业）企业\",\"ancheDate\":null}","resultCode":"00","merorderId":"93120190509164202342998","sign":"C22CC56932DE9ED11E50E25F48DFCBA9"}
	}

	// 企业信息鉴权接口
	public static void method9() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP" + System.currentTimeMillis();
		String version = "1.0";
		String entValueType = "03";
		String entValue = "易生支付有限公司";
		String verifyValueType = "03";
		String verifyValue = "易生支付有限公司";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&version=" + version + "&entValueType=" + entValueType + "&entValue=" + entValue
				+ "&verifyValueType=" + verifyValueType + "&verifyValue=" + verifyValue;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("entValueType", entValueType);
		param.put("entValue", entValue);
		param.put("verifyValueType", verifyValueType);
		param.put("verifyValue", verifyValue);
		param.put("sign", sign);
		try {
			System.out.println("企业信息鉴权接口发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/VerifyEntInfos", param);
			System.out.println("企业信息鉴权接口返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ocr-银行卡
	public static void method10() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String opt = "ocrBankcard";
		String tradeTrace = "EP" + System.currentTimeMillis();
		String encryptStr = imageToBase64("C:\\Users\\pll\\Desktop\\bank.jpg");
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&opt=" + opt + "&tradeTrace="
				+ tradeTrace + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("opt", opt);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("sign", sign);
		System.out.println("ocr-银行卡发送参数===》" + param.toString());
		param.put("encryptStr", encryptStr);
		try {
//			System.out.println("ocr-银行卡发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/OcrServet", param);
			System.out.println("ocr-银行卡返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ocr-银行卡发送参数===》{termId=180423, tradeTrace=EP1557192103657, opt=ocrBankcard, sign=0B7733634F9C801DD9EE378F6977660F, merId=17612175031, version=3.0, channelId=17612175031}
//		ocr-银行卡返回数据===》{"tradeTrace":"EP1557192103657","resultCode":"00","merorderId":"120109214200074620190507","ocrState":"00","sign":"2BBFAACD9B85D787A904913E8647FAA5","ocrContent":"{\"accno\":\"6212261307007667239\"}"}
	}

	// ocr-身份证
	public static void method11() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String opt = "ocrIdcard";
		String tradeTrace = "EP" + System.currentTimeMillis();
		String encryptStr = imageToBase64("C:\\Users\\pll\\Desktop\\idcard.jpg");
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&opt=" + opt + "&tradeTrace="
				+ tradeTrace + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("opt", opt);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("sign", sign);
		System.out.println("ocr-身份证发送参数===》" + param.toString());
		param.put("encryptStr", encryptStr);
		try {
//			System.out.println("ocr-身份证发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/OcrServet", param);
			System.out.println("ocr-身份证返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ocr-身份证发送参数===》{termId=180423, tradeTrace=EP1557196139079, opt=ocrIdcard, sign=3FB1E7317DA6B709A4177CE65A9C1420, merId=17612175031, version=3.0, channelId=17612175031}
//		ocr-身份证返回数据===》{"tradeTrace":"EP1557196139079","resultCode":"00","merorderId":"120210285900075920190507","ocrState":"00","sign":"E7EBC634C5A5A20D580271B2AA1F6590","ocrContent":"{\"birthday\":\"19950914\",\"certno\":\"34022119950914785X\",\"address\":\"安徽省芜湖市芜湖县六浪镇兴隆行政村七佰组97\",\"nation\":\"汉\",\"sex\":\"男\",\"accname\":\"王涛\"}"}
	}

	// ocr-人证对比
	public static void method12() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String opt = "ocrFace";
		String tradeTrace = "EP" + System.currentTimeMillis();
		String accName = "王涛";
		String certNo = "34022119950914785X";
		String certType = "01";
		String encryptStr = imageToBase64("C:\\Users\\pll\\Desktop\\face.jpg");
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&opt=" + opt + "&tradeTrace="
				+ tradeTrace + "&accName=" + accName + "&certNo=" + certNo + "&certType=" + certType + "&version="
				+ version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("opt", opt);
		param.put("tradeTrace", tradeTrace);
		param.put("accName", accName);
		param.put("certNo", certNo);
		param.put("certType", certType);
		param.put("version", version);
		param.put("sign", sign);
		System.out.println("ocr-人证对比发送参数===》" + param.toString());
		param.put("encryptStr", encryptStr);
		try {
//			System.out.println("ocr-人证对比发送参数===》" + param.toString());
			Response result = HttpUtils.post(url + "/OcrServet", param);
			System.out.println("ocr-人证对比返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ocr-营业执照
	public static String method13(String path) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String opt = "ocrBusinessLicense";
		String tradeTrace = UUID.randomUUID().toString().replace("-", "");
		System.out.println(tradeTrace);
		String encryptStr = imageToBase64(path);
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&opt=" + opt + "&tradeTrace="
				+ tradeTrace + "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("opt", opt);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("sign", sign);
		System.out.println("ocr-营业执照发送参数===》" + param.toString());
		param.put("encryptStr", encryptStr);
		String number = "";
		Response result = HttpUtils.post(url + "/OcrServet", param);
		System.out.println("ocr-营业执照返回数据===》" + result.body());
		if (result != null) {
			JSONObject json = JSON.parseObject(result.body());
			JSONObject ocrContent = json.getJSONObject("ocrContent");
			if (ocrContent != null) {
				number = ocrContent.getString("number");
			}
		}
		return number;
//		ocr-营业执照发送参数===》{termId=180423, tradeTrace=EP1557208714519, opt=ocrBusinessLicense, sign=C9A1B4CB140C31F1F434C9FF53BC282C, merId=17612175031, version=3.0, channelId=17612175031}
//		ocr-营业执照返回数据===》{"tradeTrace":"EP1557208714519","resultCode":"00","merorderId":"120413583400076320190507","ocrState":"00","sign":"55D9B7A2CD5BB54CC8DBD6DF985DBA2F","ocrContent":"{\"number\":\"371400228016303\",\"leaglperson\":\"张海宽\",\"enddate\":\"29991231\",\"address\":\"德城区乐园南街滨海首府小区1号楼1号门市\",\"begindate\":\"20040412\",\"entname\":\"东祥瑞广告装饰有限公司\",\"scope\":\"设计、制作、发布国内各类广告业务(含固定形式印刷品广告)加工、制作、销售虹灯、五金制品、灯具、广告材料;高低压电器、开关插座销售(依法须经批准的项目,经相关部门批准后方开展经营活动)。\",\"captial\":\"叁佰万元整\"}"}
	}

	// ocr-订单查询（opt参数不要）
	public static void method14() {
		Map<String, String> param = new HashMap<String, String>();
		String channelId = channelid;
		String merId = merid;
		String termId = termid;
		String tradeTrace = "EP1557208714519";
		String version = "3.0";
		String str = "channelId=" + channelId + "&merId=" + merId + "&termId=" + termId + "&tradeTrace=" + tradeTrace
				+ "&version=" + version;
		String sign = Pay.getSign(str, signKey);
		System.out.println("MD5加密sign===》" + sign);
		param.put("channelId", channelId);
		param.put("merId", merId);
		param.put("termId", termId);
		param.put("tradeTrace", tradeTrace);
		param.put("version", version);
		param.put("sign", sign);
		System.out.println("ocr-订单查询发送参数===》" + param.toString());
		try {
			Response result = HttpUtils.post(url + "/QueryOcrServet", param);
			System.out.println("ocr-订单查询返回数据===》" + result.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ocr-订单查询发送参数===》{termId=180423, tradeTrace=EP1557208714519, sign=27B13CB84A2107F7F45B44080E00FA72, merId=17612175031, version=3.0, channelId=17612175031}
//		ocr-订单查询返回数据===》{"tradeTrace":"EP1557208714519","resultCode":"00","merorderId":"120413583400076320190507","sign":"9B9D3BC10D9904EF1E6472608E1331F4","ocrContent":"3vjltCjVc4xyaR+3MxHUkpCIGDdxRch/u78WmJj4l7QBe6UZcB7Ku83e4zu46cmzU4vxXSfPeHtFyF73nwzyngeTiVgijb5Z2jqT20wliGcO1ebn9/J3oMVI9m/yKhpkP7T6QNJ9Qh0KdSRqKSnVeEpazVbmPmuJ2oONiCMfxSYBmlfGFVbeMr7ApR9EFWoa2n+LKG+/BBItyTnb3XJQytQ3llL5yztMxkReGu1co0RGvn8DoG8mJYHK4ppjIf+6FnPAgFk6z5HEUL/kMrWNrOscz8B7UJsg6uFfOLG++JqhbUeuWSzBgvjuLOAAkG3khXaxHwW/Dwt/cr2IXuJeAjqhjFhAVXsDgT+ag92YJuDOdEB8djgfUzkE6jBmbA1n5xIKcqehKoH8ik9gXldZUKQCd9rXnaW1+O4s4ACQbeRCPWma9151SmyDo8HxzDSBUXsB3sg3XMu5T0j2Qpw27UnKIVAN28G4W5UnK9SqhM/n8EZc+xiouuPa7r8DZ/c06XfW/oDsN75eveLasedQSPwSUX2YlBZTADf7imN0gfQVUm9nlO/IpZjv7E8DzTvGJZoX39R2kxQHhMK8IgpjmM/KWfL9ITDyIURxZ9o7kJjTyQzzp04QbM1G8FK0OGeVSrbqj0fHidpn6yWMWx8sKnXJjrfP/mZB9tDRWC57Rk/o7Nt67k9Tnw=="}
	}

	// 图片转base64
	public static String imageToBase64(String path) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

}
