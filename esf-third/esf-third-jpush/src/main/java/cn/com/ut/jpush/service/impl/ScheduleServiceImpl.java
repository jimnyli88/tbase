package cn.com.ut.jpush.service.impl;

import cn.com.ut.jpush.service.ScheduleService;
import cn.jiguang.common.TimeUnit;
import cn.jiguang.common.Week;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.schedule.ScheduleListResult;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.SchedulePayload;
import cn.jpush.api.schedule.model.TriggerPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * Created by lanbin on 2017/7/28.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

	@Resource
	private JPushClient jpushClient;

	@Override
	public void createSingleSchedule(String name, String time, PushPayload push) {

		try {
			ScheduleResult e = jpushClient.createSingleSchedule(name, time, push);
			logger.info("schedule result is " + e);
		} catch (APIConnectionException var5) {
			logger.error("Connection error. Should retry later. ", var5);
		} catch (APIRequestException var6) {
			logger.error("Error response from JPush server. Should review and fix it. ", var6);
			logger.info("HTTP Status: " + var6.getStatus());
			logger.info("Error Code: " + var6.getErrorCode());
			logger.info("Error Message: " + var6.getErrorMessage());
		}

	}

	@Override
	public void createDailySchedule(String name, String start, String end, String time,
			PushPayload push) {

		try {
			ScheduleResult e = jpushClient.createDailySchedule(name, start, end, time, push);
			logger.info("schedule result is " + e);
		} catch (APIConnectionException var7) {
			logger.error("Connection error. Should retry later. ", var7);
		} catch (APIRequestException var8) {
			logger.error("Error response from JPush server. Should review and fix it. ", var8);
			logger.info("HTTP Status: " + var8.getStatus());
			logger.info("Error Code: " + var8.getErrorCode());
			logger.info("Error Message: " + var8.getErrorMessage());
		}

	}

	@Override
	public void createWeeklySchedule(String name, String start, String end, String time,
			Week[] days, PushPayload push) {

		try {
			ScheduleResult e = jpushClient.createWeeklySchedule(name, start, end, time, days, push);
			logger.info("schedule result is " + e);
		} catch (APIConnectionException var8) {
			logger.error("Connection error. Should retry later. ", var8);
		} catch (APIRequestException var9) {
			logger.error("Error response from JPush server. Should review and fix it. ", var9);
			logger.info("HTTP Status: " + var9.getStatus());
			logger.info("Error Code: " + var9.getErrorCode());
			logger.info("Error Message: " + var9.getErrorMessage());
		}

	}

	@Override
	public void createMonthlySchedule(String name, String start, String end, String time,
			String[] points, PushPayload push) {

		try {
			ScheduleResult e = jpushClient.createMonthlySchedule(name, start, end, time, points,
					push);
			logger.info("schedule result is " + e);
		} catch (APIConnectionException var8) {
			logger.error("Connection error. Should retry later.", var8);
		} catch (APIRequestException var9) {
			logger.error("Error response from JPush server. Should review and fix it. ", var9);
			logger.info("HTTP Status: " + var9.getStatus());
			logger.info("Error Code: " + var9.getErrorCode());
			logger.info("Error Message: " + var9.getErrorMessage());
		}

	}

	@Override
	public void deleteSchedule(String scheduleId) {

		try {
			jpushClient.deleteSchedule(scheduleId);
		} catch (APIConnectionException var3) {
			logger.error("Connection error. Should retry later. ", var3);
		} catch (APIRequestException var4) {
			logger.error("Error response from JPush server. Should review and fix it. ", var4);
			logger.info("HTTP Status: " + var4.getStatus());
			logger.info("Error Code: " + var4.getErrorCode());
			logger.info("Error Message: " + var4.getErrorMessage());
		}

	}

	@Override
	public void getScheduleList(byte page) {

		try {
			ScheduleListResult e = jpushClient.getScheduleList(page);
			logger.info("total " + e.getTotal_count());
			Iterator var4 = e.getSchedules().iterator();

			while (var4.hasNext()) {
				ScheduleResult s = (ScheduleResult) var4.next();
				logger.info(s.toString());
			}
		} catch (APIConnectionException var5) {
			logger.error("Connection error. Should retry later. ", var5);
		} catch (APIRequestException var6) {
			logger.error("Error response from JPush server. Should review and fix it. ", var6);
			logger.info("HTTP Status: " + var6.getStatus());
			logger.info("Error Code: " + var6.getErrorCode());
			logger.info("Error Message: " + var6.getErrorMessage());
		}

	}

	@Override
	public void updateSchedule() {

		String scheduleId = "95bbd066-3a88-11e5-8e62-0021f652c102";
		JPushClient jpushClient = new JPushClient("b95ab661f79cf1e5499e2e1a",
				"c9c6449c0939f6932ba41ad0");
		String[] points = new String[] { Week.MON.name(), Week.FRI.name() };
		TriggerPayload trigger = TriggerPayload.newBuilder()
				.setPeriodTime("2015-08-01 12:10:00", "2015-08-30 12:12:12", "15:00:00")
				.setTimeFrequency(TimeUnit.WEEK, 2, points).buildPeriodical();
		SchedulePayload payload = SchedulePayload.newBuilder().setName("test_update_schedule")
				.setEnabled(Boolean.valueOf(false)).setTrigger(trigger).build();

		try {
			jpushClient.updateSchedule(scheduleId, payload);
		} catch (APIConnectionException var6) {
			logger.error("Connection error. Should retry later. ", var6);
		} catch (APIRequestException var7) {
			logger.error("Error response from JPush server. Should review and fix it. ", var7);
			logger.info("HTTP Status: " + var7.getStatus());
			logger.info("Error Code: " + var7.getErrorCode());
			logger.info("Error Message: " + var7.getErrorMessage());
		}

	}

	@Override
	public void getSchedule(String scheduleId) {

		try {
			ScheduleResult e = jpushClient.getSchedule(scheduleId);
			logger.info("schedule " + e);
		} catch (APIConnectionException var3) {
			logger.error("Connection error. Should retry later. ", var3);
		} catch (APIRequestException var4) {
			logger.error("Error response from JPush server. Should review and fix it. ", var4);
			logger.info("HTTP Status: " + var4.getStatus());
			logger.info("Error Code: " + var4.getErrorCode());
			logger.info("Error Message: " + var4.getErrorMessage());
		}
	}

}
