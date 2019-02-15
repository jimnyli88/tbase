package cn.com.ut.core.common.system.beans;

import java.io.Serializable;

/**
 * 请求解析
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class RequestHeader implements Serializable {

	private static final long serialVersionUID = -5031418914838624731L;

	/**
	 * referer
	 */
	public static final String REFERER = "referer";
	/**
	 * x-forwarded-for
	 */
	public static final String X_FORWARDED_FOR = "x-forwarded-for";

	/**
	 * x-requested-with
	 */
	public static final String X_REQUESTED_WITH = "x-requested-with";

	/**
	 * x-requested-with-ajax
	 */
	public static final String X_REQUESTED_WITH_AJAX = "XMLHttpRequest";

	/**
	 * xRequestedWith
	 */
	private String xRequestedWith;

	/**
	 * xForwardedFor
	 */
	private String xForwardedFor;
	/**
	 * referer
	 */
	private String referer;
	/**
	 * localAddr
	 */
	private String localAddr;
	/**
	 * remoteAddr
	 */
	private String remoteAddr;
	/**
	 * scheme
	 */
	private String scheme;

	/**
	 * 请求域名
	 */
	private String serverName;

	/**
	 * nginx配置的请求端口
	 */
	private int serverPort;

	public String getxRequestedWith() {

		return xRequestedWith;
	}

	public void setxRequestedWith(String xRequestedWith) {

		this.xRequestedWith = xRequestedWith;
	}

	/**
	 *
	 * @return scheme
	 */
	public String getScheme() {

		return scheme;
	}

	/**
	 *
	 * @param scheme
	 */
	public void setScheme(String scheme) {

		this.scheme = scheme;
	}

	/**
	 *
	 * @return localAddr
	 */
	public String getLocalAddr() {

		return localAddr;
	}

	/**
	 *
	 * @param localAddr
	 */
	public void setLocalAddr(String localAddr) {

		this.localAddr = localAddr;
	}

	/**
	 *
	 * @return remoteAddr
	 */
	public String getRemoteAddr() {

		return remoteAddr;
	}

	/**
	 *
	 * @param remoteAddr
	 */
	public void setRemoteAddr(String remoteAddr) {

		this.remoteAddr = remoteAddr;
	}

	/**
	 *
	 * @return xForwardedFor
	 */
	public String getxForwardedFor() {

		return xForwardedFor;
	}

	/**
	 *
	 * @param xForwardedFor
	 */
	public void setxForwardedFor(String xForwardedFor) {

		this.xForwardedFor = xForwardedFor;
	}

	/**
	 *
	 * @return referer
	 */
	public String getReferer() {

		return referer;
	}

	/**
	 *
	 * @param referer
	 */
	public void setReferer(String referer) {

		this.referer = referer;
	}

	public String getServerName() {

		return serverName;
	}

	public void setServerName(String serverName) {

		this.serverName = serverName;
	}

	public int getServerPort() {

		return serverPort;
	}

	public void setServerPort(int serverPort) {

		this.serverPort = serverPort;
	}

}
