package cn.com.ut.core.restful;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import cn.com.ut.core.common.util.CollectionUtil;

public class RestTemplateHelper {

	private RestTemplate restTemplate;
	private RestTemplate springRestTemplate;

	public RestTemplateHelper() {

	}

	public RestTemplate getSpringRestTemplate() {

		return springRestTemplate;
	}

	public void setSpringRestTemplate(RestTemplate springRestTemplate) {

		this.springRestTemplate = springRestTemplate;
	}

	public RestTemplate getRestTemplate() {

		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {

		this.restTemplate = restTemplate;
	}

	public RestTemplateHelper(RestTemplate restTemplate, RestTemplate springRestTemplate) {
		this.restTemplate = restTemplate;
		this.springRestTemplate = springRestTemplate;
	}

	public <T> T postForResponseData(Map<String, ?> requestData, String appName, String path,
			Object body, boolean loadBalance) {

		ResponseData responseData = postForEntity(requestData, appName, path, ResponseData.class,
				body, loadBalance);
		return responseData == null ? null : (T) responseData.getEntity();
	}

	public <T> T postForEntity(Map<String, ?> requestData, String appName, String path,
			Class<T> responseType, Object body, boolean loadBalance) {

		HttpEntity<Object> httpEntity = body == null ? null : new HttpEntity<Object>(body);
		T responseData = exchangeForEntity(requestData, appName, path, responseType,
				HttpMethod.POST, httpEntity, loadBalance);
		return responseData;
	}

	public <T> T getForResponseData(Map<String, String> requestData, String appName, String path,
			boolean loadBalance) {

		ResponseData responseData = getForForEntity(requestData, appName, path, ResponseData.class,
				loadBalance);
		return responseData == null ? null : (T) responseData.getEntity();
	}

	public <T> T getForForEntity(Map<String, String> requestData, String appName, String url,
			Class<T> responseType, boolean loadBalance) {

		T responseData = exchangeForEntity(requestData, appName, url, responseType, HttpMethod.GET,
				null, loadBalance);
		return responseData;
	}

	public <T> T exchangeForEntity(Map<String, ?> requestData, String appName, String path,
			Class<T> responseType, HttpMethod method, HttpEntity<Object> httpEntity,
			boolean loadBalance) {

		StringBuilder sb = new StringBuilder();
		if (appName.startsWith("http"))
			sb.append(appName + path);
		else
			sb.append("http://" + appName + path);
		if (!CollectionUtil.isEmptyMap(requestData)) {
			Set<String> params = requestData.keySet();
			int paramIndex = 0;
			for (String param : params) {
				if (paramIndex == 0)
					sb.append("?" + param + "={" + param + "}");
				else
					sb.append("&" + param + "={" + param + "}");
			}
		}

		ResponseEntity<T> responseEntity = null;
		if (requestData == null)
			requestData = Collections.emptyMap();
		if (HttpMethod.GET.equals(method))
			responseEntity = getRestTemplate(loadBalance).getForEntity(sb.toString(), responseType,
					requestData);
		else
			responseEntity = getRestTemplate(loadBalance).postForEntity(sb.toString(), httpEntity,
					responseType, requestData);
		if (responseEntity == null)
			return null;
		else {
			T responseData = responseEntity.getBody();
			return responseData;
		}
	}

	private RestTemplate getRestTemplate(boolean loadBalance) {

		return loadBalance ? getRestTemplate() : getSpringRestTemplate();
	}
}
