package cn.com.ut.config.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.xxl.job.core.executor.XxlJobExecutor;

@PropertySource({ "classpath:/cn/com/ut/config/schedule/xxl/xxl-job.properties" })
@Configuration
public class XxlJobConfig {

	@Autowired
	private Environment env;

	@Bean(initMethod = "start", destroyMethod = "destroy", name = { "xxlJobExecutor" })
	public XxlJobExecutor xxlJobExecutor() {

		XxlJobExecutor res = new XxlJobExecutor();
		res.setIp(env.getProperty("xxl.job.executor.ip"));
		res.setPort(env.getRequiredProperty("xxl.job.executor.port", Integer.class));
		res.setAppName(env.getProperty("xxl.job.executor.appname"));
		res.setAdminAddresses(env.getProperty("xxl.job.admin.addresses"));
		res.setLogPath(env.getProperty("xxl.job.executor.logpath"));
		return res;
	}
}
