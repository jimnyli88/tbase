package cn.com.ut.weixin.protocol.refundquery;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

public class RefundQueryReqData extends ModuleBase {
	// 每个字段具体的意思请查看API文档
	@WeiXinField("device_info")
	private String deviceInfo;

	@WeiXinField("transaction_id")
	private String transactionId;

	@WeiXinField("out_trade_no")
	private String outTradeNo;

	@WeiXinField("out_refund_no")
	private String outRefundNo;

	@WeiXinField("refund_id")
	private String refundId;

	public String getDeviceInfo() {

		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
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

}
