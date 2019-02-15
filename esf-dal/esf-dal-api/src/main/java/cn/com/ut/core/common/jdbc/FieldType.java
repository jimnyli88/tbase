package cn.com.ut.core.common.jdbc;
/**
* 字段类型实体
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class FieldType {

	/**
	 * name
	 */
	public String name;
	/**
	 * field
	 */
	public String field;
	/**
	 * type
	 */
	public String type;

	/**
	 *
	 */
	public FieldType() {

	}

	/**
	 *
	 * @param field
	 * @param name
	 */
	public FieldType(String field, String name) {

		this.field = field;
		this.name = name;
	}

	/**
	 *
	 * @param field
	 * @param name
	 * @param type
	 */
	public FieldType(String field, String name, String type) {

		this(field, name);
		this.type = type;
	}

	/**
	 *
	 * @return name
	 */
	public String getName() {

		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 *
	 * @return field
	 */
	public String getField() {

		return field;
	}

	/**
	 *
	 * @param field
	 */
	public void setField(String field) {

		this.field = field;
	}

	/**
	 *
	 * @return type
	 */
	public String getType() {

		return type;
	}

	/**
	 *
	 * @param type
	 */
	public void setType(String type) {

		this.type = type;
	}

}
