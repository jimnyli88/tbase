package cn.com.ut.core.dal.beans;

public class ORMapperElement extends BaseElement {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String table;
	private String schema;

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

}
