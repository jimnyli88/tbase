package cn.com.ut.core.cache.session;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.com.ut.core.cache.CacheHelper;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.system.beans.User;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.converter.DateTimeUtil;
import cn.com.ut.core.net.HostAddress;

@Component("sessionManager")
public class SessionManagerImpl implements SessionManager {

	private static final Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

	public static final String KEY_BEGIN_SESSION = "s/";
	public static final String KEY_BEGIN_VERIFICATION = "v/";
	private static String local = ConstantUtil.LOOPBACK;
	/**
	 * 微信OPENid前缀
	 */
	public static final String KEY_BEGIN_OPEN = "WEIXINOPEN/";

	/**
	 * 全局缓存时间，超长，用于主机、创作软件
	 */
	public static final int LONG_EXPIRE_SECONDS_GLOBAL_SESSION = 60 * 20;

	/**
	 * 全局缓存时间
	 */
	public static final int EXPIRE_SECONDS_GLOBAL_SESSION = 60 * 20;
	/**
	 * APP会话缓存时间
	 */
	public static final int EXPIRE_SECONDS_APP_SESSION = 60 * 60 * 24 * 10;

	/**
	 * 局部缓存时间
	 */
	public static final int EXPIRE_SECONDS_LOCAL_SESSION = 60 * 15;

	public static final int EXPIRE_SECONDS_GLOBAL_VERIFICATION = 60 * 10;

	/**
	 * 业务类别
	 */
	private String serviceType;
	/**
	 * 业务服务器端口
	 */
	private String serviceServerPort;
	/**
	 * 本机服务器网络地址
	 */
	private HostAddress hostAddress;

	@Autowired
	private Environment env;
	@Autowired(required = false)
	private CacheHelper cacheHelper;

	@PostConstruct
	public void init() {

		serviceType = env.getProperty("spring.config.name");
		serviceServerPort = env.getProperty("server.port");

		try {
			local = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}

		hostAddress = new HostAddress(local, serviceServerPort, serviceType);
	}

	@Override
	public User loadSessionFromCache(User user) {

		User userSession = getAndTouchSession(user.getSessionId());
		if (userSession != null) {
			userSession.setPosition(user.getPosition());
			userSession.setArea(user.getArea());
			return userSession;
		} else {
			return user;
		}

	}

	/**
	 * 设置全局会话缓存
	 * 
	 * @param key
	 *            会话key
	 * @param user
	 *            会话对象
	 * @return 操作结果
	 */
	public boolean addGlobalVerification(String key, User user) {

		return cacheHelper.set(KEY_BEGIN_VERIFICATION + key, EXPIRE_SECONDS_GLOBAL_VERIFICATION,
				user);
	}

	/**
	 * 从全局缓存获取验证码
	 * 
	 * @param key
	 *            缓存key
	 * @return 验证码对象
	 */
	public User getGlobalVerification(String key) {

		return cacheHelper.get(KEY_BEGIN_VERIFICATION + key);
	}

	/**
	 * 设置会话缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @param user
	 *            会话对象
	 */
	public void addSession(String sessionKey, User user) {

		user.getHosts().add(hostAddress);
		boolean b = addGlobalSession(sessionKey, user);
	}

	/**
	 * 设置会话全局缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @param user
	 *            会话对象
	 * @return 操作结果
	 */
	public boolean addGlobalSession(String sessionKey, User user) {

		String logonType = user.getSessionKey().getUserType();
		int expire = EXPIRE_SECONDS_GLOBAL_SESSION;
		if (ConstantUtil.USER_LOGON_TYPE_IE.equals(logonType)) {

		} else if (ConstantUtil.USER_LOGON_TYPE_APP.equals(logonType)) {
			expire = EXPIRE_SECONDS_APP_SESSION;
		} else {
			expire = LONG_EXPIRE_SECONDS_GLOBAL_SESSION;
		}
		Timestamp timeout = new Timestamp(DateTimeUtil.addSeconds(new Date(), expire).getTime());
		user.getSessionKey().setTimeout(timeout);
		return cacheHelper.set(KEY_BEGIN_SESSION + sessionKey, expire, user);
	}

