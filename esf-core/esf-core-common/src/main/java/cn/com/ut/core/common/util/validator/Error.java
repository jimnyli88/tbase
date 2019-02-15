package cn.com.ut.core.common.util.validator;

/**
 * 系统错误消息实体
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class Error {

	/**
	 * 错误编码
	 */
	private String errKey;
	/**
	 * 错误消息
	 */
	private String errMsg;

	/**
	 * 
	 * @return 错误编码
	 */
	public String getErrKey() {

		return errKey;
	}

	/**
	 * 
	 * @param errKey
	 */
	public void setErrKey(String errKey) {

		this.errKey = errKey;
	}

	/**
	 * 
	 * @return 错误消息
	 */
	public String getErrMsg() {

		return errMsg;
	}

	/**
	 * 
	 * @param errMsg
	 */
	public void setErrMsg(String errMsg) {

		this.errMsg = errMsg;
	}

	/**
	 * 
	 * @param errMsg
	 */
	public Error(String errMsg) {

		this.errMsg = errMsg;
	}

	public Error() {

	}

	/**
	 * 
	 * @param errKey
	 * @param errMsg
	 */
	public Error(String errKey, String errMsg) {

		this(errMsg);
		this.errKey = errKey;
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
		result = prime * result + ((errKey == null) ? 0 : errKey.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Error)) {
			return false;
		}
		Error other = (Error) obj;
		if (errKey == null) {
			if (other.errKey != null) {
				return false;
			}
		} else if (!errKey.equals(other.errKey)) {
			return false;
		}
		return true;
	}

}
