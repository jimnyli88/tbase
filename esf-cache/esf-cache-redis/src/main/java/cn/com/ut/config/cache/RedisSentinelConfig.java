package cn.com.ut.config.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cn.com.ut.core.cache.RedisSentinelCondition;
import cn.com.ut.core.common.util.CommonUtil;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Conditional(RedisSentinelCondition.class)
public class RedisSentinelConfig {

	@Autowired
	private RedisSettings redisSettings;

	/**
	 * 获取连接池
	 * 
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 最大连接数
		jedisPoolConfig.setMaxTotal(redisSettings.getPoolMaxTotal());
		jedisPoolConfig.setMaxIdle(redisSettings.getPoolMaxIdle());
		jedisPoolConfig.setMinIdle(redisSettings.getPoolMinIdle());
		jedisPoolConfig.setMaxWaitMillis(redisSettings.getPoolMaxWaitMillis());
		return jedisPoolConfig;
	}

	@Bean
	public RedisSentinelConfiguration redisSentinelConfiguration() {

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("spring.redis.sentinel.nodes", redisSettings.getSentinelNodes());
		source.put("spring.redis.sentinel.master", redisSettings.getSentinelMaster());
		PropertySource<Map<String, Object>> propertySource = new MapPropertySource(
				redisSettings.getSentinelMaster(), source);
		return new RedisSentinelConfiguration(propertySource);

	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory fac = new JedisConnectionFactory(redisSentinelConfiguration(),
				jedisPoolConfig());
		if (!CommonUtil.isEmpty(redisSettings.getPassword())) {
			fac.setPassword(redisSettings.getPassword());
		}
		return fac;
	}

	@Bean
	public RedisTemplate<String, Serializable> redisTemplate() {

		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		return redisTemplate;
	}

	// @Bean
	public RedisCacheManager cacheManager(RedisTemplate<String, Serializable> redisTemplate) {

		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		return redisCacheManager;
	}

	@Bean
	@Primary
	public CacheManager cacheManager() {

		CompositeCacheManager cacheManager = new CompositeCacheManager();
		ArrayList<CacheManager> cacheManagers = new ArrayList<>();
		cacheManagers.add(new RedisCacheManager(redisTemplate()));
		cacheManager.setCacheManagers(cacheManagers);
		return cacheManager;
	}

}
