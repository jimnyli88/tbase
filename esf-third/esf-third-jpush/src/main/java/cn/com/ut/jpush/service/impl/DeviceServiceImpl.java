package cn.com.ut.jpush.service.impl;

import cn.com.ut.jpush.service.DeviceService;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.OnlineStatus;
import cn.jpush.api.device.TagAliasResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 手机设备服务层
 * Created by lanbin on 2017/7/31.
 */
public class DeviceServiceImpl implements DeviceService{

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    @Resource
    private JPushClient jpushClient;

    /**
     * 获取设备绑定的所有标签和别名
     * @param registrationId 设备ID
     * @return
     */
    @Override
    public TagAliasResult getDeviceTagAlias(String registrationId) {
        try {
            return jpushClient.getDeviceTagAlias(registrationId);
        } catch (APIConnectionException var1) {
            logger.error("Connection error. Should retry later. ", var1);
        } catch (APIRequestException var2) {
            logger.error("Error response from JPush server. Should review and fix it. ", var2);
            logger.info("HTTP Status: " + var2.getStatus());
            logger.info("Error Code: " + var2.getErrorCode());
            logger.info("Error Message: " + var2.getErrorMessage());
        }
        return null;
    }



    /**
     * 获取设备绑定的所有标签
     * @param registrationId 设备ID
     * @return
     */
    @Override
    public List<String> getDeviceTags(String registrationId) {
        try {
            return jpushClient.getDeviceTagAlias(registrationId).tags;
        } catch (APIConnectionException var1) {
            logger.error("Connection error. Should retry later. ", var1);
        } catch (APIRequestException var2) {
            logger.error("Error response from JPush server. Should review and fix it. ", var2);
            logger.info("HTTP Status: " + var2.getStatus());
            logger.info("Error Code: " + var2.getErrorCode());
            logger.info("Error Message: " + var2.getErrorMessage());
        }
        return null;
    }

    /**
     * 获取设备绑定的别名
     * @param registrationId 设备ID
     * @return
     */
    @Override
    public String getDeviceAlias(String registrationId) {
        try {
            TagAliasResult result = jpushClient.getDeviceTagAlias(registrationId);
            return result.alias;
        } catch (APIConnectionException var1) {
            logger.error("Connection error. Should retry later. ", var1);
        } catch (APIRequestException var2) {
            logger.error("Error response from JPush server. Should review and fix it. ", var2);
            logger.info("HTTP Status: " + var2.getStatus());
            logger.info("Error Code: " + var2.getErrorCode());
            logger.info("Error Message: " + var2.getErrorMessage());
        }
        return null;
    }

    /**
     * 获取用户在线状态
     * @param registrationIds
     */
    @Override
    public Map<String, OnlineStatus>  getUserOnlineStatus(String... registrationIds) {
        try {
            return jpushClient.getUserOnlineStatus(registrationIds);
        } catch (APIConnectionException var1) {
            logger.error("Connection error. Should retry later. ", var1);
        } catch (APIRequestException var2) {
            logger.error("Error response from JPush server. Should review and fix it. ", var2);
            logger.info("HTTP Status: " + var2.getStatus());
            logger.info("Error Code: " + var2.getErrorCode());
            logger.info("Error Message: " + var2.getErrorMessage());
        }
        return null;
    }

    /**
     * 绑定设备注册ID和手机号码
     * @param registrationId
     * @param mobile
     */
    @Override
    public void bindMobile(String registrationId, String mobile) {
        try {
            DefaultResult e = jpushClient.bindMobile(registrationId, mobile);
            logger.info("Got result " + e);
        } catch (APIConnectionException var1) {
            logger.error("Connection error. Should retry later. ", var1);
        } catch (APIRequestException var2) {
            logger.error("Error response from JPush server. Should review and fix it. ", var2);
            logger.info("HTTP Status: " + var2.getStatus());
            logger.info("Error Code: " + var2.getErrorCode());
            logger.info("Error Message: " + var2.getErrorMessage());
        }
    }
}
