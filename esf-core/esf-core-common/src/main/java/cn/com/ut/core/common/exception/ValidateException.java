package cn.com.ut.core.common.exception;

/**
 * 校验异常
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class ValidateException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1415628179649115255L;

	public ValidateException() {

	}

	public ValidateException(String message) {

		super(message);
	}

	public ValidateException(Throwable cause) {

		super(cause);
	}

	public ValidateException(String message, Throwable cause) {

		super(message, cause);
	}

	public ValidateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

}
