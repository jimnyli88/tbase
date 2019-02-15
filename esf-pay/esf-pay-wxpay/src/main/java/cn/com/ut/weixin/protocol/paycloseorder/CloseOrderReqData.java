package cn.com.ut.weixin.protocol.paycloseorder;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

public class CloseOrderReqData extends ModuleBase {

	/**
	 * 商户订单号
	 */
	@WeiXinField("out_trade_no")
	private String outTradeNo;

	public String getOutTradeNo() {

		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {

		this.outTradeNo = outTradeNo;
	}

}
