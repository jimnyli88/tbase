package cn.com.ut.weixin.protocol.notify;

import cn.com.ut.weixin.common.WeiXinField;

/**
 * 支付回调的数据实体 详情可参考官网https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 * 
 * @author ouyuexing
 *
 */
public class PayNotifyData {

	/**
	 * SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	@WeiXinField("return_code")
	private String returnCode;

	/**
	 * 返回信息，如非空，为错误原因
	 */
	@WeiXinField("return_msg")
	private String returnMsg;

	/**
	 * 微信分配的公众账号ID
	 */
	private String appid;

	/**
	 * 微信支付分配的商户号
	 */
	@WeiXinField("mch_id")
	private String mchId;

	/**
	 * 微信支付分配的终端设备号
	 */
	@WeiXinField("device_info")
	private String deviceInfo;

	/**
	 * 随机字符串，不长于32位
	 */
	@WeiXinField("nonce_str")
	private String nonceStr;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
	 */
	@WeiXinField("sign_type")
	private String signType;

	/**
	 * SUCCESS/FAIL
	 */
	@WeiXinField("result_code")
	private String resultCode;

	/**
	 * 错误返回的信息描述
	 */
	@WeiXinField("err_code")
	private String errCode;

	/**
	 * 错误返回的信息描述
	 */
	@WeiXinField("err_code_des")
	private String errCodeDes;

	/**
	 * 用户在商户appid下的唯一标识
	 */
	@WeiXinField("openid")
	private String openid;

	/**
	 * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	 */
	@WeiXinField("is_subscribe")
	private String isSubscribe;

	/**
	 * JSAPI、NATIVE、APP
	 */
	@WeiXinField("trade_type")
	private String tradeType;

	/**
	 * 银行类型，采用字符串类型的银行标识，银行类型见银行列表
	 */
	@WeiXinField("bank_type")
	private String bankType;

	/**
	 * 订单总金额，单位为分
	 */
	@WeiXinField("total_fee")
	private String totalFee;

	/**
	 * 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
	 */
	@WeiXinField("settlement_total_fee")
	private String settlementTotalFee;

	/**
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
	 */
	@WeiXinField("fee_type")
	private String feeType;

	/**
	 * 现金支付金额订单现金支付金额，详见支付金额
	 */
	@WeiXinField("cash_fee")
	private String cashFee;

	/**
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
	 */
	@WeiXinField("cash_fee_type")
	private String cashFeeType;

	/**
	 * 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
	 */
	@WeiXinField("coupon_fee")
	private String couponFee;

	/**
	 * 代金券使用数量
	 */
	@WeiXinField("coupon_count")
	private String couponCount;

	/**
	 * 微信支付订单号
	 */
	@WeiXinField("transaction_id")
	private String transactionId;

	/**
	 * 商户系统的订单号，与请求一致
	 */
	@WeiXinField("out_trade_no")
	private String outTradeNo;

	/**
	 * 商家数据包，原样返回
	 */
	private String attach;

	/**
	 * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 */
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

	public String getSignType() {

		return signType;
	}

	public void setSignType(String signType) {

		this.signType = signType;
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

	public String getFeeType() {

		return feeType;
	}

	public void setFeeType(String feeType) {

		this.feeType = feeType;
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

	public String getCouponFee() {

		return couponFee;
	}

	public void setCouponFee(String couponFee) {

		this.couponFee = couponFee;
	}

	public String getCouponCount() {

		return couponCount;
	}

	public void setCouponCount(String couponCount) {

		this.couponCount = couponCount;
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
