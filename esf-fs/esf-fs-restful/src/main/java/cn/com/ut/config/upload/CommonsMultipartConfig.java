package cn.com.ut.config.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class CommonsMultipartConfig {

	@Autowired
	private Environment env;

	@Bean
	public CommonsMultipartResolver commonsMultipartResolver() {

		CommonsMultipartResolver tem = new CommonsMultipartResolver();
		tem.setMaxUploadSize(env.getProperty("fs.uploadMaxSize", Long.class, 1024 * 1024 * 5l));
		tem.setMaxInMemorySize(env.getProperty("fs.maxInMemorySize", Integer.class, 4096));
		tem.setDefaultEncoding("UTF-8");
		return tem;
	}
}
