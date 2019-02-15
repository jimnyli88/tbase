package cn.com.ut.weixin.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import cn.com.ut.weixin.config.BaseConfigure;
import cn.com.ut.weixin.protocol.ModuleBase;

/**
 * 服务的基类
 * @author ouyuexing
 * @since 2017年6月28日
 */
public class BaseService {

	// API的地址
	private String apiURL;

	// 发请求的HTTPS请求器
	private IServiceRequest serviceRequest;

	public BaseService(String api, boolean isUseSSL, String certLocalPath, String certPassword)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		apiURL = api;
		Class c = Class.forName(BaseConfigure.HttpsRequestClassName);
		serviceRequest = (IServiceRequest) c.newInstance();
		serviceRequest.setUseSSL(isUseSSL);
		serviceRequest.setCertLocalPath(certLocalPath);
		serviceRequest.setCertPassword(certPassword);
	}

	protected String sendPost(ModuleBase xmlObj) throws UnrecoverableKeyException, IOException,
			NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		return serviceRequest.sendPost(apiURL, xmlObj);
	}

	/**
	 * 供商户想自定义自己的HTTP请求器用
	 * 
	 * @param request
	 *            实现了IserviceRequest接口的HttpsRequest
	 */
	public void setServiceRequest(IServiceRequest request) {

		serviceRequest = request;
	}

	/**
	 * 请求支付服务
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public String request(ModuleBase par, String key) throws Exception {

		// 设置sign
		par.beforeSend(key);
		// --------------------------------------------------------------------
		// 发送HTTPS的Post请求到API地址
		// --------------------------------------------------------------------
		String responseString = sendPost(par);

		return responseString;
	}
}
