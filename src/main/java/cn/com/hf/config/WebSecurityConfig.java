/**
 * llin 2019年4月3日下午5:26:28
 */
package cn.com.hf.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.com.hf.verify.config.VerifyConfig;

/**
 * @author llin 拦截器配置
 */

//@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

	@Bean
	public SecurityInterceptor getSecurityInterceptor() {
		return new SecurityInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

		// 排除配置
		addInterceptor.excludePathPatterns("/index");
		addInterceptor.excludePathPatterns("/login");
		addInterceptor.excludePathPatterns("/register");
		// 拦截配置
		addInterceptor.addPathPatterns("/**");
		addInterceptor.addPathPatterns("/**/verify");
	}

	private class SecurityInterceptor extends HandlerInterceptorAdapter {
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws IOException {
			HttpSession session = request.getSession();

			// 判断是否已有该用户登录的session
			if (session.getAttribute(VerifyConfig.SYSTEM_SESSION_NAME) != null) {
				return true;
			}
			// 跳转到登录页
			String url = "index";
			response.sendRedirect(url);
			return false;
		}

	}

}
