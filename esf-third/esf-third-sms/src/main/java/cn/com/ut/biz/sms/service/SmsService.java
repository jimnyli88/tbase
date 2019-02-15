package cn.com.ut.biz.sms.service;

/**
 * @author: lanbin
 * @since: 2017年3月8日
 */
public interface SmsService {

	/**
	 * 发送短信通知（通用模板）
	 * 
	 * @param mobileNumber
	 * @param checkCode
	 */
	void sendDefaultSmsNotice(String mobileNumber, String checkCode);

	/**
	 * 发送注册短信通知
	 * 
	 * @param mobildNumber
	 * @param checkCode
	 */
	void sendRegisterSmsNotice(String mobileNumber, String checkCode);

	/**
	 * 发送修改用户手机短信通知
	 * 
	 * @param mobileNumber
	 * @param checkCode
	 */
	void sendModMobileSmsNotice(String mobileNumber, String checkCode);

	/**
	 * 发送忘记密码重置密码手机短信通知
	 * 
	 * @param mobileNumber
	 * @param checkCode
	 */
	void sendForgetPwdSmsNotice(String mobileNumber, String checkCode);

	// /**
	// * 发送修改用户密码手机短信通知
	// *
	// * @param mobileNumber
	// * @param checkCode
	// */
	// void sendModPwdSmsNotice(String mobileNumber, String checkCode);

}
