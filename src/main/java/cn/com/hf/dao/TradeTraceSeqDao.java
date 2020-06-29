/**
 * llin 2019年4月13日下午7:06:43
 */
package cn.com.hf.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author llin
 * 用于请求流水自增序列
 */
@Component
public class TradeTraceSeqDao {
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public String getTraceSeq(){
		
		String sql = "select v_verifytrace_seq.nextval as vuserid from dual";
		long vuserid = 0 ;
		try {
			vuserid = jdbcTemplate.queryForObject(sql, long.class );
		} catch (Exception e) {
			logger.warn("获取v_verifytrace_seq数据异常");
		}
		
		return vuserid + "";
	}

}
