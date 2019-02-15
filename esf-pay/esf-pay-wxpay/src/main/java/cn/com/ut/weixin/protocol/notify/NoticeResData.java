package cn.com.ut.weixin.protocol.notify;

import cn.com.ut.weixin.common.WeiXinField;

/**
 * 商户处理后同步返回给微信参数
 * 
 * @author ouyuexing
 *
 */
public class NoticeResData {
	/**
	 * SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功 ，成功后微信就不会继续推送了 请谨慎
	 */
	@WeiXinField("return_code")
	private String returnCode;

	/**
	 * 返回信息，如非空，为错误原因：签名失败,参数格式校验错误
	 */
	@WeiXinField("return_msg")
	private String returnMsg;

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

}
