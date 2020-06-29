/**
 * llin 2019年4月10日下午9:53:22
 */
package cn.com.hf.contller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author llin
 */
@Controller
public class IndexController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping("/index")
	public String register(ModelMap map) {
		return "index";
	}
	
}
