package cn.com.ut.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.weixin.common.Signature;
import cn.com.ut.weixin.common.Util;
import cn.com.ut.weixin.protocol.downloadbill.DownloadBillReqData;
import cn.com.ut.weixin.protocol.paycloseorder.CloseOrderReqData;
import cn.com.ut.weixin.protocol.paycloseorder.CloseOrderResData;
import cn.com.ut.weixin.protocol.payprotocol.ScanPayReqData;
import cn.com.ut.weixin.protocol.payprotocol.ScanPayResData;
import cn.com.ut.weixin.protocol.payquery.ScanPayQueryReqData;
import cn.com.ut.weixin.protocol.payquery.ScanPayQueryResData;
import cn.com.ut.weixin.protocol.refund.RefundReqData;
import cn.com.ut.weixin.protocol.refund.RefundResData;
import cn.com.ut.weixin.protocol.refundquery.RefundQueryReqData;
import cn.com.ut.weixin.protocol.shorturl.ShortUrlReqData;
import cn.com.ut.weixin.service.CloseOrderService;
import cn.com.ut.weixin.service.DownloadBillService;
import cn.com.ut.weixin.service.RefundQueryService;
import cn.com.ut.weixin.service.RefundService;
import cn.com.ut.weixin.service.ScanPayQueryService;
import cn.com.ut.weixin.service.ScanPayService;
import cn.com.ut.weixin.service.ShortUrlService;

/**
 * 微信扫码支付总入口
 */
public class WXPay {
	private static final Logger logger = LoggerFactory.getLogger(WXPay.class);

	/**
	 * 统一下单接口请求
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static String requestScanPayService(ScanPayReqData scanPayReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		return new ScanPayService(certLocalPath, certPassword).request(scanPayReqData, key);
	}

	/**
	 * 统一下单接口请求
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static ScanPayResData requestScanPayService2(ScanPayReqData scanPayReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		String responseString = new ScanPayService(certLocalPath, certPassword)
				.request(scanPayReqData, key);
		logger.debug(responseString);
		boolean flag = Signature.checkIsSignValidFromResponseString(responseString, key);
		ScanPayResData result = null;
		if (!flag) {
			result = new ScanPayResData();
			result.setErrCode("RETURN SIGN ERROR");
			result.setErrCodeDes("返回来的签名不正确 请注意");
			logger.debug("返回来的签名不正确 请注意");
		} else {
			result = (ScanPayResData) Util.getObjectFromXML(responseString, ScanPayResData.class);
		}
		return result;
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param scanPayQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestScanPayQueryService(ScanPayQueryReqData scanPayQueryReqData,
			String key, String certLocalPath, String certPassword) throws Exception {

		return new ScanPayQueryService(certLocalPath, certPassword).request(scanPayQueryReqData,
				key);
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param scanPayQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static ScanPayQueryResData requestScanPayQueryService2(
			ScanPayQueryReqData scanPayQueryReqData, String key, String certLocalPath,
			String certPassword) throws Exception {

		String responseString = new ScanPayQueryService(certLocalPath, certPassword)
				.request(scanPayQueryReqData, key);
		boolean flag = Signature.checkIsSignValidFromResponseString(responseString, key);
		ScanPayQueryResData result = null;
		if (!flag) {
			result = new ScanPayQueryResData();
			result.setErrCode("RETURN SIGN ERROR");
			result.setErrCodeDes("返回来的签名不正确 请注意");
		} else {
			result = (ScanPayQueryResData) Util.getObjectFromXML(responseString,
					ScanPayQueryResData.class);
		}
		return result;
	}

	/**
	 * 请求退款服务
	 * 
	 * @param refundReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestRefundService(RefundReqData refundReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		return new RefundService(certLocalPath, certPassword).request(refundReqData, key);
	}

	/**
	 * 请求退款服务
	 * 
	 * @param refundReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static RefundResData requestRefundService2(RefundReqData refundReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		String responseString = new RefundService(certLocalPath, certPassword)
				.request(refundReqData, key);
		logger.debug(responseString);
		RefundResData result = null;
		if (CommonUtil.isEmpty(responseString)) {
			return result;
		}
		boolean flag = Signature.checkIsSignValidFromResponseString(responseString, key);
		if (!flag) {
			result = new RefundResData();
			result.setErrCode("RETURN SIGN ERROR");
			result.setErrCodeDes("返回来的签名不正确 请注意");
			logger.debug("退款返回来的签名不正确 请注意");
		} else {
			result = (RefundResData) Util.getObjectFromXML(responseString, RefundResData.class);
		}
		return result;
	}

	/**
	 * 请求退款查询服务
	 * 
	 * @param refundQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestRefundQueryService(RefundQueryReqData refundQueryReqData,
			String key, String certLocalPath, String certPassword) throws Exception {

		return new RefundQueryService(certLocalPath, certPassword).request(refundQueryReqData, key);
	}

	/**
	 * 请求对账单下载服务
	 * 
	 * @param downloadBillReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestDownloadBillService(DownloadBillReqData downloadBillReqData,
			String key, String certLocalPath, String certPassword) throws Exception {

		return new DownloadBillService(certLocalPath, certPassword).request(downloadBillReqData,
				key);
	}

	/**
	 * 关闭订单
	 * 
	 * @param closeOrderReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static String closeOrder(CloseOrderReqData closeOrderReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		return new CloseOrderService(certLocalPath, certPassword).request(closeOrderReqData, key);
	}

	/**
	 * 关闭订单
	 * 
	 * @param closeOrderReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static CloseOrderResData closeOrder2(CloseOrderReqData closeOrderReqData, String key,
			String certLocalPath, String certPassword) throws Exception {

		String responseString = new CloseOrderService(certLocalPath, certPassword)
				.request(closeOrderReqData, key);
		if (CommonUtil.isEmpty(responseString)) {
			logger.debug("关闭订单返回数据空");
			return null;
		}
		logger.debug(responseString);
		boolean flag = Signature.checkIsSignValidFromResponseString(responseString, key);
		CloseOrderResData result = null;
		if (!flag) {
			result = new CloseOrderResData();
			result.setErrCode("RETURN SIGN ERROR");
			result.setErrCodeDes("返回来的签名不正确 请注意");
			logger.debug("关闭订单返回来的签名不正确 请注意");
		} else {
			result = (CloseOrderResData) Util.getObjectFromXML(responseString,
					CloseOrderResData.class);
		}
		return result;
	}

	/**
	 * 获取短码
	 * 
	 * @param shortUrlReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static String shortUrl(ShortUrlReqData shortUrlReqData, String key, String certLocalPath,
			String certPassword) throws Exception {

		return new ShortUrlService(certLocalPath, certPassword).request(shortUrlReqData, key);
	}
}
