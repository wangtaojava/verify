/**
 * llin 2019年4月13日下午3:00:16
 */
package cn.com.hf.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.tools.TimeTool;
import sun.misc.BASE64Encoder;

/**
 * @author llin
 */
@Component
public class UserInfoDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void insertUserInfoManage(String userName, String passWord, String phoneNo) throws SystemException {

		String timeStr = TimeTool.getNowTime(TimeTool.FORMAT02);// 获取当前时间

		try {
			StringBuilder sbd = new StringBuilder(
					"insert into v_user_info (v_user_id, v_user_login_pwd, v_user_name, v_user_tel, v_user_info_modification_time) values ('");
			sbd.append(userName).append("','").append(str2base64(passWord)).append("','").append(userName).append("','")
					.append(phoneNo).append("','").append(timeStr).append("')");

//			logger.info("---保存v_user_info:" + sbd.toString());
			int resultInt = jdbcTemplate.update(sbd.toString());
			if (resultInt != 1) {
				logger.warn("保存v_user_info数据库异常");
				throw new SystemException("注册异常");
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw new SystemException("系统异常");
		}
	}

	/**
	 * 判断用户是否存在
	 */
	public boolean selectUserInfoManage(String userName) {
		boolean flag = false;

		if (StringUtils.isBlank(userName))
			return flag;

		String sql = "select v_user_status from v_user_info where v_user_id = '" + userName + "'";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		if (queryForList != null && queryForList.size() > 0) {
			Map<String, Object> map = queryForList.get(0);
			if (map != null && map.size() > 0) {
				flag = true;
			}
		}

		return flag;
	}

	public void selectUserInfoManage(String userName, String passWord) throws SystemException {

		if (StringUtils.isBlank(userName) || StringUtils.isBlank(passWord))
			throw new SystemException("用户/密码不能为空");

		StringBuilder sbd = new StringBuilder(
				"select v_user_login_pwd,v_user_status from v_user_info where v_user_id = '");
		sbd.append(userName).append("'");

//		logger.info("SEECT:" + sbd.toString());
		try {
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sbd.toString());
			if (queryForList != null && queryForList.size() > 0) {
				Map<String, Object> map = queryForList.get(0);
				if (map != null && map.size() > 0) {

					Object object_pwd = map.get("v_user_login_pwd");
					Object object_user_status = map.get("v_user_status");// 用户可用状态

					if (object_pwd != null) {
						String pwd = object_pwd.toString();
						if (StringUtils.isNotBlank(pwd) && !pwd.equals(str2base64(passWord)))
							throw new SystemException("用户密码错误");

						// 更新用户登录状态，并获取用于登录IP
						// ...

					}

					if (object_user_status != null) {
						String user_status = object_user_status.toString();
						if (!"1".equals(user_status)) {
							throw new SystemException("该用户不可用,请联系站务人员(llin@easypay.com.cn或wangtao@easypay.com.cn)");
						}
					}
				} else {
					throw new SystemException("用户不存在");
				}
			} else {
				throw new SystemException("用户不存在");
			}
		} catch (Exception e) {
			if (e instanceof SystemException)
				throw new SystemException(((SystemException) e).getMsg());
			else
				logger.error(null, e);
		}
	}

	/**
	 * 将字符串转换成base64编码格式的字符串
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
