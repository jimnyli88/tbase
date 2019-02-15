package cn.com.ut.weixin.protocol.payprotocol;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

/**
 * 请求扫码支付统一下单API需要提交的数据
 */
public class ScanPayReqData extends ModuleBase {
	// 每个字段具体的意思请查看API文档
	@WeiXinField("device_info")
	private String deviceInfo;

	private String body;

	private String detail;

	private String attach;

	@WeiXinField("out_trade_no")
	private String outTradeNo;

	@WeiXinField("fee_type")
	private String feeType;

	@WeiXinField("total_fee")
	private String totalFee = "0";

	@WeiXinField("spbill_create_ip")
	private String spbillCreateIp;

	@WeiXinField("time_start")
	private String timeStart;

	@WeiXinField("time_expire")
	private String timeExpire;

	@WeiXinField("goods_tag")
	private String goodsTag;

	@WeiXinField("notify_url")
	private String notifyUrl;

	@WeiXinField("trade_type")
	private String tradeType = "JSAPI";

	@WeiXinField("product_id")
	private String productId;

	@WeiXinField("limit_pay")
	private String limitPay;

	private String openid;

	public String getDeviceInfo() {

		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
	}

	public String getBody() {

		return body;
	}

	public void setBody(String body) {

		this.body = body;
	}

	public String getDetail() {

		return detail;
	}

	public void setDetail(String detail) {

		this.detail = detail;
	}

	public String getAttach() {

		return attach;
	}

	public void setAttach(String attach) {

		this.attach = attach;
	}

	public String getOutTradeNo() {

		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {

		this.outTradeNo = outTradeNo;
	}

	public String getFeeType() {

		return feeType;
	}

	public void setFeeType(String feeType) {

		this.feeType = feeType;
	}

	public String getTotalFee() {

		return totalFee;
	}

	public void setTotalFee(String totalFee) {

		this.totalFee = totalFee;
	}

	public String getSpbillCreateIp() {

		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {

		this.spbillCreateIp = spbillCreateIp;
	}

	public String getTimeStart() {

		return timeStart;
	}

	public void setTimeStart(String timeStart) {

		this.timeStart = timeStart;
	}

	public String getTimeExpire() {

		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {

		this.timeExpire = timeExpire;
	}

	public String getGoodsTag() {

		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {

		this.goodsTag = goodsTag;
	}

	public String getNotifyUrl() {

		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {

		this.notifyUrl = notifyUrl;
	}

	public String getTradeType() {

		return tradeType;
	}

	public void setTradeType(String tradeType) {

		this.tradeType = tradeType;
	}

	public String getProductId() {

		return productId;
	}

	public void setProductId(String productId) {

		this.productId = productId;
	}

	public String getLimitPay() {

		return limitPay;
	}

	public void setLimitPay(String limitPay) {

		this.limitPay = limitPay;
	}

	public String getOpenid() {

		return openid;
	}

	public void setOpenid(String openid) {

		this.openid = openid;
	}

}
