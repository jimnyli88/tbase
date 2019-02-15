package cn.com.ut.redisson.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxiaohua
 * @since 2018/10/30
 */
@Configuration
@ConfigurationProperties(prefix = "redisson")
@Getter
@Setter
public class RedissonProperties {

	private long ttl = 30 * 60 * 1000;
	private long maxIdleTime = 3 * 60 * 60 * 1000;
	private List<String> cacheNames;
	private String singleServer;
}