	/**
	 * 更新会话全局缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @param user
	 *            会话对象
	 * @return 操作结果
	 */
	public boolean replaceGlobalSession(String sessionKey, User user) {

		String logonType = user.getSessionKey().getUserType();
		int expire = EXPIRE_SECONDS_GLOBAL_SESSION;
		if (ConstantUtil.USER_LOGON_TYPE_IE.equals(logonType)) {

		} else if (ConstantUtil.USER_LOGON_TYPE_APP.equals(logonType)) {
			expire = EXPIRE_SECONDS_APP_SESSION;
		} else {
			expire = LONG_EXPIRE_SECONDS_GLOBAL_SESSION;
		}
		return cacheHelper.replace(KEY_BEGIN_SESSION + sessionKey, expire, user);
	}

	/**
	 * 清除会话全局缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @return 操作结果
	 */
	public boolean removeGlobalSession(String sessionKey) {

		return cacheHelper.delete(KEY_BEGIN_SESSION + sessionKey);
	}

	/**
	 * 获取会话全局缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @return 会话对象
	 */
	public User getGlobalSession(String sessionKey) {

		User user = cacheHelper.get(KEY_BEGIN_SESSION + sessionKey);
		if (user != null && user.getSessionKey() != null) {
			String logonType = user.getSessionKey().getUserType();
			logger.debug("getGlobalSession logonType {}", logonType);
			if (!ConstantUtil.USER_LOGON_TYPE_APP.equals(logonType)) {
				cacheHelper.touch(KEY_BEGIN_SESSION + sessionKey, EXPIRE_SECONDS_GLOBAL_SESSION);
			}

		}
		return user;
	}

	/**
	 * 清除会话缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 */
	public void clearAllSession(String sessionKey) {

		removeGlobalSession(sessionKey);
	}

	/**
	 * 更新会话缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @param user
	 *            会话对象
	 */
	public void replaceAllSession(String sessionKey, User user) {

		User newUser = getGlobalSession(sessionKey);
		if (newUser != null && user != null) {
			boolean b = replaceGlobalSession(sessionKey, user);
			logger.debug("replaceGlobalSession result {} by key {}", b,
					KEY_BEGIN_SESSION + sessionKey);
		}
	}

	/**
	 * 更新会话缓存
	 * 
	 * @param sessionKey
	 *            会话旧key
	 * @param sessionKey
	 *            会话新key
	 * @param user
	 *            会话对象
	 */
	public void replaceAllSession(String sessionKey, String sessionKeyNew, User user) {

		if (sessionKey.equals(sessionKeyNew)) {
			replaceAllSession(sessionKey, user);
		} else {

			User newUser = getGlobalSession(sessionKey);
			if (newUser != null && user != null) {
				user.setSessionId(sessionKeyNew);
				removeGlobalSession(sessionKey);
				addGlobalSession(sessionKeyNew, user);
			}
		}
	}

	/**
	 * 获取会话缓存
	 * 
	 * @param sessionKey
	 *            会话key
	 * @return 会话对象
	 */
	public User getAndTouchSession(String sessionKey) {

		return getGlobalSession(sessionKey);
	}

	@Override
	public String getWeiXinOpenIdBySessionId(String sessionId) {

		return cacheHelper.get(KEY_BEGIN_OPEN + sessionId);
	}

	@Override
	public boolean setWeiXinOpenIdBySessionId(String sessionId, String openId) {

		if (CommonUtil.isEmpty(sessionId, openId)) {
			return false;
		}
		return cacheHelper.set(KEY_BEGIN_OPEN + sessionId, 60 * 60 * 5, openId);
	}

	@Override
	public User loadSessionFromCache(String sessionId) {

		return cacheHelper.get(KEY_BEGIN_SESSION + sessionId);
	}

	@Override
	public void addGlobalSession(User user, int times) {

		cacheHelper.add(KEY_BEGIN_SESSION + user.getSessionId(), times, user);
	}

}
