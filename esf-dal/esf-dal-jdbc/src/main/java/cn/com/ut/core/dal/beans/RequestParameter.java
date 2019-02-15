package cn.com.ut.core.dal.beans;

/**
 * 参数传递
 * @author wuxiaohua
 * @since 2014-3-7
 * @param <T>
 */
public class RequestParameter<T> {

	private T parameter;

	public RequestParameter() {

	}

	public RequestParameter(T t) {

		this.parameter = t;
	}

	public T getParameter() {

		return parameter;
	}

	public void setParameter(T parameter) {

		this.parameter = parameter;
	}

	public static void main(String[] args) {

		RequestParameter<String> p = new RequestParameter<String>();
	}
}
