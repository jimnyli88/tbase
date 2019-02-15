package cn.com.ut.weixin.protocol.paycloseorder;

import cn.com.ut.weixin.common.WeiXinField;

public class CloseOrderResData {

	/**
	 * 返回状态码
	 */
	@WeiXinField("return_code")
	private String returnCode;
	/**
	 * 返回信息
	 */
	@WeiXinField("return_msg")
	private String returnMsg;
	/**
	 * 公众账号ID
	 */
	private String appid;
	/**
	 * 商户号
	 */
	@WeiXinField("mch_id")
	private String mchId;
	/**
	 * 随机字符串
	 */
	@WeiXinField("nonce_str")
	private String nonceStr;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 业务结果
	 */
	@WeiXinField("result_code")
	private String resultCode;
	/**
	 * 业务结果描述
	 */
	@WeiXinField("result_msg")
	private String resultMsg;
	/**
	 * 错误代码
	 */
	@WeiXinField("err_code")
	private String errCode;
	/**
	 * 错误代码描述
	 */
	@WeiXinField("err_code_des")
	private String errCodeDes;

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

	public String getResultMsg() {

		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {

		this.resultMsg = resultMsg;
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
}
