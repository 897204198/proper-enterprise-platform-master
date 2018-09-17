package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class PushNoticeMsgStatisticVO {

    /**
     * 渠道
     */
    private String pushChannel;

    /**
     * 消息成功发送日期或者最后修改日期
     */
    private String sendDate;

    /**
     * 消息状态，是否发送
     */
    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    /**
     * 消息数量
     */
    private Integer msgCount;

    public String getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(String pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public Integer getMsgCount() {
        if (null == msgCount) {
            this.msgCount = 0;
        }
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
