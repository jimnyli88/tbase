package cn.com.ut.core.cache;

import cn.com.ut.core.common.constant.ConstantUtil;
//import cn.com.ut.core.common.system.beans.User;

/**
 * Cache operation helper
 * 
 * @author wuxiaohua
 * @since 2014-3-19
 */
public interface CacheHelper {

	public static final int DEFAULT_EXPIRE = 60 * 20; // 分钟
	public static final int LONGTIME_EXPIRE = 60 * 60 * 24 * 7; // 分钟
	/**
	 * 是否开启缓存
	 */
	public static final String ENABLE_CACHE = ConstantUtil.FLAG_YES;

	boolean set(String key, Object value);

	boolean set(String key, int exp, Object value);

	/**
	 * 保存键值对至缓存中
	 * 
	 * @param key
	 *            键
	 * @param exp
	 *            过期时间
	 * @param value
	 *            值
	 * @param timeout
	 *            超时时间
	 * @return
	 */
	boolean set(String key, int exp, Object value, long timeout);

	boolean add(String key, Object value);

	boolean add(String key, int exp, Object value);

	boolean add(String key, int exp, Object value, long timeout);

	boolean replace(String key, Object value);

	boolean replace(String key, int exp, Object value);

	boolean replace(String key, int exp, Object value, long timeout);

	public <T> T getAndTouch(String key, int exp, long timeout);

	public <T> T getAndTouch(String key, int exp);

	public <T> T getAndTouch(String key);

	public boolean touch(String key, int exp, long timeout);

	public boolean touch(String key, int exp);

	<T> T get(String key);

	/**
	 * 按照key从缓存中查找数据
	 * 
	 * @param key
	 * @param timeout
	 * @return
	 */
	<T> T get(String key, long timeout);

	boolean delete(String key);

	/**
	 * 按照key清除缓存
	 * 
	 * @param key
	 * @param timeout
	 * @return
	 */
	boolean delete(String key, long timeout);

	/**
	 * 清除一组key
	 * 
	 * @param keys
	 */
	void delete(String... keys);

}