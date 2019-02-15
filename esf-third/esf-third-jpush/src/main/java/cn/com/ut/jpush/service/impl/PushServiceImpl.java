package cn.com.ut.jpush.service.impl;

import cn.com.ut.jpush.service.PushService;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 推送服务层
 * Created by lanbin on 2017/7/28.
 */
@Service
public class PushServiceImpl implements PushService {
	private static final Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

	@Resource
	private JPushClient jPushClient;

	/**
	 * 推送到所有平台，所有设备
	 */
	@Override
	public PushResult pushAll(String msg) {

		PushPayload payload = PushPayload.alertAll(msg);
		return push(payload);
	}

	/**
	 * 推送到所有平台，推送目标是为指定别名的设备
	 */
	@Override
	public PushResult pushAllByAlias(String msg, Collection<String> aliases) {

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.alias(aliases)).setNotification(Notification.alert(msg))
				.build();

		return push(payload);
	}

	/**
	 * 推送到所有平台，推送目标是为指定别名的设备
	 */
	@Override
	public PushResult pushAllByAlias(String msg, String... alias) {

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.alias(alias)).setNotification(Notification.alert(msg))
				.build();

		return push(payload);
	}

	/**
	 * 推送到所有平台，推送目标是指定tag 的设备(tag 并集)
	 */
	@Override
	public PushResult pushAllByTags(String msg, String... tags) {

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tags)).setNotification(Notification.alert(msg))
				.build();

		return push(payload);
	}

	/**
	 * 推送到所有平台，推送目标是指定tag 的设备(tag 并集)
	 */
	@Override
	public PushResult pushAllByTags(String msg, Collection<String> tags) {

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tags)).setNotification(Notification.alert(msg))
				.build();

		return push(payload);
	}
	/**
	 * 推送到所有平台，推送目标是指定tag 的设备（tag 交集）
	 */
	@Override
	public PushResult pushAllByTagsAnd(String msg, String... tags) {

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag_and(tags)).setNotification(Notification.alert(msg))
				.build();

		return push(payload);
	}

	/**
	 * 推送到所有平台，推送目标是指定tag 的设备（tag 交集）
	 */
	@Override
	public PushResult pushAllByTagsAnd(String msg, Collection<String> tags) {

		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag_and(tags)).setNotification(Notification.alert(msg))
				.build();
		return push(payload);
	}

	/**
	 * 推送
	 * @param payload
	 * @return
	 */
	@Override
	public PushResult push(PushPayload payload) {

		PushResult result = null;
		try {
			result = jPushClient.sendPush(payload);
			logger.debug("Got result - " + result);
		} catch (APIConnectionException e) {
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			logger.error("Should review the error, and fix the request", e);
			logger.debug("HTTP Status: " + e.getStatus());
			logger.debug("Error Code: " + e.getErrorCode());
			logger.debug("Error Message: " + e.getErrorMessage());
		}
		return result;
	}
}
