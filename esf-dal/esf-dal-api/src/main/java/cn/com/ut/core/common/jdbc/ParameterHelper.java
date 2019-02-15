package cn.com.ut.core.common.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.converter.DateTimeUtil;
import cn.com.ut.core.common.util.converter.TypeConvert;
/**
* 参数解析
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class ParameterHelper {

	/**
	 * 将指定字符串中的大写字符替换成小写的，并在前面追加下划线
	 * 例如：zhangHaili,返回zhang_haili
	 * @param name
	 * @return String
	 */
	public static String underscoreName(String name) {

		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	

	/**
	 * Map.Entry到对象属性值的复制
	 * 
	 * @param object
	 * @param map
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T populateValueObject(Class<T> clazz, Map<String, String> map)
			throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Constructor<T> constructor = clazz.getConstructor();
		// boolean accessible = constructor.isAccessible();
		T obj = constructor.newInstance();

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);

		Map<String, PropertyDescriptor> mappedFields = new HashMap<String, PropertyDescriptor>();
		Set<String> mappedProperties = new HashSet<String>();
		for (PropertyDescriptor pd : pds) {
			Method method = null;
			if ((method = pd.getWriteMethod()) != null) {
				mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					mappedFields.put(underscoredName, pd);
				}
				mappedProperties.add(pd.getName());
				Object value = map.get(underscoredName);
				if (value != null)
					method.invoke(obj, TypeConvert.convert(value, pd.getPropertyType()));
			}
		}

		return obj;

	}

	/**
	 * 将对象映射为Map
	 * @param t
	 * @return 指定对象的map集合
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Map<String, Object> populateMap(T t) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		if (t == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());

		for (PropertyDescriptor pd : pds) {
			Method method = null;
			if ((method = pd.getReadMethod()) != null) {
				String underscoredName = underscoreName(pd.getName());
				Object value = method.invoke(t);
				map.put(underscoredName, value);
			}
		}
		return map;
	}

	/**
	 * 从map中获取指定key的value列表
	 * @param map
	 * @param parameters
	 * @param nullEnable
	 * @param useUUID
	 * @param useCreateTime
	 * @param useUpdateTime
	 * @return List<Object>
	 */
	public static List<Object> getParameterFromMap(Map<String, Object> map, String[] parameters,
			boolean nullEnable, boolean useUUID, boolean useCreateTime, boolean useUpdateTime) {

		List<Object> array = new ArrayList<Object>();

		if ((map != null && !map.isEmpty()) && (parameters != null && parameters.length > 0)) {

			Object temp = null;
			for (String parameter : parameters) {
				temp = map.get(parameter);
				if (temp == null && !nullEnable)
					throw new RuntimeException("Not null parameter enable");
				array.add(temp);
			}
		}

		if (useUUID)
			array.add(CommonUtil.getUUID());

		if (useCreateTime || useUpdateTime) {
			Timestamp time = DateTimeUtil.currentDateTime();
			if (useCreateTime)
				array.add(time);
			if (useUpdateTime)
				array.add(time);
		}

		return array;
	}

	/**
	 * 从指定的对象中获取指定属性的值列表
	 * @param t
	 * @param parameters
	 * @param nullEnable
	 * @param useUUID
	 * @param useCreateTime
	 * @param useUpdateTime
	 * @return List<Object>
	 */
	public static List<Object> getParameterFromBeanProperty(Object t, String[] parameters,
			boolean nullEnable, boolean useUUID, boolean useCreateTime, boolean useUpdateTime) {

		List<Object> array = new ArrayList<Object>();

		if (t != null && (parameters != null && parameters.length > 0)) {

			Object temp = null;
			for (String parameter : parameters) {
				try {
					temp = PropertyUtils.getProperty(t, parameter);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}

				if (temp == null && !nullEnable)
					throw new RuntimeException("Not null parameter enable");
				array.add(temp);
			}

		}

		if (useUUID)
			array.add(CommonUtil.getUUID());

		if (useCreateTime || useUpdateTime) {
			Timestamp time = DateTimeUtil.currentDateTime();
			if (useCreateTime)
				array.add(time);
			if (useUpdateTime)
				array.add(time);
		}

		return array;
	}

}
