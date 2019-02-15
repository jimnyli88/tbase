package cn.com.ut.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisSettings {

	private String password;

	private String mode;

	private String clusterNodes;

	private int clusterTimeout;

	private int clusterMaxRedirects;

	private int poolMaxTotal;

	private int poolMaxIdle;

	private int poolMinIdle;

	private int poolMaxWaitMillis;

	private String sentinelNodes;

	private String sentinelMaster;

	private String singleHost;

	private int singlePort;

}
