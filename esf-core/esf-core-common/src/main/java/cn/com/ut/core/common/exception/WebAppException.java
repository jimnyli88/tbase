package cn.com.ut.core.common.exception;
/**
* web异常
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class WebAppException extends Exception {

	/**
	 *
	 */
	public WebAppException() {

		super();
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WebAppException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public WebAppException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 *
	 * @param message
	 */
	public WebAppException(String message) {

		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public WebAppException(Throwable cause) {

		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3439042893876249436L;
	

}
