package cn.com.ut.weixin.config;

/**
 * 微信支付的公共信息
 * 
 * @author ouyuexing
 *
 */
public class BaseConfigure {

	public static String HttpsRequestClassName = "cn.com.ut.weixin.common.HttpsRequest";

	/**
	 * 统一下单API
	 */
	public static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 查询订单API
	 */
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	/**
	 * 关闭订单API
	 */
	public static String CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

	/**
	 * 申请退款API
	 */
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * 查询退款API
	 */
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	/**
	 * 下载对账单API
	 */
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	/**
	 * 转换短链接
	 */
	public static String SHORTURL_API = "https://api.mch.weixin.qq.com/payitil/report";

	public static String getHttpsRequestClassName() {

		return HttpsRequestClassName;
	}

	public static void setHttpsRequestClassName(String httpsRequestClassName) {

		HttpsRequestClassName = httpsRequestClassName;
	}

}
