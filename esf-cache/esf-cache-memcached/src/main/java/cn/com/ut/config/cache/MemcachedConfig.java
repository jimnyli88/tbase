package cn.com.ut.config.cache;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import cn.com.ut.core.cache.CacheHelper;
import cn.com.ut.core.cache.MemcachedHelper;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * @author: lanbin
 * @since: 2017年2月15日
 */
// @Configuration
public class MemcachedConfig {

	@Autowired
	private Environment env;

	@Bean
	public XMemcachedClientBuilder memcachedClientBuilder() {

		XMemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(env.getProperty("memcached.cluster")), new int[] { 1, 1 });

		builder.setCommandFactory(new BinaryCommandFactory());
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());

		return builder;
	}

	@Bean(destroyMethod = "shutdown")
	public XMemcachedClient memcachedClient() throws IOException {

		return (XMemcachedClient) memcachedClientBuilder().build();
	}

	@Bean
	public CacheHelper cacheHelper() throws IOException {

		MemcachedHelper cacheHelper = new MemcachedHelper();
		cacheHelper.setMemcachedClient(memcachedClient());
		return cacheHelper;
	}
}
