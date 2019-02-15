package cn.com.ut.jpush.service;

import cn.jiguang.common.Week;
import cn.jpush.api.push.model.PushPayload;

/**
 * Created by lanbin on 2017/7/28.
 */
public interface ScheduleService {

    void createSingleSchedule(String name, String time, PushPayload push);

    void createDailySchedule(String name, String start, String end, String time, PushPayload push);

    void createWeeklySchedule(String name, String start, String end, String time, Week[] days, PushPayload push);

    void createMonthlySchedule(String name, String start, String end, String time, String[] points, PushPayload push);

    void deleteSchedule(String scheduleId);

    void getScheduleList(byte page);

    void updateSchedule();

    void getSchedule(String scheduleId);
}
