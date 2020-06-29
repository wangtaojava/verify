/**
 * llin 2019年4月13日下午3:47:07
 */
package cn.com.hf.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.tools.DesTool;

/**
 * @author llin
 * 	鉴权流水
 */
@Component
public class AuthenticationPipelineDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void insertAuthenticationPipelineManage(String userName, String verifyType, String tradeTrace,
			Map<String, String> verifyDataMap) throws SystemException {

		try {
			StringBuilder sbd = new StringBuilder(
					"insert into v_authentication_pipeline (v_authentication_opar_id, v_authentication_function_id, v_authentication_requst_pline, v_authentication_database) values ('");
			sbd.append(userName).append("','").append(verifyType).append("','").append(tradeTrace).append("','")
					.append(getMap2DesStr(verifyDataMap)).append("')");

//			logger.info("---保存v_authentication_pipeline:" + sbd.toString());
			int resultInt = jdbcTemplate.update(sbd.toString());
			if (resultInt != 1) {
				logger.warn("保存v_authentication_pipeline数据库异常");
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw new SystemException("系统异常");
		}
	}

	private String getMap2DesStr(Map<String, String> verifyDataMap) {
		String key = "ilovehf.";
		DesTool desTool = DesTool.getInstance();
		return desTool.getEncString(verifyDataMap.toString(), key);
	}

}
