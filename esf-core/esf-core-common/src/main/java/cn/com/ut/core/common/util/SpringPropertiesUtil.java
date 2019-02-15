package cn.com.ut.core.common.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring Properties Util
 * 
 * @author wuxiaohua
 * @since 2018年1月25日
 */
@Component
public class SpringPropertiesUtil implements InitializingBean {

	private static Environment environment;

	@Autowired
	public void setEnvironment(Environment environment) {

		SpringPropertiesUtil.environment = environment;
	}

	public static Environment getEnvironment() {

		return environment;
	}

	public static String getPropertyValue(String property, String defaultValue) {

		return environment.getProperty(property, defaultValue);
	}

	public static String getPropertyValue(String property) {

		return getPropertyValue(property, null);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		// TODO Auto-generated method stub

	}

}
