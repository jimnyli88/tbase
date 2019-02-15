package cn.com.ut.core.common.system.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会话key实体
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class SessionKey implements Serializable {

	private static final long serialVersionUID = 25944893040244771L;

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 会话id
	 */
	private String sessionId;

	/**
	 * 超时时间
	 */
	private Timestamp timeout;
	/**
	 * 会话状态
	 */
	private int status;
	/**
	 * 用户类型
	 */
	private String userType;
	/**
	 * 客户端id
	 */
	private String clientId;

	/**
	 *
	 * @return 客户端id
	 */
	public String getClientId() {

		return clientId;
	}

	/**
	 *
	 * @param clientId
	 */
	public void setClientId(String clientId) {

		this.clientId = clientId;
	}

	/**
	 *
	 * @return clientId
	 */
	public String getUserType() {

		return userType;
	}

	/**
	 *
	 * @param userType
	 */
	public void setUserType(String userType) {

		this.userType = userType;
	}

	/**
	 *
	 */
	public SessionKey() {

	}

	/**
	 *
	 * @param sessionId
	 */
	public SessionKey(String sessionId) {

		this.sessionId = sessionId;
	}

	/**
	 *
	 * @return 会话状态
	 * 
	 */
	public int getStatus() {

		return status;
	}

	/**
	 *
	 * @param status
	 */
	public void setStatus(int status) {

		this.status = status;
	}

	/**
	 *
	 * @return 会话状态
	 */
	public Timestamp getTimeout() {

		return timeout;
	}

	/**
	 *
	 * @param timeout
	 */
	public void setTimeout(Timestamp timeout) {

		this.timeout = timeout;
	}

	/**
	 *
	 * @return 会话状态
	 */
	public String getUserId() {

		return userId;
	}

	/**
	 *
	 * @param userId
	 */
	public void setUserId(String userId) {

		this.userId = userId;
	}

	/**
	 *
	 * @return 会话id
	 */
	public String getSessionId() {

		return sessionId;
	}

	/**
	 *
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {

		this.sessionId = sessionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionKey other = (SessionKey) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

}
