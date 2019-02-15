package cn.com.ut.core.restful;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.jdbc.PageBean;
import cn.com.ut.core.common.jdbc.PageBuilder;
import cn.com.ut.core.common.util.converter.TypeConvert;

/**
 * 将响应结果转为json
 * 
 * @author wuxiaohua
 * @since 2016年12月30日 下午2:08:01
 */
public class Result2JsonPreHandle {

	private static boolean primitive(Object value) {

		if (value instanceof java.util.Date || value instanceof Byte || value instanceof Integer
				|| value instanceof Float || value instanceof Double || value instanceof Long
				|| value instanceof BigInteger || value instanceof BigDecimal) {
			return true;
		}
		return false;

	}

	public static void preHandle(Map<String, Object> vo, Map<String, String> dict) {

		preHandle(vo, dict, null);
	}

	public static void preHandle(List<Map<String, Object>> vos, Map<String, String> dict,
			PageBean page) {

		preHandle(vos, dict, page, null);
	}

	public static void preHandle(Map<String, Object> vo, Map<String, String> dict,
			Set<String> convertFields) {

		preHandle(vo, dictionary(dict), dict, convertFields);
	}

	public static void preHandle(List<Map<String, Object>> vos, Map<String, String> dict,
			PageBean page, Set<String> convertFields) {

		preHandle(vos, dictionary(dict), dict, page, convertFields);
	}

	private static Map<String, Map<String, Object>> dictionary(Map<String, String> dict) {

		Map<String, Map<String, Object>> dictEntry = new HashMap<String, Map<String, Object>>();
		if (dict == null)
			return dictEntry;
		Collection<String> dictTypes = dict.values();
		if (dictTypes != null) {
			for (String dictType : dictTypes) {
				// todo
			}
		}
		return dictEntry;

	}

	private static void preHandle(Map<String, Object> vo,
			Map<String, Map<String, Object>> dictEntry, Map<String, String> dict,
			Set<String> convertFields) {

		Set<Entry<String, Object>> entrySet = vo.entrySet();
		Entry<String, Object> entry = null;
		Iterator<Entry<String, Object>> iter = entrySet.iterator();
		Map<String, Object> appendVo = new LinkedHashMap<String, Object>();
		while (iter.hasNext()) {
			entry = iter.next();
			Object value = entry.getValue();
			if (convertFields != null && primitive(value)
					&& (convertFields.isEmpty() || convertFields.contains(entry.getKey()))) {
				value = TypeConvert.getStringValue(value);
				vo.put(entry.getKey(), value);
			}
			String dictType = null;
			if (dict != null && (dictType = dict.get(entry.getKey())) != null) {
				Map<String, Object> dictTypeVo = dictEntry.get(dictType);
				if (dictTypeVo == null) {
					appendVo.put(entry.getKey() + ConstantUtil.DICTIONARY_POSTFIX_LOWER, value);
				} else {
					Object valueText = dictTypeVo.get((String) value);
					appendVo.put(entry.getKey() + ConstantUtil.DICTIONARY_POSTFIX_LOWER,
							valueText == null ? "" : String.valueOf(valueText));
				}
			}
		}
		if (!appendVo.isEmpty()) {
			vo.putAll(appendVo);
		}
	}

	private static void preHandle(List<Map<String, Object>> vos,
			Map<String, Map<String, Object>> dictEntry, Map<String, String> dict, PageBean page,
			Set<String> convertFields) {

		boolean isPage = (page != null);
		int rownum = isPage ? page.getStartIndex() + 1 : 1;
		if (vos != null && !vos.isEmpty()) {
			for (Map<String, Object> vo : vos) {
				preHandle(vo, dictEntry, dict, convertFields);
				if (isPage)
					vo.put(PageBuilder.ROWNUM, rownum++);
			}
		}
	}

}
