package cn.com.ut.core.cache.session;

import cn.com.ut.core.common.system.beans.User;

public interface SessionManagerRemote {

	public User getGlobalVerification(String key);

	public void clearAllSession(String sessionKey);

	public boolean addGlobalSession(String sessionKey, User user);

}