package cn.com.ut.auth.exception;

/**
 * 验证签名异常
 * 
 * @author wuxiaohua
 * @since 2017年10月11日
 */
public class AuthException extends RuntimeException {
	private static final long serialVersionUID = -429840854615386777L;

	/**
	 *
	 */
	public AuthException() {

		super();
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AuthException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public AuthException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 *
	 * @param message
	 */
	public AuthException(String message) {

		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public AuthException(Throwable cause) {

		super(cause);
	}

}
