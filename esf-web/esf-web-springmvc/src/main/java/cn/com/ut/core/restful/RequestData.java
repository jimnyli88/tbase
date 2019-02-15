package cn.com.ut.core.restful;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestData extends RestData implements Serializable {

	private static final long serialVersionUID = 1499957645219341174L;

	private Map<String, Object> params = new LinkedHashMap<>();
	private transient Object[] valueArray;
	private int paramIndex = 0;

	public RequestData() {

	}

	public static RequestData build() {

		RequestData requestData = new RequestData();
		return requestData;
	}

	public RequestData addParam(String paramName, Object paramValue) {

		params.put(paramName, paramValue);
		return this;
	}

	public RequestData addParamIndex(String paramName, Object paramValue) {

		params.put(paramIndex + "@" + paramName, paramValue);
		paramIndex++;
		return this;
	}

	public RequestData addParams(Map<String, ? extends Object> params) {

		this.params.putAll(params);
		return this;
	}

	public Map<String, Object> getParams() {

		return params;
	}

	public <T> T getParamValue(int index) {

		if (valueArray == null)
			valueArray = params.values().toArray();

		if (index > -1 && index < valueArray.length)
			return (T) valueArray[index];
		else
			return null;

	}

	public Integer getIntParamValue(int index) {

		return (Integer) getParamValue(index);
	}

	public Long getLongParamValue(int index) {

		return (Long) getParamValue(index);
	}

	public java.util.Date getDateParamValue(int index) {

		return (java.util.Date) getParamValue(index);
	}

	public BigDecimal getDecimalParamValue(int index) {

		return (BigDecimal) getParamValue(index);
	}

	public String getStringParamValue(int index) {

		return (String) getParamValue(index);
	}

	public Integer getIntParamValue(String paramName) {

		return (Integer) getParamValue(paramName);
	}

	public Long getLongParamValue(String paramName) {

		return (Long) getParamValue(paramName);
	}

	public java.util.Date getDateParamValue(String paramName) {

		return (java.util.Date) getParamValue(paramName);
	}

	public BigDecimal getDecimalParamValue(String paramName) {

		return (BigDecimal) getParamValue(paramName);
	}

	public String getStringParamValue(String paramName) {

		return (String) getParamValue(paramName);
	}

	public <T> T getParamValue(String paramName) {

		return (T) params.get(paramName);
	}

	public <T> T getParamValue(String paramName, Class<T> clazz) {

		return getParamValue(paramName);
	}

}
