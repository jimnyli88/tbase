package cn.com.ut.core.dal.jdbc;

import java.util.List;

@Deprecated
public class SqlParams {

	private String sql;
	private Object[] params;
	private List<Object[]> paramsList;
	private String key;

	public SqlParams(String sql) {

		this.sql = sql;
	}

	public SqlParams(String sql, Object[] params) {

		this.sql = sql;
		this.params = params;
	}

	public SqlParams(String sql, Object[] params, String key) {

		this.sql = sql;
		this.params = params;
		this.key = key;
	}

	public SqlParams(String sql, List<Object[]> paramsList) {

		this.sql = sql;
		this.paramsList = paramsList;
	}

	public List<Object[]> getParamsList() {

		return paramsList;
	}

	public void setParamsList(List<Object[]> paramsList) {

		this.paramsList = paramsList;
	}

	public String getSql() {

		return sql;
	}

	public void setSql(String sql) {

		this.sql = sql;
	}

	public Object[] getParams() {

		return params;
	}

	public void setParams(Object[] params) {

		this.params = params;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

}
