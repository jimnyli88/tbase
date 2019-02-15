package cn.com.ut.jpush.service.impl;

import cn.com.ut.jpush.service.ReportService;
import cn.jiguang.common.TimeUnit;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.report.MessagesResult;
import cn.jpush.api.report.ReceivedsResult;
import cn.jpush.api.report.UsersResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 统计报表服务层
 * Created by lanbin on 2017/7/28.
 */
@Service
public class ReportServiceImpl implements ReportService {
	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Resource
	private JPushClient jPushClient;

	/**
	 * 获取统计
	 * @param msgIds
	 * @return
	 */
	@Override
	public ReceivedsResult getReportReceiveds(String msgIds) {

		ReceivedsResult result = null;
		try {
			result = jPushClient.getReportReceiveds(msgIds);
			logger.info("Got result - " + result);
		} catch (APIConnectionException e) {
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			logger.error("Should review the error, and fix the request", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
		return result;
	}

	/**
	 * 获取统计
	 * @return TimeUnit.DAY, "2014-06-10", 3
	 */
	@Override
	public UsersResult getReportUsers(TimeUnit timeUnit, String start, int duration) {

		UsersResult result = null;
		try {
			result = jPushClient.getReportUsers(timeUnit,start,duration);
			logger.info("Got result - " + result);
		} catch (APIConnectionException e) {
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			logger.error("Should review the error, and fix the request", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
		return result;
	}

	/**
	 * 获取统计
	 * @param msgIds
	 * @return
	 */
	@Override
	public MessagesResult getReportMessages(String msgIds) {

		MessagesResult result = null;
		try {
			result = jPushClient.getReportMessages(msgIds);
			logger.info("Got result - " + result);
		} catch (APIConnectionException e) {
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			logger.error("Should review the error, and fix the request", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
		return result;
	}
}
