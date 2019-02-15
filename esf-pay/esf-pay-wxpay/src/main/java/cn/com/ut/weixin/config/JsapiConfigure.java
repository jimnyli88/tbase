package cn.com.ut.weixin.config;

import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.PropertiesUtil;

/**
 * 微信公众号支付的信息
 * 
 * @author ouyuexing
 *
 */
public class JsapiConfigure {

	static {
		String address = PropertiesUtil.getProperty("pay.callback.addr");
		String host = PropertiesUtil.getProperty("pay.callback.host");
		PAY_NOTIFE_URL = address + "/ecommerce/pay/" + host + "/weixin/payResult/WeChat";
		key = PropertiesUtil.getProperty("weixin.pay.jsapi.key");
		appID = PropertiesUtil.getProperty("weixin.pay.jsapi.appID");
		mchID = PropertiesUtil.getProperty("weixin.pay.jsapi.mchID");
		sign_type = PropertiesUtil.getProperty("weixin.pay.jsapi.signType");
		String systemName = System.getProperty("os.name");
		if (CommonUtil.isNotEmpty(systemName) && systemName.toLowerCase().startsWith("win")) {
			certLocalPath = PropertiesUtil.getProperty("weixin.pay.jsapi.windows.certLocalPath");
		} else {
			certLocalPath = PropertiesUtil.getProperty("weixin.pay.jsapi.linux.certLocalPath");
		}
		certPassword = PropertiesUtil.getProperty("weixin.pay.jsapi.certPassword");
		secretkey = PropertiesUtil.getProperty("weixin.pay.jsapi.secretkey");
	}

	// 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	private static final String key;

	/**
	 * 公众号的appsecret
	 */
	private static final String secretkey;

	// 微信分配的公众号ID（开通公众号之后可以获取到）
	private static final String appID;

	// 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private static final String mchID;

	// 受理模式下给子商户分配的子商户号
	// private static String subMchID = "";

	private static final String sign_type;

	// HTTPS证书的本地路径
	private static final String certLocalPath;

	// HTTPS证书密码，默认密码等于商户号MCHID
	private static final String certPassword;

	/**
	 * 支付回调地址
	 */
	public static final String PAY_NOTIFE_URL;

	public static String getKey() {

		return key;
	}

	public static String getSecretkey() {

		return secretkey;
	}

	public static String getAppID() {

		return appID;
	}

	public static String getMchID() {

		return mchID;
	}

	public static String getSign_type() {

		return sign_type;
	}

	public static String getCertLocalPath() {

		return certLocalPath;
	}

	public static String getCertPassword() {

		return certPassword;
	}
}
