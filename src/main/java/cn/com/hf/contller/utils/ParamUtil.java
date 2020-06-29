/**  
 * All rights Reserved, Designed By www.tydic.com
 * @Title: ParamUtil.java   
 * @Package: cn.com.hf.contller.utils   
 * @Description: 
 * @author: wangtao 
 * @date: 2019年11月14日 下午1:45:33
 */
package cn.com.hf.contller.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.hf.dao.ParamDao;
import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.tools.TimeTool;

/**
 * @ClassName: ParamUtil
 * @Description:
 * @author: wangtao
 * @date: 2019年11月14日 下午1:45:33
 */
@Component
public class ParamUtil {

	private static final String key = "EasyPay.PARAM.SEQUENCE";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired // 操作字符串的template，StringRedisTemplate是RedisTemplate的一个子集
	private StringRedisTemplate jedisTemplate;
	@Autowired
	private ParamDao paramDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = SystemException.class)
	public Map<String, String> getParam(String chk, String username, String mername) throws Exception {
		// 生成channelid，merid，termid，渠道密钥，签名密钥
		String channelid = "6161616";
		String merid = "MER0213";
		String termid = "TE";
		String sequence = getSequence();
		channelid = channelid + sequence;
		merid = merid + sequence;
		termid = termid + sequence.substring(2, 8);
		logger.info(username + "操作\r\n" + "channelid=====>" + channelid + ",merid=====>" + merid + ",termid=====>"
				+ termid);
		String channelkey = getRandomCharAndNumr(14);// 渠道密钥
		String signkey = getRandomCharAndNumr(33);// 签名密钥
		// 获取当前时间
		String timeStr = TimeTool.getNowTime(TimeTool.FORMAT02);// 获取当前时间

		// 保存wtp_verify_mer_info商户表
		paramDao.insert_wtp_verify_mer_info(channelid, channelkey, signkey, merid, termid, timeStr, username, mername);

		// 保存mps_verify_money商户金额表
		paramDao.insert_mps_verify_money(channelid, username);
		// 保存商户功能单价表和映射表
		paramDao.insert_mps_channelid_price(chk, channelid, merid, timeStr, username);
		Map<String, String> map = new HashMap<String, String>();
		map.put("channelid", channelid);
		map.put("merid", merid);
		map.put("termid", termid);
		map.put("channelkey", channelkey);
//		map.put("signkey", signkey);
		map.put("address", "https://notify-test.eycard.cn:7443/EasypayMPSystem/~");
		return map;
	}

	public String getSequence() {
		// 获取当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Date date = new Date();
		String formatDate = sdf.format(date);

		// 获取自增序列，存24小时，之后重置从0开始
		RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, jedisTemplate.getConnectionFactory());
		Long increment = entityIdCounter.getAndIncrement();
		DecimalFormat df = new DecimalFormat("00");
		String sequence = formatDate + df.format(increment);
		logger.info("序列号=====>" + sequence);
		entityIdCounter.expire(1, TimeUnit.DAYS);
		return sequence;
	}

	public static void main(String[] args) {
	}

	public String getRandomCharAndNumr(Integer length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b) { // 字符串
				// int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
				str += (char) (97 + random.nextInt(26));// 取得大写字母
			} else { // 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}
}
