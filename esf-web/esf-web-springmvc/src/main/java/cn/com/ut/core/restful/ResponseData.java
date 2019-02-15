package cn.com.ut.core.restful;

import java.io.Serializable;

public class ResponseData extends RestData implements Serializable {

	private static final long serialVersionUID = 7199884037353990205L;

	private Object value;

	public ResponseData() {

	}

	public static ResponseData build(Object value) {

		ResponseData responseData = new ResponseData();
		responseData.setValue(value);
		return responseData;
	}

	public <T> T getEntity() {

		return (T) value;
	}

	public Object getValue() {

		return value;
	}

	public void setValue(Object value) {

		this.value = value;
	}

}
