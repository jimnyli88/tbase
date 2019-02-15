package cn.com.ut.core.common.util.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.com.ut.core.common.exception.ValidateException;
import cn.com.ut.core.common.util.ArrayUtil;
import cn.com.ut.core.common.util.CollectionUtil;

/**
 * 验证工具类
 * 
 * @author: lanbin
 * @since: Apr 6, 2017
 */
public class ValidatorUtil {

	/**
	 * 检查是否缺少必要参数且不为空（若缺少则抛出验证异常并提示缺少哪些参数）
	 * 
	 * @see cn.com.ut.core.common.util.CollectionUtil#checkMapContainsKey
	 * @param map
	 *            被检查的map
	 * @param keys
	 *            必要参数
	 * @throws ValidateException
	 */
	public static void validateMapContainsKey(Map<String, ? extends Object> map, String... keys)
			throws ValidateException {

		if (CollectionUtil.isEmptyMap(map)) {
			if (ArrayUtil.isEmptyArray(keys)) {
				throw new ValidateException("参数空，缺少必要参数");
			} else {
				throw new ValidateException("参数空，缺少必要参数：" + Arrays.asList(keys));
			}
		}
		List<String> missKeys = CollectionUtil.checkMapContainsKey(map, keys);
		if (missKeys != null) {
			throw new ValidateException("缺少必要参数：" + missKeys);
		}
	}

	/**
	 * 检查必填参数
	 * 
	 * @param values
	 *            参数值
	 * @param keys
	 *            参数名
	 */
	public static void requiredFieldMiss(Object[] values, String... keys) {

		boolean isMiss = false;
		List<String> missKeys = new ArrayList<String>();
		if (ArrayUtil.isEmptyArray(values)) {
			isMiss = true;
			if (!ArrayUtil.isEmptyArray(keys)) {
				missKeys = Arrays.asList(keys);
			}
		}

		for (int i = 0; i < values.length; i++) {
			boolean currentMiss = false;
			if (values[i] == null) {
				currentMiss = true;
			} else if ("".equals(values[i].toString().trim())) {
				currentMiss = true;
			}
			isMiss = isMiss || currentMiss;
			if (currentMiss) {
				if (i < keys.length - 1) {
					String key = keys[i];
					if (key != null) {
						missKeys.add(key);
					}
				}
			}
		}

		if (isMiss) {
			if (CollectionUtil.isEmptyCollection(missKeys)) {
				throw new ValidateException("缺少必要参数");
			} else {
				throw new ValidateException("缺少必要参数：" + missKeys);
			}
		}
	}
}
