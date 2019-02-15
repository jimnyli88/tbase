package cn.com.ut.ali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import cn.com.ut.ali.config.AlipayAppConfig;
import cn.com.ut.ali.config.AlipayH5Config;
import cn.com.ut.ali.config.AlipayPcConfig;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.util.CommonUtil;

/**
 * 支付宝即时到帐入口
 */
public class ALiPay {
	private final static Logger logger = LoggerFactory.getLogger(ALiPay.class);

	/**
	 * 获取阿里h5支付的链接
	 * 
	 * @return
	 */
	public static AlipayClient getAlipayH5Client() {

		AlipayClient alipayClient = new DefaultAlipayClient(AlipayH5Config.server_url,
				AlipayH5Config.appid, AlipayH5Config.private_key, AlipayH5Config.format,
				AlipayH5Config.charset, AlipayH5Config.public_key, AlipayH5Config.sign_type);
		return alipayClient;
	}

	/**
	 * 获取阿里支付的PC链接
	 * 
	 * @return
	 */
	public static AlipayClient getAliPcpayClient() {

		AlipayClient alipayClient = new DefaultAlipayClient(AlipayPcConfig.server_url,
				AlipayPcConfig.appid, AlipayPcConfig.private_key, AlipayPcConfig.format,
				AlipayPcConfig.charset, AlipayPcConfig.public_key, AlipayPcConfig.sign_type);
		return alipayClient;
	}

	/**
	 * 获取阿里支付的APP链接
	 * 
	 * @return
	 */
	public static AlipayClient getAliApppayClient() {

		AlipayClient alipayClient = new DefaultAlipayClient(AlipayAppConfig.server_url,
				AlipayAppConfig.appid, AlipayAppConfig.private_key, AlipayAppConfig.format,
				AlipayAppConfig.charset, AlipayAppConfig.public_key, AlipayAppConfig.sign_type);
		return alipayClient;
	}

	/**
	 * 获取支付页面2
	 * 
	 * @return
	 */
	public static String getPcPayHtml(AlipayTradePagePayModel module) {

		AlipayClient alipayClient = getAliPcpayClient();
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setBizModel(module);
		alipayRequest.setNotifyUrl(AlipayPcConfig.notify_url);
		alipayRequest.setReturnUrl(AlipayPcConfig.return_url);
		String result = null;
		try {
			result = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			logger.error("调用getPcPayHtml发生异常  参数：{}  信息：{}", JSON.toJSONString(module),
					e.getMessage());
		}
		return result;
	}

	/**
	 * 获取手机网站支付页面
	 * 
	 * @param module
	 *            请求参数（非公共请求参数详情请参考支付宝文档）
	 *            https://doc.open.alipay.com/doc2/detail.htm?treeId=203&
	 *            articleId=105463&docType=1
	 * @return
	 */
	public static String getPhonePayHtml(AlipayTradeWapPayModel module) {

		AlipayClient alipayClient = getAlipayH5Client();
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		// 在公共参数中设置回跳和通知地址
		// alipayRequest.setNotifyUrl(AlipayConfig.phone_notify_url);
		alipayRequest.setReturnUrl(AlipayH5Config.return_url);
		alipayRequest.setNotifyUrl(AlipayH5Config.notify_url);
		module.setProductCode("QUICK_WAP_PAY");
		alipayRequest.setBizModel(module);
		String form = null;
		try {
			// 获取url
			// form = alipayClient.pageExecute(alipayRequest, "GET").getBody();
			// 获取页面
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			logger.error("调用getPhonePayHtml发生异常  参数：{}  信息：{}", JSON.toJSONString(module),
					e.getMessage());
		} // 调用SDK生成表单
		return form;
	}

	/**
	 * 获取阿里APP支付信息
	 * 
	 * @param module
	 * @return
	 */
	public static String getAPPPayMsg(AlipayTradeAppPayModel module) {

		AlipayClient alipayClient = getAliApppayClient();
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// request.setReturnUrl(AlipayConfig.phone_return_url);
		request.setNotifyUrl(AlipayAppConfig.notify_url);
		request.setBizModel(module);
		try {
			AlipayTradeAppPayResponse resp = alipayClient.pageExecute(request, "GET");
			if (resp != null) {
				String result = resp.getBody();
				if (CommonUtil.isNotEmpty(result)) {
					result = result.replace(AlipayAppConfig.server_url + "?", "");
				}
				return result;
			}
		} catch (AlipayApiException e) {
			logger.error("调用getAPPPayMsg发生异常  参数：{}  信息：{}", JSON.toJSONString(module),
					e.getMessage());
		}
		return null;

	}

