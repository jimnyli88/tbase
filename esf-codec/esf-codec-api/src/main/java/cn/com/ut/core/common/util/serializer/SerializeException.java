package cn.com.ut.core.common.util.serializer;
/**
* 序列化异常
* @author wuxiaohua
* @since 2013-12-22下午2:27:50
*/
public class SerializeException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8206259053570499162L;

	public SerializeException() {

		super();
		// TODO Auto-generated constructor stub
	}

	public SerializeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SerializeException(String message, Throwable cause) {

		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SerializeException(String message) {

		super(message);
		// TODO Auto-generated constructor stub
	}

	public SerializeException(Throwable cause) {

		super(cause);
		// TODO Auto-generated constructor stub
	}

}
