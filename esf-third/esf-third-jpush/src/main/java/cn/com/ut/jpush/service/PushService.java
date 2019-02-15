package cn.com.ut.jpush.service;

import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

import java.util.Collection;

/**
 * Created by lanbin on 2017/7/28.
 */
public interface PushService {

    PushResult pushAll(String msg);

    PushResult pushAllByAlias(String msg, Collection<String> aliases);

    PushResult pushAllByAlias(String msg, String... alias);

    PushResult pushAllByTags(String msg, String... tags);

    PushResult pushAllByTags(String msg, Collection<String> tags);

    PushResult pushAllByTagsAnd(String msg, String... tags);

    PushResult pushAllByTagsAnd(String msg, Collection<String> tags);

    PushResult push(PushPayload payload);
}
