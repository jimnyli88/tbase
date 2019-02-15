package cn.com.ut.core.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

	@Autowired
	private Environment env;

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(Integer.parseInt(env.getProperty("executor.corePoolSize", "5")));
		executor.setMaxPoolSize(Integer.parseInt(env.getProperty("executor.maxPoolSize", "10")));
		return executor;
	}
}
