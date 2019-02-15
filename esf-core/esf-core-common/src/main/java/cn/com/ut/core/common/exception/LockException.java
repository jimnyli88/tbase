package cn.com.ut.core.common.exception;

/**
 * @author wuxiaohua
 * @since 2018/11/9
 */
public class LockException extends RuntimeException {

	public LockException() {

		super();
	}

	public LockException(String message) {

		super(message);
	}

	public LockException(String message, Throwable cause) {

		super(message, cause);
	}

	public LockException(Throwable cause) {

		super(cause);
	}
}
