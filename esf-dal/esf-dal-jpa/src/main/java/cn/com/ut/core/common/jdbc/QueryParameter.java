package cn.com.ut.core.common.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wuxiaohua
 * @since 2018/7/16
 */
@Data
@AllArgsConstructor
public class QueryParameter {

	private String name;
	private Object value;

	public static QueryParameter build(String name, Object value) {

		return new QueryParameter(name, value);
	}
}
