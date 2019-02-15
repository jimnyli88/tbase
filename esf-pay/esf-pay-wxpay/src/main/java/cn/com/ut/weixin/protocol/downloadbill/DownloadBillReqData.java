package cn.com.ut.weixin.protocol.downloadbill;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

public class DownloadBillReqData extends ModuleBase {
	// 每个字段具体的意思请查看API文档
	@WeiXinField("device_info")
	private String deviceInfo;

	@WeiXinField("bill_date")
	private String billDate;

	@WeiXinField("bill_type")
	private String billType;

	@WeiXinField("tar_type")
	private String tarType;

	public String getDeviceInfo() {

		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {

		this.deviceInfo = deviceInfo;
	}

	public String getBillDate() {

		return billDate;
	}

	public void setBillDate(String billDate) {

		this.billDate = billDate;
	}

	public String getBillType() {

		return billType;
	}

	public void setBillType(String billType) {

		this.billType = billType;
	}

	public String getTarType() {

		return tarType;
	}

	public void setTarType(String tarType) {

		this.tarType = tarType;
	}

}
