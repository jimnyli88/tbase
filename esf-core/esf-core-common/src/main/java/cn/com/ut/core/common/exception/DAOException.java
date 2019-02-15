package cn.com.ut.core.common.exception;
/**
* 数据库异常
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class DAOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1939686586971715397L;

	/**
	 *
	 */
	public DAOException() {

		super();
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public DAOException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 *
	 * @param message
	 */
	public DAOException(String message) {

		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public DAOException(Throwable cause) {

		super(cause);
	}

}
