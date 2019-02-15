package cn.com.ut.config.web;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class WebInterceptorPathPattern {

	private HandlerInterceptorAdapter webInterceptor;
	private String[] pathPatterns;
	private String[] excludePathPatterns;

	public String[] getExcludePathPatterns() {

		return excludePathPatterns;
	}

	public void setExcludePathPatterns(String[] excludePathPatterns) {

		this.excludePathPatterns = excludePathPatterns;
	}

	public HandlerInterceptorAdapter getWebInterceptor() {

		return webInterceptor;
	}

	public void setWebInterceptor(HandlerInterceptorAdapter webInterceptor) {

		this.webInterceptor = webInterceptor;
	}

	public String[] getPathPatterns() {

		return pathPatterns;
	}

	public void setPathPatterns(String[] pathPatterns) {

		this.pathPatterns = pathPatterns;
	}

	public WebInterceptorPathPattern(HandlerInterceptorAdapter webInterceptor,
			String[] pathPatterns, String[] excludePathPatterns) {
		super();
		this.webInterceptor = webInterceptor;
		this.pathPatterns = pathPatterns;
		this.excludePathPatterns = excludePathPatterns;
	}

}
