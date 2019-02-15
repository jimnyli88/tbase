package cn.com.ut.core.common.exception;

/**
 * 业务层异常
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -429840854615386777L;

	/**
	 *
	 */
	public ServiceException() {

		super();
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 *
	 * @param message
	 */
	public ServiceException(String message) {

		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public ServiceException(Throwable cause) {

		super(cause);
	}

}
