/**
 * llin 2019年4月15日下午5:26:45
 */
package cn.com.hf.verify.tools;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.hf.dao.AuthenticationPipelineDao;
import cn.com.hf.dao.asynchronous.AuthenticationPipelineDaoAsyn;

/**
 * @author llin 异步保存流水
 */
@Component
public class VerifyTradeListDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AuthenticationPipelineDao authenticationPipelineDao;

	public void runPreservation(String userName, String verifyType, String tradeTrace,
			Map<String, String> verifyDataMap) {
		ExecutorService service = Executors.newFixedThreadPool(5);
		AuthenticationPipelineDaoAsyn task = new AuthenticationPipelineDaoAsyn(userName, verifyType, tradeTrace,
				verifyDataMap, authenticationPipelineDao);
		service.execute(task);
	}

}
