package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;

public interface PushNoticeMsgService {

    /**
     * 根据消息服务端框架回调 同步插入推送记录
     *
     * @param readOnlyNotice 只读消息
     * @param pushChannel 推送渠道
     */
    void savePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel);
}
