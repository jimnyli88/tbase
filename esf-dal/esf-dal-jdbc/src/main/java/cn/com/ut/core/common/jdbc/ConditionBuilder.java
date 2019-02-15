package cn.com.ut.core.common.jdbc;

import cn.com.ut.core.common.constant.ConstantUtil;
/**
* 条件组装
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class ConditionBuilder {

	/**
	 * AND operator
	 * 
	 * @param a
	 * @param b
	 * @return StringBuilder
	 */
	public static StringBuilder andOperator(StringBuilder a, StringBuilder b) {

		StringBuilder c = new StringBuilder();
		c.append(ConstantUtil.Expression.LEFT_BRACKET);
		c.append(a);
		c.append(ConstantUtil.STR_SPACE);
		c.append(ConstantUtil.Expression.AND);
		c.append(ConstantUtil.STR_SPACE);
		c.append(b);
		c.append(ConstantUtil.Expression.RIGHT_BRACKET);
		return c;
	}

	/**
	 * OR operator
	 * 
	 * @param a
	 * @param b
	 * @return StringBuilder
	 */
	public static StringBuilder orOperator(StringBuilder a, StringBuilder b) {

		StringBuilder c = new StringBuilder();
		c.append(ConstantUtil.Expression.LEFT_BRACKET);
		c.append(a);
		c.append(ConstantUtil.STR_SPACE);
		c.append(ConstantUtil.Expression.OR);
		c.append(ConstantUtil.STR_SPACE);
		c.append(b);
		c.append(ConstantUtil.Expression.RIGHT_BRACKET);
		return c;
	}
}
