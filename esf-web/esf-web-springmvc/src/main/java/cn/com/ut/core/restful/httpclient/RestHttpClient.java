package cn.com.ut.core.restful.httpclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.ExceptionUtil;

/**
 * 基于spring RestTemplate的http客户端
 * 
 * @author wuxiaohua
 * @since 2018年2月8日
 */
@Component
public class RestHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(RestHttpClient.class);

	@Autowired
	private Environment env;
	@Resource
	private RestTemplate springRestTemplate;
	private static final List<MediaType> acceptableMediaTypes = Arrays
			.asList(new MediaType[] { MediaType.APPLICATION_JSON_UTF8 });
	private static final MediaType APPLICATION_FORM_URLENCODED_UTF8 = MediaType
			.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8");

	/**
	 * 发送post请求
	 * 
	 * @param requestData
	 * @param appName
	 * @param path
	 * @return
	 */
	public JSONObject exchangePostForEntity(Map<String, ?> requestData, String appName,
			String path) {

		String body = "{}";
		if (!CollectionUtil.isEmptyMap(requestData)) {
			body = JSON.toJSONString(requestData);
		}
		logger.debug("request body===={}", body);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(acceptableMediaTypes);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);

		ResponseEntity<String> responseEntity = getRestTemplate()
				.postForEntity(getReqUrl(appName, path), entity, String.class, new HashMap<>());
		if (responseEntity != null && !CommonUtil.isEmpty(responseEntity.getBody())) {
			logger.debug("response body===={}", responseEntity.getBody());
			return JSON.parseObject(responseEntity.getBody());
		}

		return new JSONObject();

	}

	/**
	 * 发送get请求
	 * 
	 * @param requestData
	 * @param appName
	 * @param path
	 * @return
	 */
	public JSONObject exchangeGetForEntity(Map<String, ?> requestData, String appName,
			String path) {

		ResponseEntity<JSONObject> responseEntity = getRestTemplate()
				.getForEntity(getReqUrl(appName, path), JSONObject.class, requestData);
		JSONObject responseData = null;
		if (responseEntity != null) {
			responseData = responseEntity.getBody();
		}
		return responseData;
	}

	/**
	 * 以表单方式提交
	 * 
	 * @param vo
	 *            参数
	 * @param restCtx
	 *            应用名称
	 * @param restApi
	 *            接口路径
	 * @return
	 */
	public JSONObject formPost(Map<String, String> vo, String restCtx, String restApi) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_FORM_URLENCODED_UTF8);
		headers.setAccept(acceptableMediaTypes);

		MultiValueMap<String, String> formParam = new LinkedMultiValueMap<String, String>();
		Set<Map.Entry<String, String>> set = vo.entrySet();
		Iterator<Map.Entry<String, String>> iter = set.iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			formParam.add(entry.getKey(), (String) entry.getValue());
		}

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(
				formParam, headers);
		ResponseEntity<String> responseEntity = springRestTemplate
				.postForEntity(getPropertiesValue(restCtx) + restApi, entity, String.class);
		if (responseEntity != null && !CommonUtil.isEmpty(responseEntity.getBody())) {
			logger.debug("response body===={}", responseEntity.getBody());
			return JSON.parseObject(responseEntity.getBody());
		}

		return new JSONObject();

	}

	/**
	 * 获取请求的url
	 * 
	 * @param appName
	 * @param path
	 * @return
	 */
	private String getReqUrl(String appName, String path) {

		StringBuilder sb = new StringBuilder();
		String appPath = env.getProperty(appName);
		if (CommonUtil.isEmpty(appPath)) {
			ExceptionUtil.throwServiceException(appName + " is null ");
		}
		sb.append(appPath);
		sb.append(path);
		return sb.toString();
	}

	private RestTemplate getRestTemplate() {

		return springRestTemplate;
	}

	/**
	 * 获取Properties的值
	 * 
	 * @param key
	 * @return
	 */
	public String getPropertiesValue(String key) {

		return env.getProperty(key);
	}
}
