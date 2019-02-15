package cn.com.ut.core.restful;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wuxiaohua
 * @since 2018/7/2
 */
@NoArgsConstructor
@Setter
@Getter
public class RequestInfo extends RestInfo {

	private static final long serialVersionUID = -4408094072237995883L;
	private Object data;

	public static RequestInfo build() {

		RequestInfo inputData = new RequestInfo();
		return inputData;
	}

	public <T> RequestInfo appendData(T data) {

		this.setData(data);
		return this;
	}
}
