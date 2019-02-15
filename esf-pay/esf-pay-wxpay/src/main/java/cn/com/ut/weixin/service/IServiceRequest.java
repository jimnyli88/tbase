package cn.com.ut.weixin.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import cn.com.ut.weixin.protocol.ModuleBase;

/**
 * 
 * @author ouyuexing
 * @since 2017年6月28日
 */
public interface IServiceRequest {

	// Service依赖的底层https请求器必须实现这么一个接口
	public String sendPost(String api_url, ModuleBase xmlObj) throws UnrecoverableKeyException,
			KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;

	/**
	 * 是否使用证书
	 * 
	 * @param isUseSSL
	 */
	void setUseSSL(boolean isUseSSL);

	/**
	 * 设置证书的本地路径
	 * 
	 * @param certLocalPath
	 */
	void setCertLocalPath(String certLocalPath);

	/**
	 * 设置证书的密码
	 * 
	 * @param certPassword
	 */
	void setCertPassword(String certPassword);

}
