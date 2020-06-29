package cn.com.hf.verify.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TimeTool {
	
	public static final String FORMAT01 = "yyyyMMddHHmmssSSS";
	public static final String FORMAT02 = "yyyyMMddHHmmss";
	public static final String FORMAT03 = "yyyyMMdd";
	public static final String FORMAT04 = "yyyyMM";
	
//	public static void main(String[] args) throws Exception {
////		getCompareTime("20180606", "20180606");
////		getCompareTime("20180608", "20180607");
//		System.out.println(getTomorrowTime("yyyyMMddHHmmssSSS"));
//		System.out.println(getTimeDifference(Calendar.MONTH, 0,"yyyyMM"));
//		System.out.println(getTimeDifference(Calendar.MONTH, -2,"yyyyMM"));
//		
//	}
	
	/**
	 * 比较 时间
	 * @param compareTime	需要比较的时间
	 * @param nowTime	现在的时间(default nowTime:yyyyMMdd)
	 * @return true：compareTime < nowTime;  false：else 
	 */
	public static boolean getCompareTime(String compareTime ,String nowTime) {
		if(StringUtils.isBlank(nowTime))
			nowTime = getNowTime(FORMAT03);
		int compareTo = compareTime.compareTo(nowTime);
//		System.out.println("compareTo:"+compareTo);
		if(compareTo < 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 获取当前时间
	 * @param formatStr (default:yyyyMMdd)
	 * @return	now dateTimeString
	 */
	public static String getNowTime(String formatStr){
		if(StringUtils.isBlank(formatStr))
			formatStr = FORMAT03;
		SimpleDateFormat sdf = new SimpleDateFormat( formatStr );
		return sdf.format(new Date());
	}
	
	/**
	 * 获取 明天的 这个时候
	 * @param dateFormatStr 时间格式(default:yyyyMMddHHmmss)
	 * @return Time String
	 * @throws Exception
	 */
	public static String getTomorrowTime(String dateFormatStr) throws Exception{
		if(StringUtils.isBlank(dateFormatStr))
			dateFormatStr = FORMAT02;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormatStr );
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**
	 * 获取相隔多久后的时间
	 * @param field		时间类型（月：Calendar.MONTH；日：Calendar.DAY_OF_YEAR； ...）
	 * @param amount	时间变量（之前<0;之后>0）
	 * @param dateFormatStr 时间格式(default:yyyyMMddHHmmss)
	 * @return	Time String
	 * @throws Exception
	 */
	public static String getTimeDifference(int field, int amount, String dateFormatStr) throws Exception{
		if(StringUtils.isBlank(dateFormatStr))
			dateFormatStr = FORMAT02;
		SimpleDateFormat sdf=new SimpleDateFormat(dateFormatStr);  
		Date date=new Date();  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);  
		calendar.add(field, amount);  
		date = calendar.getTime(); 
		return sdf.format(date);
	}
	
	/**
	 * 判断字符串是否为时间格式
	 * 
	 * @param dateStr
	 * @param formatStr
	 *            时间格式(default:yyyyMMdd)
	 * @return
	 */
	public static boolean isValidDate(String dateStr, String formatStr) {
		if (StringUtils.isBlank(formatStr))
			formatStr = FORMAT03;
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期
			format.setLenient(false);
			Date date = format.parse(dateStr);// 转换成日期
			if(!dateStr.equals(format.format(date)))// 对比转换后的日期字符串
				convertSuccess = false;
		} catch (ParseException e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

}
