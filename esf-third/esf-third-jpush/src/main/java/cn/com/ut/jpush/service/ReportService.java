package cn.com.ut.jpush.service;

import cn.jiguang.common.TimeUnit;
import cn.jpush.api.report.MessagesResult;
import cn.jpush.api.report.ReceivedsResult;
import cn.jpush.api.report.UsersResult;

/**
 * Created by lanbin on 2017/7/28.
 */
public interface ReportService {

    ReceivedsResult getReportReceiveds(String msgIds);

    UsersResult getReportUsers(TimeUnit timeUnit, String start, int duration);

    MessagesResult getReportMessages(String msgIds);
}
