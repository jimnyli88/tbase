package cn.com.ut.redisson.config;

import java.util.HashMap;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuxiaohua
 * @since 2018/9/21
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(RedissonProperties.class)
public class RedissonConfig {

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnProperty(value = "redisson.singleServer", matchIfMissing = true)
	public RedissonClient redisson(RedissonProperties redissonProperties) {

		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redissonProperties.getSingleServer());
		return Redisson.create(config);
	}

	@Bean
	public CacheManager cacheManager(RedissonClient redissonClient,
			RedissonProperties redissonProperties) {

		final CacheConfig cacheConfig = new CacheConfig(redissonProperties.getTtl(),
				redissonProperties.getMaxIdleTime());
		final Map<String, CacheConfig> cacheConfigMap = new HashMap<>();
		if (redissonProperties.getCacheNames() == null
				|| redissonProperties.getCacheNames().isEmpty()) {
			cacheConfigMap.put("cache", cacheConfig);
		} else {
			redissonProperties.getCacheNames().forEach(e -> cacheConfigMap.put(e, cacheConfig));
		}

		return new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
	}

}
