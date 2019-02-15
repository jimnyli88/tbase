package cn.com.ut.core.common.util.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * bean 属性工具类
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class BeanPropertyUtil {

	/**
	 * 支持的类型
	 */
	public static final List<Class<?>> typeFilter = Arrays.asList(new Class<?>[] {
			java.sql.Timestamp.class, java.sql.Date.class, java.sql.Time.class,
			java.math.BigDecimal.class, byte[].class, String.class });

	/**
	 * 支持的类型
	 */
	public static final List<Class<?>> typePrimitive = Arrays.asList(new Class<?>[] {
			java.lang.Integer.class, java.lang.Boolean.class, java.lang.Character.class,
			java.lang.Byte.class, java.lang.Short.class, java.lang.Long.class,
			java.lang.Float.class, java.lang.Double.class });

	/**
	 * 不支持的类型
	 */
	public static final List<Class<?>> typeDisable = Arrays
			.asList(new Class<?>[] { java.lang.Class.class });

	/**
	 * 获取指定对象的属性
	 * @param bean
	 * @return  PropertyDescriptor[]
	 */
	public static PropertyDescriptor[] getPropertyDescriptorFilter(Object bean) {

		List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
		if (pds != null) {
			for (PropertyDescriptor pd : pds) {
				if (pd.getPropertyType().isPrimitive() || typeFilter.contains(pd.getPropertyType()))
					list.add(pd);
			}

		}

		return list.toArray(new PropertyDescriptor[list.size()]);
	}

	/**
	 * 获取指定属性的对象，并映射为object返回
	 * @param bean
	 * @param pd
	 * @return Object or null
	 */
	public static Object getPropertyValue(Object bean, PropertyDescriptor pd) {

		try {
			return PropertyUtils.getProperty(bean, pd.getName());
		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {

		}
		return null;
	}

	/**
	 * 判断两个实例的类型是否一致
	 * @param o1
	 * @param o2
	 * @return 两个实例的类型是否一致
	 */
	public static boolean compareType(Object o1, Object o2) {

		return true;
	}

	public static void main(String[] args) {
		String name="updateUserId";
	}
}
