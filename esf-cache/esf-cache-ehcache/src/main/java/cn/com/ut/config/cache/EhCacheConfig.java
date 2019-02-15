package cn.com.ut.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.CacheManager;

@Configuration
public class EhCacheConfig {

	@Autowired
	private Environment env;

//	@Bean
	public EhCacheCacheManager ehCacheCacheManager(CacheManager cm) {

		EhCacheCacheManager cacheManager = new EhCacheCacheManager(cm);
		return cacheManager;
	}

	@Bean
	public EhCacheManagerFactoryBean ehcache() {

		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		factoryBean.setConfigLocation(
				new ClassPathResource(env.getProperty("ehcache.config.classpath")));
		return factoryBean;
	}
}
