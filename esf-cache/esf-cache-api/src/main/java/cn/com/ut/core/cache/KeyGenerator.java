package cn.com.ut.core.cache;

/**
 * 缓存主键生成规则
 * 
 * @author wuxiaohua
 * @since 2014-3-18
 */
public class KeyGenerator {

	public static final int NO_PARAM_KEY = 0;
	public static final int NULL_PARAM_KEY = 53;

	public static Object generate(Object[] params) {

		Object key = NO_PARAM_KEY;

		if (params == null || params.length == 0) {
			return String.valueOf(key);
		} else if (params.length == 1) {
			key = (params[0] == null ? NULL_PARAM_KEY : params[0]);
			return String.valueOf(key);
		}

		int hashCode = 17;
		for (Object object : params) {
			hashCode = 31 * hashCode + (object == null ? NULL_PARAM_KEY : object.hashCode());
		}
		key = hashCode;
		return String.valueOf(key);
	}
}
