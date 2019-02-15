package cn.com.ut.weixin.protocol.refund;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

/**
 * 
 * @author ouyuexing
 * @since 2017年6月28日
 */
public class RefundReqData extends ModuleBase {

	// 每个字段具体的意思请查看API文档
	@WeiXinField("device_info")
	private String deviceInfo;

	@WeiXinField("transaction_id")
	private String transactionId;

	@WeiXinField("out_trade_no")
	private String outTradeNo;

	@WeiXinField("out_refund_no")
	private String outRefundNo;

	@WeiXinField("total_fee")
	private String totalFee = "0";

	@WeiXinField("refund_fee")
	private String refundFee = "0";

	@WeiXinField("refund_fee_type")
	private String refundFeeType = "CNY";

	@WeiXinField("op_user_id")
	private String opUserId;

	@WeiXinField("refund_account")
	private String refundAccount;

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

	public String getTotalFee() {

		return totalFee;
	}

	public void setTotalFee(String totalFee) {

		this.totalFee = totalFee;
	}

	public String getRefundFee() {

		return refundFee;
	}

	public void setRefundFee(String refundFee) {

		this.refundFee = refundFee;
	}

	public String getRefundFeeType() {

		return refundFeeType;
	}

	public void setRefundFeeType(String refundFeeType) {

		this.refundFeeType = refundFeeType;
	}

	public String getOpUserId() {

		return opUserId;
	}

	public void setOpUserId(String opUserId) {

		this.opUserId = opUserId;
	}

	public String getRefundAccount() {

		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {

		this.refundAccount = refundAccount;
	}

}
