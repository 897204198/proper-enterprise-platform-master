package com.proper.enterprise.platform.notice.server.push.sender;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgService;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class AbstractPushSendSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPushSendSupport.class);

    @Autowired
    private PushNoticeMsgService pushNoticeMsgService;
    /**
     * 推送渠道
     */
    public static final String PUSH_CHANNEL_KEY = "pushChannel";

    /**
     * 推送角标
     */
    public static final String BADGE_NUMBER_KEY = "_proper_badge";

    public static final String CUSTOM_PROPERTY_KEY = "customs";
    /**
     * 在request中获取推送渠道
     *
     * @param readOnlyNotice 只读消息
     * @return 推送渠道
     */
    protected PushChannelEnum getPushChannel(ReadOnlyNotice readOnlyNotice) {
        if (null == readOnlyNotice.getTargetExtMsgMap()) {
            throw new ErrMsgException("the pushChannel can't be null");
        }
        String pushChannel = (String) readOnlyNotice.getTargetExtMsgMap().get(PUSH_CHANNEL_KEY);
        if (StringUtil.isEmpty(pushChannel)) {
            throw new ErrMsgException("the pushChannel can't be null");
        }
        try {
            return PushChannelEnum.valueOf(pushChannel);
        } catch (Exception e) {
            LOGGER.error("the pushChannel can't support,channel is {}",
                readOnlyNotice.getTargetExtMsgMap().get(PUSH_CHANNEL_KEY), e);
            throw new ErrMsgException("the pushChannel can't support");
        }
    }

    /**
     * 获取角标
     *
     * @param readOnlyNotice 只读消息
     * @return 角标数
     */
    protected Integer getBadgeNumber(ReadOnlyNotice readOnlyNotice) {
        if (null == readOnlyNotice.getNoticeExtMsgMap()) {
            return null;
        }
        Map<String, Object> noticeExtMsg = readOnlyNotice.getNoticeExtMsgMap();
        if (null == noticeExtMsg) {
            return null;
        }
        return (Integer) noticeExtMsg.get(BADGE_NUMBER_KEY);
    }

    /**
     * 是否透传
     *
     * @param readOnlyNotice 只读消息
     * @return true需要 false不需要 默认不需要
     */
    protected boolean isCmdMessage(ReadOnlyNotice readOnlyNotice) {
        Map<String, Object> noticeExtMsg = readOnlyNotice.getNoticeExtMsgMap();
        if (null == noticeExtMsg) {
            return false;
        }
        Object pushType = noticeExtMsg.get("_proper_pushtype");
        String cmdPush = "cmd";
        return cmdPush.equals(pushType);
    }

    /**
     * 获取自定义配置
     *
     * @param readOnlyNotice 只读消息
     * @return 自定义配置
     */
    protected Map getCustomProperty(ReadOnlyNotice readOnlyNotice) {
        if (null == readOnlyNotice.getNoticeExtMsgMap()) {
            return null;
        }
        return readOnlyNotice.getNoticeExtMsgMap();
    }

    /**
     * 将消息同步至推送
     *
     * @param readOnlyNotice 只读消息
     * @param pushChannel    推送渠道
     */
    public void updatePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel) {
        pushNoticeMsgService.updatePushMsg(readOnlyNotice, pushChannel);
    }

    /**
     * 将消息同步至推送
     *
     * @param messageId      第三方消息唯一标识
     * @param readOnlyNotice 只读消息
     * @param pushChannel    推送渠道
     */
    public void savePushMsg(String messageId, ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel) {
        pushNoticeMsgService.savePushMsg(messageId, readOnlyNotice, pushChannel);
    }
}