	/**
	 * 查询订单信息
	 * 
	 * @param model
	 *            请求参数（非公共请求参数详情请参考支付宝文档）https://doc.open.alipay.com/doc2/
	 *            apiDetail.htm?apiId=757&docType=4
	 * @param type
	 *            类型
	 * @return
	 */
	public static AlipayTradeQueryResponse queryOrder(AlipayTradeQueryModel model, String type) {

		if (CommonUtil.isEmpty(type)) {
			type = ConstantUtil.ALI_PAY_TYPE.PC;
		}
		AlipayClient alipayClient = null;
		switch (type) {
		case ConstantUtil.ALI_PAY_TYPE.PC:
			alipayClient = getAliPcpayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIAPP:
			alipayClient = getAliApppayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIH5:
			alipayClient = getAlipayH5Client();
			break;

		default:
			return null;
		}
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		try {
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			return response;
		} catch (Exception e) {
			logger.error("调用AlipayTradeQueryResponse发生异常  参数：{}  信息：{}", JSON.toJSONString(model),
					e.getMessage());
			return null;
		}
	}

	/**
	 * 关闭订单 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭
	 * 
	 * @param model
	 *            请求参数（非公共请求参数详情请参考支付宝文档）https://doc.open.alipay.com/doc2/
	 *            apiDetail.htm?apiId=1058&docType=4
	 * @return
	 */
	public static AlipayTradeCloseResponse closeOrder(AlipayTradeCloseModel model, String type) {

		if (CommonUtil.isEmpty(type)) {
			type = ConstantUtil.ALI_PAY_TYPE.PC;
		}
		AlipayClient alipayClient = null;
		switch (type) {
		case ConstantUtil.ALI_PAY_TYPE.PC:
			alipayClient = getAliPcpayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIAPP:
			alipayClient = getAliApppayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIH5:
			alipayClient = getAlipayH5Client();
			break;

		default:
			return null;
		}
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		try {
			AlipayTradeCloseResponse response = alipayClient.execute(request);
			return response;
		} catch (AlipayApiException e) {
			logger.error("调用AlipayTradeCloseResponse发生异常  参数：{}  信息：{}", JSON.toJSONString(model),
					e.getMessage());
		}

		return null;
	}

	/**
	 * 交易退款接口
	 * 
	 * @param model
	 *            请求参数（非公共请求参数详情请参考支付宝文档）https://doc.open.alipay.com/doc2/
	 *            apiDetail.htm?apiId=759&docType=4
	 * @return
	 */
	public static AlipayTradeRefundResponse refund(AlipayTradeRefundModel model, String type) {

		if (CommonUtil.isEmpty(type)) {
			type = ConstantUtil.ALI_PAY_TYPE.PC;
		}
		AlipayClient alipayClient = null;
		switch (type) {
		case ConstantUtil.ALI_PAY_TYPE.PC:
			alipayClient = getAliPcpayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIAPP:
			alipayClient = getAliApppayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIH5:
			alipayClient = getAlipayH5Client();
			break;

		default:
			return null;
		}
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		try {
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			return response;
		} catch (AlipayApiException e) {
			logger.error("调用AlipayTradeRefundResponse发生异常  参数：{}  信息：{}", JSON.toJSONString(model),
					e.getMessage());
		}
		return null;
	}

	/**
	 * 查看退款详情
	 * 
	 * @param model
	 *            请求参数（非公共请求参数详情请参考支付宝文档）
	 * @return
	 */
	public static AlipayTradeFastpayRefundQueryResponse refundRuery(
			AlipayTradeFastpayRefundQueryModel model, String type) {

		if (CommonUtil.isEmpty(type)) {
			type = ConstantUtil.ALI_PAY_TYPE.PC;
		}
		AlipayClient alipayClient = null;
		switch (type) {
		case ConstantUtil.ALI_PAY_TYPE.PC:
			alipayClient = getAliPcpayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIAPP:
			alipayClient = getAliApppayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIH5:
			alipayClient = getAlipayH5Client();
			break;

		default:
			return null;
		}
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		try {
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
			return response;
		} catch (AlipayApiException e) {
			logger.error("调用AlipayTradeFastpayRefundQueryResponse发生异常  参数：{}  信息：{}",
					JSON.toJSONString(model), e.getMessage());
		}
		return null;
	}

	/**
	 * 查询对账单下载地址
	 * 
	 * @param model
	 * @return
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse getBill(
			AlipayDataDataserviceBillDownloadurlQueryModel model, String type) {

		if (CommonUtil.isEmpty(type)) {
			type = ConstantUtil.ALI_PAY_TYPE.PC;
		}
		AlipayClient alipayClient = null;
		switch (type) {
		case ConstantUtil.ALI_PAY_TYPE.PC:
			alipayClient = getAliPcpayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIAPP:
			alipayClient = getAliApppayClient();
			break;
		case ConstantUtil.ALI_PAY_TYPE.ALIH5:
			alipayClient = getAlipayH5Client();
			break;

		default:
			return null;
		}
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		try {
			AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient
					.execute(request);
			return response;
		} catch (AlipayApiException e) {
			logger.error("调用AlipayDataDataserviceBillDownloadurlQueryResponse发生异常  参数：{}  信息：{}",
					JSON.toJSONString(model), e.getMessage());
		}

		return null;
	}

}
