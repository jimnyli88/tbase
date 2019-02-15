package cn.com.ut.core.restful.rpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cn.com.ut.core.restful.RestTemplateConverter;
import cn.com.ut.core.restful.RestTemplateHelper;

@Configuration
@ConfigurationProperties(prefix = "rest")
public class RestTemplateConfig {

	private int connectTimeout = 20000;
	private int readTimeout = 20000;
	private int connectionRequestTimeout = 20000;

	public int getConnectionRequestTimeout() {

		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {

		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getConnectTimeout() {

		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {

		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {

		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {

		this.readTimeout = readTimeout;
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new RestTemplateConverter());
		return restTemplate;
	}

	@Bean
	public RestTemplate springRestTemplate() {

		SimpleClientHttpRequestFactory fac = new SimpleClientHttpRequestFactory();
		fac.setConnectTimeout(getConnectTimeout());
		fac.setReadTimeout(getReadTimeout());

		RestTemplate restTemplate = new RestTemplate(fac);

		restTemplate.getMessageConverters().add(0, new RestTemplateConverter());
		for (HttpMessageConverter<?> httpMessageConverter : restTemplate.getMessageConverters()) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter).setWriteAcceptCharset(false);
			}
		}
		return restTemplate;
	}

	@Bean
	public RestTemplateHelper restTemplateHelper() {

		RestTemplateHelper restTemplateHelper = new RestTemplateHelper(restTemplate(),
				springRestTemplate());
		return restTemplateHelper;
	}
}
