package cn.com.ut.core.cache.session;

import cn.com.ut.core.common.system.beans.User;

public interface SessionManager extends SessionManagerRemote {


	public User getAndTouchSession(String sessionKey);

	public boolean addGlobalVerification(String key, User user);

	public User getGlobalVerification(String key);

	public void addSession(String sessionKey, User user);

	public void clearAllSession(String sessionKey);

	public void replaceAllSession(String sessionKey, User user);

	public void replaceAllSession(String sessionKey, String sessionKeyNew, User user);

	public boolean addGlobalSession(String sessionKey, User user);

	User loadSessionFromCache(User user);

	/**
	 * 根据sessionID获取微信的openid
	 * 
	 * @param sessionId
	 * @return
	 */
	public String getWeiXinOpenIdBySessionId(String sessionId);

	/**
	 * 设置微信OPENID 保存5小时
	 * 
	 * @param sessionId
	 * @param openId
	 * @return
	 */
	public boolean setWeiXinOpenIdBySessionId(String sessionId, String openId);

	User loadSessionFromCache(String sessionId);

	void addGlobalSession(User user, int times);
}
