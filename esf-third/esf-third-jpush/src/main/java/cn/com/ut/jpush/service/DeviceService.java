package cn.com.ut.jpush.service;

import cn.jpush.api.device.OnlineStatus;
import cn.jpush.api.device.TagAliasResult;

import java.util.List;
import java.util.Map;

/**
 * Created by lanbin on 2017/7/28.
 */
public interface DeviceService {

    TagAliasResult getDeviceTagAlias(String registrationId);

    List<String> getDeviceTags(String registrationId);

    String getDeviceAlias(String registrationId);

    Map<String, OnlineStatus> getUserOnlineStatus(String... registrationIds);

    void bindMobile(String registrationId, String mobile);
}
