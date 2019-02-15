package cn.com.ut.biz.sms.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.ut.biz.sms.service.SmsService;
import cn.com.ut.config.sms.JSMSConfig;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.exception.ServiceException;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;

/**
 * @author: lanbin
 * @since: 2017年3月8日
 */
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	private SMSClient smsClient;
	@Autowired
	private JSMSConfig jSMSConfig;
	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	/**
	 * 短信模板
	 */
	private enum SMS_TEMPLET {

		/** 注册通知模板 */
		REGISTER_NOTICE(40398, new String[] { "code", "datetime" }),

		/** 注册登录通知模板短格式 */
		LOGIN_NOTICE(148477, new String[] { "code" }),

		/** 修改手机号码通知模板 */
		MODMOBILE_NOTICE(40455, new String[] { "code" }),

		/** 忘记密码重置密码通知模板 */
		FORGETPWD_NOTICE(40280, new String[] { "code" }),

		/** 默认通知模板 */
		DEFAULT_NOTICE(40372, new String[] { "code" }),

		/** 默认验证码模板 */
		DEFAULT_VALID(1, new String[] {});

		private String[] tempParaKey;
		private Map<String, String> tempMap;
		private int tempId;

		SMS_TEMPLET(int tempId, String[] tempParaKey) {
			this.tempParaKey = tempParaKey;
			this.tempId = tempId;
			this.tempMap = new HashMap<>();
		}

		public void setTempMap(String[] tempParaValue) {

			for (int i = 0; i < tempParaValue.length; i++) {
				this.tempMap.put(tempParaKey[i], tempParaValue[i]);
			}
		}

		public Map<String, String> getTempMap() {

			return this.tempMap;
		}
	}

	/**
	 * DEMO示例：验证码类型，验证码由极光服务器指定
	 * 
	 * @param mobildNumber
	 *            手机号码
	 * @param checkCode
	 *            短信校验码
	 */
	public void sendRegisterSmsValid(String mobildNumber) {

		sendSMSCode(mobildNumber, SMS_TEMPLET.DEFAULT_VALID.tempId);
	}

	/**
	 * DEMO示例：语音短信验证码类型，验证码由极光服务器指定
	 */
	public void sendRegisterSmsVoice(String mobildNumber, String checkCode) {

		sendVoiceSMSCode(mobildNumber, SMS_TEMPLET.DEFAULT_VALID.tempId);
	}

	/**
	 * DEMO示例：校验短信验证码
	 * 
	 * @param msgId
	 *            sendRegisterSmsVoice或sendRegisterSmsValid接口返回的短信Id
	 * @param code
	 *            手机收到的短信验证码
	 * @return
	 */
	public boolean validSMSCode(String msgId, String code) {

		try {
			ValidSMSResult result = smsClient.sendValidSMSCode(msgId, code);
			return result.getIsValid();
		} catch (APIRequestException e) {
			logger.error("极光短信服务器错误: " + getErrMsgByCode(e.getStatus()));
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Message: " + e.getMessage());
			return false;
		} catch (APIConnectionException e) {
			logger.error("连接错误，请稍后再试。 ", e);
			return false;
		}
	}

	private void sendTemplateSMS(String mobildNumber, int tempId, Map<String, String> tempMap) {

		SMSPayload payload = SMSPayload.newBuilder().setMobildNumber(mobildNumber).setTempId(tempId)
				.setTempPara(tempMap).build();
		try {
			SendSMSResult result = smsClient.sendTemplateSMS(payload);
			StringBuilder sb = new StringBuilder();
			sb.append("getMessageId ").append(result.getMessageId()).append("\n");
			sb.append("getOriginalContent ").append(result.getOriginalContent()).append("\n");
			sb.append("getRateLimitQuota ").append(result.getRateLimitQuota()).append("\n");
			sb.append("getRateLimitRemaining ").append(result.getRateLimitRemaining()).append("\n");
			sb.append("getRateLimitReset ").append(result.getRateLimitReset()).append("\n");
			sb.append("getResponseCode ").append(result.getResponseCode()).append("\n");
			sb.append("isResultOK ").append(result.isResultOK());
			logger.debug("SendSMSResult:\n" + sb.toString());
		} catch (APIRequestException e) {
			logger.error("极光短信服务器错误: " + getErrMsgByCode(e.getErrorCode()));
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Message: " + e.getMessage());
		} catch (APIConnectionException e) {
			logger.error("连接错误，请稍后再试。 ", e);
		}
	}

	private void sendSMSCode(String MobildNumber, int tempId) {

		SMSPayload payload = SMSPayload.newBuilder().setMobildNumber(MobildNumber).setTempId(tempId)
				.build();
		try {
			SendSMSResult result = smsClient.sendSMSCode(payload);
			System.out.println("getMessageId " + result.getMessageId());
			System.out.println("getOriginalContent " + result.getOriginalContent());
			System.out.println("getRateLimitQuota " + result.getRateLimitQuota());
			System.out.println("getRateLimitRemaining " + result.getRateLimitRemaining());
			System.out.println("getRateLimitReset " + result.getRateLimitReset());
			System.out.println("getResponseCode " + result.getResponseCode());
			System.out.println("isResultOK " + result.isResultOK());
		} catch (APIRequestException e) {
			logger.error("极光短信服务器错误: " + getErrMsgByCode(e.getErrorCode()));
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Message: " + e.getMessage());
		} catch (APIConnectionException e) {
			logger.error("连接错误，请稍后再试。 ", e);
		}
	}

	private void sendVoiceSMSCode(String MobildNumber, int tempId) {

		SMSPayload payload = SMSPayload.newBuilder().setMobildNumber(MobildNumber).setTempId(tempId)
				.build();
		try {
			SendSMSResult result = smsClient.sendVoiceSMSCode(payload);
			System.out.println("getMessageId " + result.getMessageId());
			System.out.println("getOriginalContent " + result.getOriginalContent());
			System.out.println("getRateLimitQuota " + result.getRateLimitQuota());
			System.out.println("getRateLimitRemaining " + result.getRateLimitRemaining());
			System.out.println("getRateLimitReset " + result.getRateLimitReset());
			System.out.println("getResponseCode " + result.getResponseCode());
			System.out.println("isResultOK " + result.isResultOK());
		} catch (APIRequestException e) {
			logger.error("极光短信服务器错误: " + getErrMsgByCode(e.getErrorCode()));
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Message: " + e.getMessage());
		} catch (APIConnectionException e) {
			logger.error("连接错误，请稍后再试。 ", e);
		}
	}

	private String getErrMsgByCode(int code) {

		switch (code) {
		case 50001:
			return "auth为空 ";
		case 50002:
			return "auth鉴权失败";
		case 50003:
			return "body为空";
		case 50004:
			return "mobile为空";
		case 50005:
			return "temp_id为空";
		case 50006:
			return "mobile无效";
		case 50007:
			return "body无效";
		case 50008:
			return "没有短信验证权限";
		case 50009:
			return "发送超频";
		case 50010:
			return "验证码无效";
		case 50011:
			return "验证码过期";
		case 50012:
			return "验证码已验证过";
		case 50013:
			return "无效temp_id";
		case 50014:
			return "余额不足";
		case 50015:
			return "验证码为空";
		case 50016:
			return "api不存在";
		case 50017:
			return "媒体类型不支持";
		case 50018:
			return "请求方法不支持";
		case 50019:
			return "服务端异常";
		case 50020:
			return "模板审核中";
		case 50021:
			return "模板审核不通过";
		case 50022:
			return "模板参数未全部替换";
		case 50023:
			return "参数为空";
		case 50024:
			return "手机号已退订";
		}
		return "未知错误";
	}

	@Override
	public void sendModMobileSmsNotice(String mobildNumber, String checkCode) {

		devIntercept(mobildNumber, checkCode);
		sendTemplateSMS(mobildNumber, SMS_TEMPLET.MODMOBILE_NOTICE.tempId,
				SMS_TEMPLET.MODMOBILE_NOTICE.getTempMap());
	}

	@Override
	public void sendForgetPwdSmsNotice(String mobildNumber, String checkCode) {

		devIntercept(mobildNumber, checkCode);
		SMS_TEMPLET.FORGETPWD_NOTICE.setTempMap(new String[] { checkCode });
		sendTemplateSMS(mobildNumber, SMS_TEMPLET.FORGETPWD_NOTICE.tempId,
				SMS_TEMPLET.FORGETPWD_NOTICE.getTempMap());

	}

	@Override
	public void sendRegisterSmsNotice(String mobildNumber, String checkCode) {

		devIntercept(mobildNumber, checkCode);
		String dateTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		SMS_TEMPLET.REGISTER_NOTICE.setTempMap(new String[] { checkCode, dateTimeStr });
		sendTemplateSMS(mobildNumber, SMS_TEMPLET.REGISTER_NOTICE.tempId,
				SMS_TEMPLET.REGISTER_NOTICE.getTempMap());
		// SMS_TEMPLET.LOGIN_NOTICE.setTempMap(new String[] { checkCode });
		// sendTemplateSMS(mobildNumber, SMS_TEMPLET.LOGIN_NOTICE.tempId,
		// SMS_TEMPLET.LOGIN_NOTICE.getTempMap());
	}

	@Override
	public void sendDefaultSmsNotice(String mobildNumber, String checkCode) {

		devIntercept(mobildNumber, checkCode);
		SMS_TEMPLET.DEFAULT_NOTICE.setTempMap(new String[] { checkCode });
		sendTemplateSMS(mobildNumber, SMS_TEMPLET.DEFAULT_NOTICE.tempId,
				SMS_TEMPLET.DEFAULT_NOTICE.getTempMap());
	}

	private void devIntercept(String mobildNumber, String checkCode) {

		// 需要演示时，把throw注释即可
		if (ConstantUtil.FLAG_NO.equals(jSMSConfig.getOpen()))
			throw new ServiceException("开发阶段：假设已发送验证码【" + checkCode + "】到手机【" + mobildNumber + "】");
	}

}
