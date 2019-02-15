package cn.com.ut.ali.config;

import cn.com.ut.core.common.util.PropertiesUtil;

/**
 * 阿里支付的电脑网站支付配置
 * 
 * @author ouyuexing
 *
 */
public class AlipayPcConfig {

	static {
		String address = PropertiesUtil.getProperty("pay.callback.addr");
		String host = PropertiesUtil.getProperty("pay.callback.host");
		notify_url = address + "/ecommerce/pay/" + host + "/zhifubao/payResult/PC";
	}

	/**
	 * 支付宝分配给开发者的应用ID
	 */
	public static String appid = "2016080100142607";

	/**
	 * 支付的私钥
	 */
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALmXSc/ZdacTEvY/H9iHg59esLDNipyzqpLRvwUDm9JPvrnQ8Cq686CojZOTtZsK80QcGEcOruj2wVZMPf84lasO5wGlLXHDtcqu+CRRxqRyoXwctGNho3rpKGWLYS+U/Oww4geLyu5bszJtYVlbBUtQ/NC30MPPy+0mK2ni41+7AgMBAAECgYEAmbCvb3Cl/bXN/h5IQQf3+AJyT/fm0d33Y55OkdhrMb7YMwjT2o73MhRjKdMokTyAN+cMM8v1DPdLJCv/K1uwlIxnG0yPI2ID5+z9uoL7Vl1Si4JG5iRApt8XQTwhQmQROU38yhR64WU8rDMt1CFz38tT7esvO2KS5gKUtcWmoAECQQD0Q44HxDrMz6Ypr8exwDQy7ew4i2keLH4yqQyQe9NcFj9LpBETCzZCiqFHcR/czhYkYJx/VUSgoDg3/fcJywpNAkEAwoIOin5sKqNg+B+4/8VpyvrqqTSPfnOdKruSjItnqzlA+YPjEcg4P6h8F6B98wIR23Xlbq89SfilvBbX2xMGJwJBAMrhuXyV7kjjSB5j1De9bx3GJGgq0xqu1fr6EmeHkdxw1g3brhrlOyI1xxNx0Icz3YxFencAucm4ijlnheLqdBkCQANpwNORUFiXMVZpSBfE4Qxcxs1dTL9NTA655+Jrd9A7WvaF5/Ah8Uq8rCDGAi/Hc4TEVthQXuTzZgUc6YDAnrUCQCiwonXYlVar/gGWye/ku7Wn1ogbAoN9Hs0mhligELgQd6qYJBeJ4D2eME3Xw0hJRimD/Pk2J4YeIcaOA11yvGQ=";

	/**
	 * 支付的公钥
	 */
	public static String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";

	/**
	 * HTTPS请求地址 正式环境: https://openapi.alipay.com/gateway.do
	 */
	public static String server_url = "https://openapi.alipaydev.com/gateway.do";

	/**
	 * 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public static final String notify_url;

	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public static String return_url = "http://cn.6.ljs.oyx.com/p/system/personal/homepage.html";

	/**
	 * 仅支持JSON
	 */
	public static String format = "JSON";

	/**
	 * 签名方式
	 */
	public static String sign_type = "RSA";

	/**
	 * 请求使用的编码格式，如utf-8,gbk,gb2312等
	 */
	public static String charset = "utf-8";

}
