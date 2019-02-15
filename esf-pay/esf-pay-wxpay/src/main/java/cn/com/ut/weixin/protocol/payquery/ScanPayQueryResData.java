package cn.com.ut.weixin.protocol.payquery;

import cn.com.ut.weixin.common.WeiXinField;

/**
 * 
 * @author ouyuexing
 * @since 2017年6月28日
 */
public class ScanPayQueryResData {

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
	private String subMchId;// 新增

	@WeiXinField("nonce_str")
	private String nonceStr;
	private String sign;

	@WeiXinField("result_code")
	private String resultCode;

	@WeiXinField("err_code")
	private String errCode;

	@WeiXinField("err_code_des")
	private String errCodeDes;

	// 以下字段在return_code 和result_code 都为SUCCESS 的时候有返回
	@WeiXinField("trade_state")
	private String tradeState;

	// trade_state的几种可能取值：
	// SUCCESS--支付成功
	// REFUND--转入退款
	// NOTPAY--未支付
	// CLOSED--已关闭
	// REVOKED--已撤销
	// USERPAYING--用户支付中
	// NOPAY--未支付(确认支付超时)
	// PAYERROR--支付失败(其他原因，
	// 如银行返回失败)

	// 以下字段在trade_state 为SUCCESS 或者REFUND 的时候有返回

	@WeiXinField("device_info")
	private String deviceInfo;
	private String openid;

	@WeiXinField("is_subscribe")
	private String isSubscribe;

	@WeiXinField("trade_type")
	private String tradeType;

	@WeiXinField("bank_type")
	private String bankType;

	@WeiXinField("total_fee")
	private String totalFee;

	@WeiXinField("settlement_total_fee")
	private String settlementTotalFee;

	@WeiXinField("cash_fee")
	private String cashFee;

	@WeiXinField("cash_fee_type")
	private String cashFeeType;

	@WeiXinField("coupon_count")
	private String couponCount;

	@WeiXinField("trade_state_desc")
	private String tradeStateDesc;

	@WeiXinField("coupon_fee")
	private String couponFee;

	@WeiXinField("fee_type")
	private String feeType;

	@WeiXinField("transaction_id")
	private String transactionId;

	@WeiXinField("out_trade_no")
	private String outTradeNo;
	private String attach;

	@WeiXinField("time_end")
	private String timeEnd;

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

	public String getTradeState() {

		return tradeState;
	}

	public void setTradeState(String tradeState) {

		this.tradeState = tradeState;
	}

	public String getDeviceInfo() {

		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
	}

	public String getOpenid() {

		return openid;
	}

	public void setOpenid(String openid) {

		this.openid = openid;
	}

	public String getIsSubscribe() {

		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {

		this.isSubscribe = isSubscribe;
	}

	public String getTradeType() {

		return tradeType;
	}

	public void setTradeType(String tradeType) {

		this.tradeType = tradeType;
	}

	public String getBankType() {

		return bankType;
	}

	public void setBankType(String bankType) {

		this.bankType = bankType;
	}

	public String getTotalFee() {

		return totalFee;
	}

	public void setTotalFee(String totalFee) {

		this.totalFee = totalFee;
	}

	public String getSettlementTotalFee() {

		return settlementTotalFee;
	}

	public void setSettlementTotalFee(String settlementTotalFee) {

		this.settlementTotalFee = settlementTotalFee;
	}

	public String getCashFee() {

		return cashFee;
	}

	public void setCashFee(String cashFee) {

		this.cashFee = cashFee;
	}

	public String getCashFeeType() {

		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {

		this.cashFeeType = cashFeeType;
	}

	public String getCouponCount() {

		return couponCount;
	}

	public void setCouponCount(String couponCount) {

		this.couponCount = couponCount;
	}

	public String getTradeStateDesc() {

		return tradeStateDesc;
	}

	public void setTradeStateDesc(String tradeStateDesc) {

		this.tradeStateDesc = tradeStateDesc;
	}

	public String getCouponFee() {

		return couponFee;
	}

	public void setCouponFee(String couponFee) {

		this.couponFee = couponFee;
	}

	public String getFeeType() {

		return feeType;
	}

	public void setFeeType(String feeType) {

		this.feeType = feeType;
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

	public String getAttach() {

		return attach;
	}

	public void setAttach(String attach) {

		this.attach = attach;
	}

	public String getTimeEnd() {

		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {

		this.timeEnd = timeEnd;
	}
}
