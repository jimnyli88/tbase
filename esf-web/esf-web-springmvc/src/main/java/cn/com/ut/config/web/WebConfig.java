package cn.com.ut.config.web;

import java.util.List;

import cn.com.ut.core.restful.RestInfoConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.com.ut.core.restful.ResponseWrapConverter;
import cn.com.ut.core.restful.RestTemplateConverter;

//@Configuration
//@EnableWebMvc
// @ServletComponentScan(basePackages = "cn.com.ut")
public class WebConfig implements WebMvcConfigurer {

	// @Bean
	// public ServletRegistrationBean dispatcherServlet(DispatcherServlet
	// dispatcherServlet) {
	//
	// ServletRegistrationBean registration = new
	// ServletRegistrationBean(dispatcherServlet);
	// registration.addUrlMappings("/*");
	// return registration;
	// }

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	//
	// if (!registry.hasMappingForPattern("/webjars/**")) {
	// registry.addResourceHandler("/webjars/**")
	// .addResourceLocations("classpath:/META-INF/resources/webjars/");
	// }
	// if (!registry.hasMappingForPattern("/**")) {
	// registry.addResourceHandler("/**").addResourceLocations(
	// "classpath:/META-INF/resources/", "classpath:/resources/",
	// "classpath:/static/",
	// "classpath:/public/");
	// }
	// }

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

		RestInfoConverter streamDataConverter = new RestInfoConverter();
		ResponseWrapConverter responseWrapConverter = new ResponseWrapConverter();
		RestTemplateConverter restTemplateConverter = new RestTemplateConverter();
		// 首位置优先级别最高
		int index = 0;
		converters.add(index++, streamDataConverter);
		converters.add(index++, responseWrapConverter);
		converters.add(index++, restTemplateConverter);
	}

}
