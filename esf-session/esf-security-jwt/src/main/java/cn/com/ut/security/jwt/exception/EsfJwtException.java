package cn.com.ut.security.jwt.exception;

public class EsfJwtException extends RuntimeException {

	private static final long serialVersionUID = 4913779018489273275L;

	public EsfJwtException() {
		super();
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EsfJwtException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public EsfJwtException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 *
	 * @param message
	 */
	public EsfJwtException(String message) {

		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public EsfJwtException(Throwable cause) {

		super(cause);
	}

}
