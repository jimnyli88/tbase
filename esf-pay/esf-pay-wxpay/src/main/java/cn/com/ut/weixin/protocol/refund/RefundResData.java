package cn.com.ut.weixin.protocol.refund;

import cn.com.ut.weixin.common.WeiXinField;

/**
 * 
 * @author ouyuexing
 * @since 2017年6月28日
 */
public class RefundResData {

	// 协议层
	@WeiXinField("return_code")
	private String returnCode;

	@WeiXinField("return_msg")
	private String returnMsg;

	// 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
	private String appid;

	@WeiXinField("mch_id")
	private String mchId;

	@WeiXinField("sub_mch_id")
	private String subMchId;

	@WeiXinField("device_info")
	private String deviceInfo;

	@WeiXinField("nonce_str")
	private String nonceStr;
	private String sign;

	@WeiXinField("result_code")
	private String resultCode;

	@WeiXinField("err_code")
	private String errCode;

	@WeiXinField("err_code_des")
	private String errCodeDes;

	@WeiXinField("transaction_id")
	private String transactionId;

	@WeiXinField("out_trade_no")
	private String outTradeNo;

	@WeiXinField("out_refund_no")
	private String outRefundNo;

	@WeiXinField("refund_id")
	private String refundId;

	@WeiXinField("refund_fee")
	private String refundFee;

	@WeiXinField("coupon_refund_fee")
	private String couponRefundFee;

	public String getReturnCode() {

		return returnCode;
	}

	public void setReturnCode(String returnCode) {

		this.returnCode = returnCode;
	}

	public String getReturnMsg() {

		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {

		this.returnMsg = returnMsg;
	}

	public String getAppid() {

		return appid;
	}

	public void setAppid(String appid) {

		this.appid = appid;
	}

	public String getMchId() {

		return mchId;
	}

	public void setMchId(String mchId) {

		this.mchId = mchId;
	}

	public String getSubMchId() {

		return subMchId;
	}

	public void setSubMchId(String subMchId) {

		this.subMchId = subMchId;
	}

	public String getDeviceInfo() {

		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {

		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {

		this.nonceStr = nonceStr;
	}

	public String getSign() {

		return sign;
	}

	public void setSign(String sign) {

		this.sign = sign;
	}

	public String getResultCode() {

		return resultCode;
	}

	public void setResultCode(String resultCode) {

		this.resultCode = resultCode;
	}

	public String getErrCode() {

		return errCode;
	}

	public void setErrCode(String errCode) {

		this.errCode = errCode;
	}

	public String getErrCodeDes() {

		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {

		this.errCodeDes = errCodeDes;
	}

	public String getTransactionId() {

		return transactionId;
	}

	public void setTransactionId(String transactionId) {

		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {

		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {

		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {

		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {

		this.outRefundNo = outRefundNo;
	}

	public String getRefundId() {

		return refundId;
	}

	public void setRefundId(String refundId) {

		this.refundId = refundId;
	}

	public String getRefundFee() {

		return refundFee;
	}

	public void setRefundFee(String refundFee) {

		this.refundFee = refundFee;
	}

	public String getCouponRefundFee() {

		return couponRefundFee;
	}

	public void setCouponRefundFee(String couponRefundFee) {

		this.couponRefundFee = couponRefundFee;
	}

}
