/**
 * llin 2019年4月13日下午12:43:18
 */
package cn.com.hf.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author llin
 * 配置错误页面
 */

@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {
 
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[5];
        errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
        errorPages[1] = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/405.html");
        errorPages[2] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
        errorPages[3] = new ErrorPage(HttpStatus.BAD_GATEWAY, "/502.html");
        errorPages[4] = new ErrorPage(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "/505.html");
 
        registry.addErrorPages(errorPages);
    }
}
