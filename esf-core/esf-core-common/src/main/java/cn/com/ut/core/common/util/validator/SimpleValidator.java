package cn.com.ut.core.common.util.validator;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import cn.com.ut.core.common.exception.ValidateException;

/**
 * 简单校验工具类型
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class SimpleValidator {

	/**
	 * 对象是否为null值或空字符串
	 * 
	 * @param value
	 * @return 是否为null值或空字符串
	 */
	public static boolean isEmpty(String value) {

		boolean b = false;
		if (value == null || value.trim().length() == 0)
			b = true;
		return b;
	}

	/**
	 * 字符串字面值是否为整数
	 * 
	 * @param value
	 * @param emptyEnable
	 * @param throwException
	 * @return 字符串字面值是否为整数
	 */
	public static boolean isInteger(String value, boolean emptyEnable, boolean throwException) {

		boolean b = true;

		if (isEmpty(value)) {
			if (emptyEnable)
				return true;
			else
				return false;
		}

		try {
			int i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			b = false;
			if (throwException) {
				throw new ValidateException(e);
			}
		}

		return b;
	}

	public static void main(String[] args) {

		long l = System.currentTimeMillis();
		Date date = new Date(l);
		Timestamp dt = new Timestamp(l);
		Time t = new Time(l);

		System.out.println(date.getTime());
		System.out.println(dt.getTime());
		System.out.println(t.getTime());

	}

}
