package cn.com.ut.core.common.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段前缀组装
 * @author wuxiaohua
 * @since 2015-6-16
 */
public class FieldPrefixBuilder {

	/**
	 * fieldPrefix
	 */
	private Map<String, String> fieldPrefix = new HashMap<String, String>();

	/**
	 *
	 */
	private FieldPrefixBuilder() {

	}

	/**
	 *
	 * @return FieldPrefixBuilder
	 */
	public static FieldPrefixBuilder builder() {

		return new FieldPrefixBuilder();
	}

	/**
	 * 获取fieldPrefix
	 * @return 返回一个map
	 */
	public Map<String, String> getFieldPrefix() {

		return fieldPrefix;
	}

	/**
	 * 给指定字段加指定前缀
	 * @param prefix
	 * @param fields
	 * @return FieldPrefixBuilder
	 */
	public FieldPrefixBuilder append(String prefix, String... fields) {

		if (fields != null && prefix != null) {
			for (String field : fields) {
				fieldPrefix.put(field, prefix);
			}
		}
		return this;
	}

}
