package com.proper.enterprise.platform.notice.server.push.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractPushNoticeSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPushNoticeSender.class);

    /**
     * 推送渠道
     */
    public static final String PUSH_CHANNEL_KEY = "pushChannel";

    /**
     * 推送角标
     */
    public static final String BADGE_NUMBER_KEY = "_proper_badge";

    /**
     * 自定义配置
     */
    public static final String CUSTOM_PROPERTY_KEY = "customs";

    /**
     * 在request中获取推送渠道
     *
     * @param readOnlyNotice 只读消息
     * @return 推送渠道
     */
    protected PushChannelEnum getPushChannel(ReadOnlyNotice readOnlyNotice) {
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
        return (Integer) readOnlyNotice.getNoticeExtMsgMap().get(BADGE_NUMBER_KEY);
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
        return (Map) readOnlyNotice.getNoticeExtMsgMap().get(CUSTOM_PROPERTY_KEY);
    }
}
