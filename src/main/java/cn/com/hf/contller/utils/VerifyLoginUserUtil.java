/**
 * llin 2019年4月19日下午3:19:38
 */
package cn.com.hf.contller.utils;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.com.hf.bean.LoginBean;
import cn.com.hf.dao.UserInfoDao;
import cn.com.hf.exception.SystemException;
import cn.com.hf.verify.config.VerifyConfig;
import cn.com.hf.verify.tools.MD5EcryptUtil;

/**
 * @author llin
 *	登录安全检验
 */
@Component
public class VerifyLoginUserUtil {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired // 操作字符串的template，StringRedisTemplate是RedisTemplate的一个子集
	private StringRedisTemplate jedisTemplate;
	@Autowired
	UserInfoDao userInfoDao;
	
	public void verifyLoginUser(String session_loginUser, String ipAddress) throws SystemException{
		
		boolean loginFlag = userInfoDao.selectUserInfoManage(session_loginUser);// 查询数据库，判断用户是否存在
		if(!loginFlag)
			throw new SystemException("您还未登录,请先登录!");
		
		// 查询redis中是否存在该用户
		getLoginUser(session_loginUser, ipAddress);
		
	}
	
	public void verifyLoginUser(LoginBean bean) throws SystemException{
		
		String userName = bean.getUserName().trim();
		String passWord = bean.getPassWord().trim();
		String loginIpSite = bean.getLoginIpSite().trim();
		
		userInfoDao.selectUserInfoManage(userName, passWord);// 查询数据库，判断用户的合法性
		
		// 查询redis中是否存在该用户
		getLoginUser(userName, loginIpSite);
		
	}
	
	/**
	 * 更改用户在数据中的状态、存放登录用户信息至redis、session中
	 * @param bean
	 * @param session
	 */
	public void setLoginUser(LoginBean bean, HttpSession session){
		// 更改用户在数据中的状态
		
		// 存放登录用户信息至redis中
		String userName = bean.getUserName().trim();
		String loginIpSite = bean.getLoginIpSite().trim();
		String redisKey = generateRedisKey(userName);
		jedisTemplate.opsForValue().set(redisKey, loginIpSite);
		
		// 存放至session中
		session.setAttribute(VerifyConfig.SYSTEM_SESSION_NAME, userName);
		
		jedisTemplate.expire(redisKey, 10, TimeUnit.MINUTES); 
		
	}
	
	public void getLoginUser(String userName, String ipAddress) throws SystemException{
		String redisKey = generateRedisKey(userName);
		
		String loginIpSite = jedisTemplate.opsForValue().get(redisKey);
		if(StringUtils.isNotBlank(ipAddress) && StringUtils.isNotBlank(loginIpSite) && !ipAddress.equals(loginIpSite))
			throw new SystemException("用户已在" + loginIpSite + "登录！");
		
	}
	
	public void removeLoginUser(HttpSession session){
		
		String session_loginUser = (String) session.getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		if(StringUtils.isBlank(session_loginUser))
			return;
		
		// 移除session
		session.removeAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
		
		// 移除redis
		String redisKey = generateRedisKey(session_loginUser);
		jedisTemplate.delete(redisKey);
		
		// 更新数据中用户状态
		// ...
		
	}
	
	private String generateRedisKey(String userName){
		String key = StringUtils.EMPTY;
		
		userName = userName.trim();
		if(StringUtils.isBlank(userName))
			return key;
		
		try {
			userName = userName + "=" + userName;
			key = MD5EcryptUtil.md5ToUpperCase(userName.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error(null, e);
		}
			
		return key;
	}

}
