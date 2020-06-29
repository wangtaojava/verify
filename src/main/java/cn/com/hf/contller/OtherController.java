package cn.com.hf.contller;

import cn.com.hf.verify.config.VerifyConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OtherController
 * @Description TODO
 * @Author wangtao
 * @Date 2020/5/11 11:10
 */
@Controller
public class OtherController {

    @RequestMapping("/other")
    public String other(ModelMap mode, HttpServletRequest request) {
        String session_loginUser = (String) request.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
        if (StringUtils.isEmpty(session_loginUser)) {
            mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
            return "index";
        }
        return "other";
    }
}