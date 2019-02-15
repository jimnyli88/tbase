package cn.com.ut.config.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.jsms.api.common.SMSClient;
import lombok.Data;

/**
 * @author: lanbin
 * @since: 2017年3月8日
 */
@Configuration
@ConfigurationProperties(prefix = "jsms")
@Data
public class JSMSConfig {

	private String masterSecret;
	private String appkey;
	private String open = ConstantUtil.FLAG_NO;

	@Bean
	public SMSClient smsClient() {

		SMSClient client = new SMSClient(masterSecret, appkey);
		return client;
	}
}
