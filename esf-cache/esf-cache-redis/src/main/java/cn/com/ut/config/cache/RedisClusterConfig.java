package cn.com.ut.config.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cn.com.ut.core.cache.RedisClusterCondition;
import cn.com.ut.core.common.util.CommonUtil;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: lanbin
 * @since: 2017年2月15日
 */
@Configuration
@Conditional(RedisClusterCondition.class)
public class RedisClusterConfig {

	@Autowired
	private RedisSettings redisSettings;

	@Bean
	public RedisTemplate<String, Serializable> redisTemplate() {

		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
				redisClusterConfiguration(), jedisPoolConfig());
		if (!CommonUtil.isEmpty(redisSettings.getPassword())) {
			jedisConnectionFactory.setPassword(redisSettings.getPassword());
		}
		return jedisConnectionFactory;
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(redisSettings.getPoolMaxTotal());
		jedisPoolConfig.setMaxIdle(redisSettings.getPoolMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(redisSettings.getPoolMaxWaitMillis());
		return jedisPoolConfig;
	}

	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("spring.redis.cluster.nodes", redisSettings.getClusterNodes());
		source.put("spring.redis.cluster.timeout", redisSettings.getClusterTimeout());
		source.put("spring.redis.cluster.max-redirects", redisSettings.getClusterMaxRedirects());
		return new RedisClusterConfiguration(
				new MapPropertySource("RedisClusterConfiguration", source));
	}

}
