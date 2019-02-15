package cn.com.ut.core.cache;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * Memcached Client
 * 
 * @author wuxiaohua
 * @since 2014-3-19
 */
public class MemcachedHelper implements CacheHelper {

	private static final Logger logger = LoggerFactory.getLogger(MemcachedHelper.class);

	/**
	 * 缓存服务器连接
	 */
	private XMemcachedClient memcachedClient;

	public void setMemcachedClient(XMemcachedClient memcachedClient) {

		this.memcachedClient = memcachedClient;
	}

	/**
	 * 设置一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param value
	 *            缓存value
	 * @return 操作是否成功
	 */
	public boolean set(String key, Object value) {

		return set(key, DEFAULT_EXPIRE, value);
	}

	/**
	 * 设置一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param value
	 *            缓存value
	 * @return 操作是否成功
	 */
	public boolean set(String key, int exp, Object value) {

		return set(key, exp, value, memcachedClient.getOpTimeout());
	}

	/**
	 * 设置一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param value
	 *            缓存value
	 * @param timeout
	 *            连接超时
	 * @return 操作是否成功
	 */
	public boolean set(String key, int exp, Object value, long timeout) {

		boolean result = false;
		try {
			result = memcachedClient.set(key, exp, value, timeout);
			logger.debug("set data to cache by key = {}", key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @return 缓存数据实例
	 */
	public <T> T get(String key) {

		return get(key, memcachedClient.getOpTimeout());
	}

	/**
	 * 获取一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param timeout
	 *            连接超时
	 * @return 缓存数据实例
	 */
	public <T> T get(String key, long timeout) {

		T t = null;
		try {
			t = memcachedClient.get(key, timeout);
			logger.debug("get data {} from cache by key = {}", t == null ? "miss" : "hit", key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return t;
	}

	/**
	 * 删除一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @return 操作结果
	 */
	public boolean delete(String key) {

		return delete(key, memcachedClient.getOpTimeout());
	}

	/**
	 * 删除一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param timeout
	 *            连接超时
	 * @return 操作结果
	 */
	public boolean delete(String key, long timeout) {

		boolean result = false;
		try {
			result = memcachedClient.delete(key, timeout);
			logger.debug("delete data from remote cache by key = {}", key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 清除一组缓存
	 * 
	 * @param keys
	 *            一组缓存key
	 */
	public void delete(String... keys) {

		if (keys != null && keys.length > 0) {
			for (String key : keys)
				delete(key);
		}
	}

	/**
	 * 设置一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param value
	 *            缓存value
	 * @param timeout
	 *            连接超时
	 * @return 操作是否成功
	 */
	public boolean add(String key, int exp, Object value, long timeout) {

		boolean result = false;
		try {
			result = memcachedClient.add(key, exp, value, timeout);
			logger.debug("add data to cache by key = {}", key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * @see MemcachedHelper#add(String, int, Object, long)
	 */
	public boolean add(String key, int exp, Object value) {

		return add(key, exp, value, memcachedClient.getOpTimeout());
	}

	/**
	 * @see MemcachedHelper#add(String, int, Object)
	 */
	public boolean add(String key, Object value) {

		return add(key, DEFAULT_EXPIRE, value);
	}

	/**
	 * 替换一条缓存
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param value
	 *            缓存value
	 * @param timeout
	 *            连接超时
	 * @return 操作是否成功
	 */
	public boolean replace(String key, int exp, Object value, long timeout) {

		boolean result = false;
		try {
			result = memcachedClient.replace(key, exp, value, timeout);
			logger.debug("replace data result {} to remote cache by key = {}", key, result);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * @see MemcachedHelper#replace(String, int, Object, long)
	 */
	public boolean replace(String key, int exp, Object value) {

		return replace(key, exp, value, memcachedClient.getOpTimeout());
	}

	/**
	 * @see MemcachedHelper#replace(String, int, Object)
	 */
	public boolean replace(String key, Object value) {

		return replace(key, DEFAULT_EXPIRE, value);
	}

	/**
	 * 获取一条缓存，并设置新的缓存时长
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param timeout
	 *            连接超时
	 * @return 缓存数据
	 */
	public <T> T getAndTouch(String key, int exp, long timeout) {

		T t = null;
		try {
			t = memcachedClient.getAndTouch(key, exp, timeout);
			logger.debug("getAndTouch data {} from cache by key = {}", t == null ? "miss" : "hit",
					key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return t;
	}

	/**
	 * 设置新的缓存时长
	 * 
	 * @param key
	 *            缓存key
	 * @param exp
	 *            缓存时长
	 * @param timeout
	 *            连接超时
	 * @return 操作结果
	 */
	public boolean touch(String key, int exp, long timeout) {

		boolean b = false;
		try {
			b = memcachedClient.touch(key, exp, timeout);
			logger.debug("touch data from cache by key = {}", key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error(e.getMessage());
		}
		return b;
	}

	/**
	 * @see MemcachedHelper#touch(String, int, long)
	 */
	public boolean touch(String key, int exp) {

		return touch(key, exp, memcachedClient.getOpTimeout());
	}

	/**
	 * @see MemcachedHelper#getAndTouch(String, int, long)
	 */
	public <T> T getAndTouch(String key, int exp) {

		return getAndTouch(key, exp, memcachedClient.getOpTimeout());
	}

	/**
	 * @see MemcachedHelper#getAndTouch(String, int)
	 */
	public <T> T getAndTouch(String key) {

		return getAndTouch(key, DEFAULT_EXPIRE);
	}

}
