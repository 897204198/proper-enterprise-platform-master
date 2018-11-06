package com.proper.enterprise.platform.notice.server.push.convert;

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushDeviceTypeEnum;

public class PushMsgConvert {
    /**
     * 将消息转换为PushMsg
     *
     * @param notice 只读消息
     * @return PushMsg
     */
    public static PushNoticeMsgEntity convert(ReadOnlyNotice notice) {
        PushNoticeMsgEntity pushNoticeMsg = new PushNoticeMsgEntity();
        pushNoticeMsg.setAppKey(notice.getAppKey());
        pushNoticeMsg.setContent(notice.getContent());
        pushNoticeMsg.setSendCount((notice.getRetryCount() == null ? 0 : notice.getRetryCount()) + 1);
        pushNoticeMsg.setStatus(notice.getStatus());
        pushNoticeMsg.setTitle(notice.getTitle());
        pushNoticeMsg.setTargetTo(notice.getTargetTo());
        pushNoticeMsg.setNoticeId(notice.getId());
        pushNoticeMsg.setBatchId(notice.getBatchId());
        pushNoticeMsg.setErrorMsg(notice.getErrorMsg());
        return pushNoticeMsg;
    }

    /**
     * 将推送渠道转成设备类型
     *
     * @param pushChannel 推送渠道
     * @return 设备类型
     */
    public static PushDeviceTypeEnum convert(PushChannelEnum pushChannel) {
        switch (pushChannel) {
            case APNS:
                return PushDeviceTypeEnum.IOS;
            case HUAWEI:
            case XIAOMI:
                return PushDeviceTypeEnum.ANDROID;
            default:
                return null;
        }
    }

}
