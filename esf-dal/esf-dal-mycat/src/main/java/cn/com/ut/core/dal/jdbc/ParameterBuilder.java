package cn.com.ut.core.dal.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.ut.core.common.util.CommonUtil;

/**
 * 参数构建
 * 
 * @author wuxiaohua
 * @since 2015-3-4
 */
public class ParameterBuilder {

	/**
	 * sql参数值集合
	 */
	private List<Object> parameters = new ArrayList<Object>();
	/**
	 * sql查询字段集合
	 */
	private List<String> selectColumns = new ArrayList<String>();

	private ParameterBuilder() {

	}

	/**
	 * 实例构建器
	 * 
	 * @return 实例
	 */
	public static ParameterBuilder builder() {

		return new ParameterBuilder();
	}

	public List<Object> getParameters() {

		return parameters;
	}

	public List<String> getSelectColumns() {

		return selectColumns;
	}

	/**
	 * 给查询字段追加表别名前缀
	 * 
	 * @param prefix
	 *            表别名前缀
	 * @param names
	 *            查询字段数组
	 * @return this
	 */
	public ParameterBuilder appendColumns(String prefix, String... names) {

		if (names != null) {
			if (prefix != null) {
				for (String name : names) {
					selectColumns.add(prefix + "." + name);
				}
			} else {
				for (String name : names) {
					selectColumns.add(name);
				}
			}
		}
		return this;
	}

	/**
	 * 追加指定参数的值
	 * 
	 * @param valueObject
	 *            参数值集合
	 * @param names
	 *            参数名数组
	 * @return this
	 */
	public ParameterBuilder append(Map<String, ? extends Object> valueObject, String... names) {

		if (valueObject != null && names != null) {
			for (String name : names) {
				parameters.add(valueObject.get(name));
			}
		}
		return this;
	}

	/**
	 * 往参数值集合添加一组参数值
	 * 
	 * @param es
	 *            参数值数组
	 * @return this
	 */
	public ParameterBuilder append(Object... es) {

		if (es != null) {
			for (Object e : es) {
				parameters.add(e);
			}
		}
		return this;
	}

	/**
	 * 往参数值集合按索引位置添加参数值
	 * 
	 * @param index
	 *            索引位置
	 * @param e
	 *            参数值
	 * @return this
	 */
	public ParameterBuilder append(int index, Object e) {

		parameters.add(index, e);
		return this;
	}

	/**
	 * 字段集合转换为字段数组
	 * 
	 * @return 字段数组
	 */
	public String[] toColumns() {

		return selectColumns.toArray(new String[selectColumns.size()]);
	}

	/**
	 * 参数值集合转换为参数值数组
	 * 
	 * @return 参数值数组
	 */
	public Object[] toArray() {

		return parameters.toArray();
	}

	/**
	 * 构建数据表字段数组
	 * 
	 * @param names
	 *            字符串数组
	 * @return 数据表字段数组
	 */
	public static String[] buildColumn(String[] names) {

		if (names == null || names.length == 0)
			return names;
		String[] array = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			if (CommonUtil.isEmpty(names[i]))
				throw new RuntimeException();
			String[] both = names[i].split("@");
			array[i] = both[0];
		}
		return array;
	}

	/**
	 * 构建条件字段数组
	 * 
	 * @param names
	 *            字符串数组
	 * @return 条件字段数组
	 */
	public static String[] buildParameter(String[] names) {

		if (names == null || names.length == 0)
			return names;
		String[] array = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			if (CommonUtil.isEmpty(names[i]))
				throw new RuntimeException();
			String[] both = names[i].split("@");
			if (both.length == 2)
				array[i] = both[1];
			else
				array[i] = both[0];
		}
		return array;
	}
}