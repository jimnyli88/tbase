package cn.com.ut.core.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import cn.com.ut.core.common.constant.EnumConstant;

/**
 * Map集合操作工具类
 * 
 * @author wuxiaohua
 * @since 2017年4月21日 上午9:39:57
 */
public class MapUtil {

	@Test
	public void testReplaceMapKeyToUpperLowerCaseErr() {

		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map1.put("aa", "11");
		map1.put("AA", "12");
		map1.put("BB", "22");
		map1.put("map2", map2);

		map2.put("cc", "33");
		map2.put("DD", "44");
		map2.put("map3", map3);

		map3.put("ee", "55");
		map3.put("FF", "66");

		replaceMapKeyToUpperLowerCase(map1, EnumConstant.UpperLowerCase.LOWER);

		System.out.println(map1);
	}

	@Test
	public void testReplaceMapKeyToUpperLowerCase() {

		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map1.put("aa", "11");
		map1.put("BB", "22");
		map1.put("map2", map2);

		map2.put("cc", "33");
		map2.put("DD", "44");
		map2.put("map3", map3);

		map3.put("ee", "55");
		map3.put("FF", "66");

		replaceMapKeyToUpperLowerCase(map1, EnumConstant.UpperLowerCase.LOWER);

		System.out.println(map1);
	}

	/**
	 * 统一替换Map集合中的key的大小写情况，如果Map集合中存在Map类型元素，支持递归
	 * 
	 * @param fromList
	 * @param upperLowerCase
	 */
	public static <T extends Object> List<Map<String, T>> replaceMapKeyToUpperLowerCase(
			List<Map<String, T>> fromList, EnumConstant.UpperLowerCase upperLowerCase) {

		if (CollectionUtil.isEmptyCollection(fromList)
				|| EnumConstant.UpperLowerCase.PLAIN.equals(upperLowerCase)) {
			return fromList;
		}

		List<Map<String, T>> toList = new ArrayList<Map<String, T>>();
		for (Map<String, T> from : fromList) {
			toList.add(replaceMapKeyToUpperLowerCase(from, upperLowerCase));
		}
		return toList;

	}

	/**
	 * 统一替换Map集合中的key的大小写情况，如果Map集合中存在Map类型元素，支持递归
	 * 
	 * @param from
	 * @param upperLowerCase
	 */
	public static <T extends Object> Map<String, T> replaceMapKeyToUpperLowerCase(
			Map<String, T> from, EnumConstant.UpperLowerCase upperLowerCase) {

		if (CollectionUtil.isEmptyMap(from)
				|| EnumConstant.UpperLowerCase.PLAIN.equals(upperLowerCase)) {
			return from;
		}

		if (from instanceof LinkedCaseInsensitiveMap) {
			from = new LinkedHashMap<String, T>(from);
		}

		List<String> removeKeys = new ArrayList<String>();
		List<String> nextKeys = new ArrayList<String>();
		Set<Entry<String, T>> entrySet = from.entrySet();
		for (Entry<String, T> entry : entrySet) {
			String key1 = entry.getKey();
			String key2 = EnumConstant.UpperLowerCase.LOWER.equals(upperLowerCase)
					? key1.toLowerCase() : key1.toUpperCase();
			T t = entry.getValue();
			if (!key1.equals(key2)) {
				removeKeys.add(key1);
			}

			if (t instanceof Map) {
				nextKeys.add(key2);
			}
		}

		for (String removeKey : removeKeys) {

			String key3 = EnumConstant.UpperLowerCase.LOWER.equals(upperLowerCase)
					? removeKey.toLowerCase() : removeKey.toUpperCase();
			if (from.containsKey(key3)) {
				throw new RuntimeException("err");
			}
			T t = from.remove(removeKey);
			from.put(key3, t);
		}

		for (String nextKey : nextKeys) {
			from.put(nextKey, (T) replaceMapKeyToUpperLowerCase((Map<String, T>) from.get(nextKey),
					upperLowerCase));
		}

		return from;
	}

	/**
	 * 对象中属性和属性值转换为Map中键值对
	 * 
	 * @param source
	 *            源对象
	 * @return 目标Map
	 */
	public static Map<String, Object> objToMap(Object source) {

		if (source == null) {
			return null;
		}
		PropertyDescriptor[] pd = BeanUtils.getPropertyDescriptors(source.getClass());
		if (pd == null || pd.length < 1) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		for (PropertyDescriptor targetPd : pd) {
			Method readMethod = targetPd.getReadMethod();
			// 排除父类Object中声明的getClass()方法
			if (Object.class.getName().equals(readMethod.getDeclaringClass().getName())) {
				continue;
			}
			String name = targetPd.getName();
			try {
				Object value = readMethod.invoke(source);
				resultMap.put(name, value);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return resultMap;
	}
}
