/**
 * llin 2019年4月13日下午2:38:00
 */
package cn.com.hf.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.tools.TimeTool;

/**
 * @author llin
 */
@Component
public class UserDao {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void insertUserManage(String userName,String userIpaddess) throws SystemException{
		
		String timeStr = TimeTool.getNowTime(TimeTool.FORMAT02);// 获取当前时间
		
		StringBuilder sbd = new StringBuilder("insert into v_user (V_USER_ID, V_USER_LOGIN_IP, V_USER_CREATION_TIME) values ('");
		sbd.append(userName).append("','")
			.append(userIpaddess).append("','")
			.append(timeStr).append("')");
		
			try {
//				logger.info("---保存v_user:"+sbd.toString());
				int resultInt = jdbcTemplate.update(sbd.toString());
				if(resultInt != 1){
					logger.warn("保存v_user数据库异常");
					throw new SystemException("注册异常");
				}
			} catch (Exception e) {
				logger.error(null , e);
				throw new SystemException("系统异常");
			}
		
	}

}
