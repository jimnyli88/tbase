package cn.com.ut.weixin.protocol.payquery;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

public class ScanPayQueryReqData extends ModuleBase {

	// 每个字段具体的意思请查看API文档
	@WeiXinField("transaction_id")
	private String transactionId;

	@WeiXinField("out_trade_no")
	private String outTradeNo;

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

}
