package cn.com.ut.ali.config;

import cn.com.ut.core.common.util.PropertiesUtil;

/**
 * 阿里APP支付的配置
 * 
 * @author ouyuexing
 *
 */
public class AlipayAppConfig {

	static {
		String address = PropertiesUtil.getProperty("pay.callback.addr");
		String host = PropertiesUtil.getProperty("pay.callback.host");
		notify_url = address + "/ecommerce/pay/" + host + "/zhifubao/payResult/APP";
	}

	/**
	 * 支付宝分配给开发者的应用ID
	 */
	public static String appid = AlipayPcConfig.appid;

	/**
	 * 支付的私钥
	 */
	public static String private_key = AlipayPcConfig.private_key;

	/**
	 * app或h5支付的公钥
	 */
	public static String public_key = AlipayPcConfig.public_key;

	/**
	 * HTTPS请求地址 正式环境: https://openapi.alipay.com/gateway.do
	 */
	public static String server_url = AlipayPcConfig.server_url;

	/**
	 * 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public static final String notify_url;

	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public static String return_url = AlipayPcConfig.return_url;

	/**
	 * 仅支持JSON
	 */
	public static String format = AlipayPcConfig.format;

	/**
	 * 签名方式
	 */
	public static String sign_type = AlipayPcConfig.sign_type;

	/**
	 * 请求使用的编码格式，如utf-8,gbk,gb2312等
	 */
	public static String charset = AlipayPcConfig.charset;

}
