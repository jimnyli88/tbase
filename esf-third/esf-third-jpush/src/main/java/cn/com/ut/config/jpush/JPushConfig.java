package cn.com.ut.config.jpush;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;

/**
 * @author: lanbin
 * @since: 2017年3月8日
 */
@Configuration
@PropertySource({ "classpath:/cn/com/ut/config/jpush/jpush-config.properties" })
public class JPushConfig {

	@Value("jpush.maxRetryTimes")
	private int maxRetryTimes;
	@Value("jpush.connectionTimeout")
	private int connectionTimeout;
	@Value("jpush.master_secret")
	private String mastersecret;
	@Value("jpush.jpush.app_key")
	private String appkey;

	@Bean
	public JPushClient jpushClient() {

		ClientConfig config = ClientConfig.getInstance();
		config.setMaxRetryTimes(maxRetryTimes); // 最大重连次数，默认是3
		config.setConnectionTimeout(connectionTimeout); // 连接超时时间，默认是5秒

		JPushClient jpushClient = new JPushClient(mastersecret, appkey, null, config);
		return jpushClient;
	}
}
