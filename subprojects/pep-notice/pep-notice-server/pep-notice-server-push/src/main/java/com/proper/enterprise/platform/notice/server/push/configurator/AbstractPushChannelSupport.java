package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class AbstractPushChannelSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPushChannelSupport.class);

    /**
     * 在request中获取推送渠道
     *
     * @param params params
     * @return 推送渠道
     */
    protected PushChannelEnum getPushChannel(Map<String, Object> params) {
        String pushChannel = (String) params.get("pushChannel");
        if (StringUtil.isEmpty(pushChannel)) {
            throw new ErrMsgException("the pushChannel can't be null");
        }
        try {
            return PushChannelEnum.valueOf(pushChannel);
        } catch (Exception e) {
            LOGGER.error("the pushChannel can't support,channel is {}", params.get("pushChannel"), e);
            throw new ErrMsgException("the pushChannel can't support");
        }
    }
}
