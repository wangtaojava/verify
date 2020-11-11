/**  
 * All rights Reserved, Designed By www.tydic.com
 * @Title: ParamDao.java   
 * @Package: cn.com.hf.dao   
 * @Description: 
 * @author: wangtao 
 * @date: 2019年11月14日 下午2:48:05
 */
package cn.com.hf.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.com.hf.exception.SystemException;

/**
 * @ClassName: ParamDao
 * @Description:
 * @author: wangtao
 * @date: 2019年11月14日 下午2:48:05
 */
@Component
public class ParamDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	// 保存商户表
	public void insert_wtp_verify_mer_info(String channelid, String channelkey, String keyinfo, String merid,
			String termid, String changedt, String username, String mername) throws SystemException {
		mername = mername + "-" + username;
		String sql = "insert into wtp_verify_mer_info (channel_id,is_in_use,channel_key,key_info,mer_id,termid,mer_name,many_channel,changedt,requestip) values ('"
				+ channelid + "','T','" + channelkey + "','" + keyinfo + "','" + merid + "','" + termid + "','"
				+ mername + "','','" + changedt + "','')";
		try {
			int resultInt = jdbcTemplate.update(sql);
			if (resultInt != 1) {
				logger.warn(username + "操作\r\n" + "保存wtp_verify_mer_info数据库异常");
				throw new SystemException("系统异常");
			}
		} catch (Exception e) {
			logger.error(username + "操作\r\n" + "保存wtp_verify_mer_info数据库异常", e);
			throw new SystemException("系统异常");
		}
	}

	// 保存商户金额表
	public void insert_mps_verify_money(String channelid, String username) throws SystemException {
		String sql = "insert into mps_verify_money (channel_id,verify_sum,verify_use,bank_price,operator_price,police_price,query_price,is_in_use,bank_price2,bank_price3,bank_price4) values ('"
				+ channelid + "','30','0','10','10','10','10','T','0','0','0')";
		try {
			int resultInt = jdbcTemplate.update(sql);
			if (resultInt != 1) {
				logger.warn(username + "操作\r\n" + "保存mps_verify_money数据库异常");
				throw new SystemException("系统异常");
			}
		} catch (Exception e) {
			logger.error(username + "操作\r\n" + "保存mps_verify_money数据库异常", e);
			throw new SystemException("系统异常");
		}
	}

	// 保存商户功能单价表和映射表
	public void insert_mps_channelid_price(String chk, String channelid, String merid, String changedt, String username)
			throws SystemException {
		List<String> sqllist = new ArrayList<String>();
		String[] chks = chk.split(",");
		for (int i = 0; i < chks.length; i++) {
			String sql = "insert into mps_channelid_price (channel_id,main_trade_type,trade_type,price) values ('"
					+ channelid + "','$type$','-','1')";

			String channelsql = "insert into MPS_MANYCHANNEL_INFO (TRADE_ID, MER_ID, TERM_ID, IS_IN_USE, MAIN_TRADE_TYPE, NEXT_MER_ID, NEXT_TERM_ID, NEXT_CHANNEL_TYPE, TRADE_TYPE, CHANGEDT,REMARK) values (MPS_MANYCHANNEL_INFO_seq.nextval,'"
					+ merid
					+ "','-','T','$maintradetype$','$nextmerid$','$nexttermid$','$nextchanneltype$', '$tradetype$','"
					+ changedt + "','')";
			switch (chks[i]) {
			case "1":// 银行卡信息鉴权
				sql = sql.replace("$type$", "0901");
				channelsql = channelsql.replace("$maintradetype$", "0901").replace("$nextmerid$", "ZW74V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "74")
						.replace("$tradetype$", "V");
				break;
			case "2":// 运营商信息鉴权
				sql = sql.replace("$type$", "0902");
				channelsql = channelsql.replace("$maintradetype$", "0902").replace("$nextmerid$", "ZW79V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "79")
						.replace("$tradetype$", "V");
				break;
			case "3":// 公安信息鉴权
				sql = sql.replace("$type$", "0903");
				channelsql = channelsql.replace("$maintradetype$", "0903").replace("$nextmerid$", "ZW74V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "74")
						.replace("$tradetype$", "V");
				break;
			case "4":// 风险信息鉴权
				String sql1 = sql.replace("$type$", "0913");// 营业执照
				String sql2 = sql.replace("$type$", "0912");// 身份证
				String sql3 = sql.replace("$type$", "0911");// 银行卡
				sqllist.add(sql1);
				sqllist.add(sql2);
				sqllist.add(sql3);
				sql = "";

				String channelsql1 = channelsql.replace("$maintradetype$", "0911")
						.replace("$nextmerid$", "ZW69V0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "69").replace("$tradetype$", "V");
				String channelsql2 = channelsql.replace("$maintradetype$", "0912")
						.replace("$nextmerid$", "ZW69V0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "69").replace("$tradetype$", "V");
				String channelsql3 = channelsql.replace("$maintradetype$", "0913")
						.replace("$nextmerid$", "ZW69V0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "69").replace("$tradetype$", "V");
				sqllist.add(channelsql1);
				sqllist.add(channelsql2);
				sqllist.add(channelsql3);
				channelsql = "";
				break;
			case "5":// OCR-银行卡
				sql = sql.replace("$type$", "1201");
				channelsql = channelsql.replace("$maintradetype$", "1201").replace("$nextmerid$", "ZW84R0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "76")
						.replace("$tradetype$", "R");
				break;
			case "6":// OCR-身份证
				sql = sql.replace("$type$", "1202");
				channelsql = channelsql.replace("$maintradetype$", "1202").replace("$nextmerid$", "ZW84R0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "76")
						.replace("$tradetype$", "R");
				break;
			case "7":// OCR-营业执照
				sql = sql.replace("$type$", "1204");
				channelsql = channelsql.replace("$maintradetype$", "1204").replace("$nextmerid$", "ZW84R0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "76")
						.replace("$tradetype$", "R");
				break;
			case "8":// OCR-人证对比
				sql = sql.replace("$type$", "1203");
				channelsql = channelsql.replace("$maintradetype$", "1203").replace("$nextmerid$", "ZW64R0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "64")
						.replace("$tradetype$", "R");
				break;
			case "9":// OCR-结果查询
				sql = sql.replace("$type$", "1200");
				channelsql = channelsql.replace("$maintradetype$", "1200").replace("$nextmerid$", "ZW63R0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "63")
						.replace("$tradetype$", "R");
				break;
			case "10":// 企业信息查询
				sql = sql.replace("$type$", "0931");
				channelsql = channelsql.replace("$maintradetype$", "0931").replace("$nextmerid$", "ZW77V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "77")
						.replace("$tradetype$", "V");
				break;
			case "11":// 企业信息鉴权
				sql = sql.replace("$type$", "0921");
				channelsql = channelsql.replace("$maintradetype$", "0921").replace("$nextmerid$", "ZW71V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "77")
						.replace("$tradetype$", "V");
				break;
			case "12":// 短信网关
				sql = sql.replace("$type$", "0941");
				channelsql = channelsql.replace("$maintradetype$", "0941").replace("$nextmerid$", "ZW83V0000000001")
						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "83")
						.replace("$tradetype$", "V");
				break;
			case "13":// 电子协议-接口转发
				String sql4 = sql.replace("$type$", "1351");// 电子协议-接口转发
				String sql5 = sql.replace("$type$", "1352");// 电子协议-短信验证码
				String sql6 = sql.replace("$type$", "1353");// 电子协议-合同下载
				sqllist.add(sql4);
				sqllist.add(sql5);
				sqllist.add(sql6);
				sql = "";

				String channelsql4 = channelsql.replace("$maintradetype$", "1351")
						.replace("$nextmerid$", "ZW66c0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "66").replace("$tradetype$", "c");
				String channelsql5 = channelsql.replace("$maintradetype$", "1352")
						.replace("$nextmerid$", "ZW83V0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "83").replace("$tradetype$", "V");
				String channelsql6 = channelsql.replace("$maintradetype$", "1353")
						.replace("$nextmerid$", "ZW66c0000000001").replace("$nexttermid$", "00000001")
						.replace("$nextchanneltype$", "66").replace("$tradetype$", "c");
				sqllist.add(channelsql4);
				sqllist.add(channelsql5);
				sqllist.add(channelsql6);
				channelsql = "";
//				sql = sql.replace("$type$", "1351");
//				channelsql = channelsql.replace("$maintradetype$", "1351").replace("$nextmerid$", "ZW66c0000000001")
//						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "66")
//						.replace("$tradetype$", "c");
				break;
//			case "14":// 电子协议-短信验证码
//				sql = sql.replace("$type$", "1352");
//				channelsql = channelsql.replace("$maintradetype$", "1352").replace("$nextmerid$", "ZW83V0000000001")
//						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "83")
//						.replace("$tradetype$", "V");
//				break;
//			case "15":// 电子协议-合同下载
//				sql = sql.replace("$type$", "1353");
//				channelsql = channelsql.replace("$maintradetype$", "1353").replace("$nextmerid$", "ZW66c0000000001")
//						.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "66")
//						.replace("$tradetype$", "c");
//				break;
				case "14": //活体检测 1205
					sql = sql.replace("$type$", "1205");
					channelsql = channelsql.replace("$maintradetype$", "1205").replace("$nextmerid$", "ZW95V0000000001")
							.replace("$nexttermid$", "00000001").replace("$nextchanneltype$", "95")
							.replace("$tradetype$", "R");
					break;
			}
			if (StringUtils.isNotBlank(sql)) {
				sqllist.add(sql);
			}
			if (StringUtils.isNotBlank(channelsql)) {
				sqllist.add(channelsql);
			}
		}

		try {
			String[] sqls = new String[sqllist.size()];
			sqllist.toArray(sqls);
			jdbcTemplate.batchUpdate(sqls);

		} catch (Exception e) {
			logger.error(username + "操作\r\n" + "保存mps_channelid_price和mps_manychannel_info异常", e);
			throw new SystemException("系统异常");
		}
	}
}
