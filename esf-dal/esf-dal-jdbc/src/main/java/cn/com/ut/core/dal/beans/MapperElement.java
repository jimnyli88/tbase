package cn.com.ut.core.dal.beans;

import java.util.Map;
import java.util.TreeMap;

public class MapperElement extends BaseElement {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String table;
	private String schema;
	private Map<String, SQLElement> sqlElements = new TreeMap<String, SQLElement>();

	public String getTable() {

		return table;
	}

	public void setTable(String table) {

		this.table = table;
	}

	public String getSchema() {

		return schema;
	}

	public void setSchema(String schema) {

		this.schema = schema;
	}

	public Map<String, SQLElement> getSqlElements() {

		return sqlElements;
	}

	public void setSqlElements(Map<String, SQLElement> sqlElements) {

		this.sqlElements = sqlElements;
	}

}
