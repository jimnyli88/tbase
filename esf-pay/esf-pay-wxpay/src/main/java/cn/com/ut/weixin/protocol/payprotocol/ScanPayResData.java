package cn.com.ut.weixin.protocol.payprotocol;

import cn.com.ut.weixin.common.WeiXinField;

/**
 * 统一下单接口提交Post数据给到API之后，API会返回XML格式的数据，这个类用来装这些数据
 */
public class ScanPayResData {

	// 协议层
	@WeiXinField("return_code")
	private String returnCode;

	@WeiXinField("return_msg")
	private String returnMsg;

	// 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
	private String appid;

	@WeiXinField("mch_id")
	private String mchId;

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

	// 业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
	@WeiXinField("trade_type")
	private String tradeType;

	@WeiXinField("prepay_id")
	private String prepayId;

	@WeiXinField("code_url")
	private String codeUrl;

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

	public String getTradeType() {

		return tradeType;
	}

	public void setTradeType(String tradeType) {

		this.tradeType = tradeType;
	}

	public String getPrepayId() {

		return prepayId;
	}

	public void setPrepayId(String prepayId) {

		this.prepayId = prepayId;
	}

	public String getCodeUrl() {

		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {

		this.codeUrl = codeUrl;
	}

}
