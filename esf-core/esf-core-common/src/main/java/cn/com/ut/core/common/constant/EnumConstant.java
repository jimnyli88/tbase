package cn.com.ut.core.common.constant;

/**
 * 常量枚举
 * 
 * @author wuxiaohua
 * @since 2014-10-30
 *
 */
public interface EnumConstant {

	/**
	 * 排序
	 */
	enum OrderBy {
		/**
		 * 升序，降序
		 */
		ASC, DESC;
	}

	/**
	 * Where 条件
	 */
	enum WhereCase {
		/**
		 * EQ, 等于; NE, 不等于; GT, 大于; LT, 小于; GE, 大于等于; LE, 小于等于; IN, in 范围;
		 * BETWEEN, between 区间; LIKE, 模糊匹配; LLIKE, 左匹配; RLIKE, 右匹配; NULL, is
		 * null; NOTNULL, is not null; TRUE, and 1=1; FALSE or 1=2.
		 */
		EQ, NE, GT, LT, GE, LE, IN, BETWEEN, LIKE, LLIKE, RLIKE, NULL, NOTNULL, TRUE, FALSE, TERMS, EXISTS, NESTED, NOTRANGE, NOTEXISTS, NOTIN, NOTTERMS, PREFIX, LIMIT, REGEXP, MUSTEQ, MUSTNE, MUSTRANGE, NESTEDRANGE, NESTEDEQ, NESTEDIN, NESTEDGT, NESTEDLT, NESTEDGE, NESTEDLE, GEOPOLYGON;
	}

	enum SqlType {

		LONG, INT, DOUBLE, FLOAT, DATE, DATETIME, DECIMAL, STRING
	}

	enum DbAction {
		SELECT, INSERT, DELETE, UPDATE;
	}

	enum TableColumn {
		ID, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, CREATE_NAME, UPDATE_NAME, IS_DEL;
	}

	/**
	 * 大小写样式
	 */
	enum UpperLowerCase {
		PLAIN, UPPER, LOWER
	}

}
