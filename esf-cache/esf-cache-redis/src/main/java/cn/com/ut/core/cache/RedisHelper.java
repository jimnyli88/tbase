package cn.com.ut.core.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis Client
 * 
 * @author wuxiaohua
 * @since 2014-3-19
 */
@Component
public class RedisHelper implements CacheHelper {

	private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);

	@Autowired(required = false)
	private RedisTemplate<String, Serializable> redisTemplate;

	public RedisTemplate<String, Serializable> getRedisTemplate() {

		return redisTemplate;
	}

	@Override
	public boolean set(String key, Object value) {

		if (!(value instanceof Serializable)) {
			logger.error("value不是序列化实例！");
			return false;
		}

		try {
			redisTemplate.opsForValue().set(key, (Serializable) value);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean set(String key, int seconds, Object value) {

		if (!(value instanceof Serializable)) {
			logger.error("value不是序列化实例！");
			return false;
		}
		try {
			redisTemplate.opsForValue().set(key, (Serializable) value, seconds, TimeUnit.SECONDS);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean set(String key, int seconds, Object value, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(String key, Object value) {

		return add(key, -1, value);
	}

	@Override
	public boolean add(String key, int seconds, Object value) {

		if (!(value instanceof Serializable)) {
			logger.error("value不是序列化实例！");
			return false;
		}
		try {
			boolean isSucc = redisTemplate.opsForValue().setIfAbsent(key, (Serializable) value);
			if (seconds == -1)
				redisTemplate.expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
			else
				redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
			return isSucc;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean add(String key, int seconds, Object value, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean replace(String key, Object value) {

		return replace(key, -1, value);
	}

	@Override
	public boolean replace(String key, int seconds, Object value) {

		if (!(value instanceof Serializable)) {
			logger.error("value不是序列化实例！");
			return false;
		}
		try {
			if (!redisTemplate.hasKey(key)) {
				return false;
			}
			if (seconds == -1)
				redisTemplate.opsForValue().set(key, (Serializable) value, DEFAULT_EXPIRE,
						TimeUnit.SECONDS);
			else
				redisTemplate.opsForValue().set(key, (Serializable) value, seconds,
						TimeUnit.SECONDS);
			return true;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean replace(String key, int seconds, Object value, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getAndTouch(String key, int seconds, long timeout) {

		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAndTouch(String key, int seconds) {

		try {
			Serializable result = redisTemplate.opsForValue().get(key);
			if (result == null)
				return null;
			if (seconds == -1)
				redisTemplate.expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
			else
				redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
			return (T) result;
		} catch (RuntimeException e) {
			return null;
		}
	}

	@Override
	public <T> T getAndTouch(String key) {

		return getAndTouch(key, -1);
	}

	@Override
	public boolean touch(String key, int seconds, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean touch(String key, int seconds) {

		try {
			return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {

		try {
			return (T) redisTemplate.opsForValue().get(key);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public <T> T get(String key, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(String key) {

		try {
			redisTemplate.delete(key);
			return true;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean delete(String key, long timeout) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(String... keys) {

		try {
			redisTemplate.delete(Arrays.asList(keys));
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
	}

}
