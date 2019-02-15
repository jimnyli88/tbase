package cn.com.ut.core.common.util.converter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.constant.EnumConstant;
import cn.com.ut.core.common.util.ArrayUtil;
import cn.com.ut.core.common.util.CommonUtil;

/**
 * 类型转换工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class TypeConvert {

	/**
	 * 支持转换的类型集合
	 */
	private static Map<String, Class<?>> classType = new HashMap<String, Class<?>>();

	/**
	 * 初始化支持转换的类型集合
	 */
	static {
		classType.put("int", int.class);
		classType.put("long", long.class);
		classType.put("double", double.class);
		classType.put("float", float.class);
		classType.put("string", String.class);
		classType.put("byte", byte.class);
		classType.put("date", java.sql.Date.class);
		classType.put("datetime", java.sql.Timestamp.class);
		classType.put("time", java.sql.Time.class);

	}

	/**
	 * str to Object
	 * 
	 * @param value
	 * @param type
	 * @param format
	 * @return Object or null
	 */
	public static Object str2Obj(String value, String type, String format) {

		Object obj = null;
		if (CommonUtil.isEmpty(value))
			return null;
		else if (CommonUtil.isEmpty(type) || TypeConvert.classType.get(type) == null)
			return value;
		String[] values = null;
		if (value.indexOf(",") != -1) {
			values = value.split(",");
		}

		try {
			if (values == null)
				obj = ConvertUtils.convert(value, TypeConvert.classType.get(type));
			else
				obj = ConvertUtils.convert(values, TypeConvert.classType.get(type));
		} catch (ConversionException e) {
			;
		}

		return obj;
	}

	/**
	 * 类型转换
	 * 
	 * @param value
	 * @param type
	 * @return Object or null
	 */
	public static Object convert(Object value, Class<?> type) {

		if (value == null || type == null)
			return value;
		return ConvertUtils.convert(value, type);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 *            可选类型date,datetime,int,double
	 * @return Object or null
	 */
	public static Object convert(String value, String type) {

		Object result = null;

		if (CommonUtil.isEmpty(value))
			return null;
		if (CommonUtil.isEmpty(type))
			return value;

		if (EnumConstant.SqlType.DATE.name().equalsIgnoreCase(type)) {
			result = DateTimeUtil.dateFormat(value, null);
		} else if (EnumConstant.SqlType.DATETIME.name().equalsIgnoreCase(type)) {
			result = DateTimeUtil.dateTimeFormat(value, null);
		} else if (EnumConstant.SqlType.INT.name().equalsIgnoreCase(type)) {
			result = Integer.parseInt(value);
		} else if (EnumConstant.SqlType.DOUBLE.name().equalsIgnoreCase(type)) {
			result = Double.parseDouble(value);
		} else if (EnumConstant.SqlType.FLOAT.name().equalsIgnoreCase(type)) {
			result = Float.parseFloat(value);
		} else if (EnumConstant.SqlType.LONG.name().equalsIgnoreCase(type)) {
			result = Long.parseLong(value);
		} else if (EnumConstant.SqlType.DECIMAL.name().equalsIgnoreCase(type)) {
			result = new BigDecimal(value);
		} else {
			result = value;
		}

		return result;

	}

	/**
	 * 获取指定对象的字符串
	 * 
	 * @param value
	 * @param utc
	 *            utc=true时将时间的毫秒数作为字符串返回
	 * @return String or ""
	 */
	public static String getStringValue(Object value, boolean utc) {

		if (value == null)
			return "";
		// 返回byte[]
		if (value.getClass().isArray() && value.getClass().getComponentType().equals(byte.class)) {
			return ArrayUtil.byte2HexStr((byte[]) value);
		}

		else if (value instanceof java.sql.Time) {
			if (utc)
				return String.valueOf(((java.sql.Time) value).getTime());
			else
				return new SimpleDateFormat(ConstantUtil.TIME_FORMAT).format(value);
		}

		else if (value instanceof java.sql.Date) {
			if (utc)
				return String.valueOf(((java.sql.Date) value).getTime());
			else
				return new SimpleDateFormat(ConstantUtil.DATE_FORMAT).format(value);
		}

		else if (value instanceof java.util.Date) {
			if (utc)
				return String.valueOf(((java.util.Date) value).getTime());
			else
				return new SimpleDateFormat(ConstantUtil.DATETIME_FORMAT).format(value);
		}

		else if (value instanceof java.sql.Timestamp) {
			if (utc)
				return String.valueOf(((java.sql.Timestamp) value).getTime());
			else
				return new SimpleDateFormat(ConstantUtil.DATETIME_FORMAT).format(value);
		}

		else {
			return value.toString();
		}
	}

	/**
	 * 获取指定对象的字符串
	 * 
	 * @param value
	 * @return String
	 */
	public static String getStringValue(Object value) {

		return getStringValue(value, false);
	}
}
