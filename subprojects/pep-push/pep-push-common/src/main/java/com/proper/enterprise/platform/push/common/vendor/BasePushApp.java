package com.proper.enterprise.platform.push.common.vendor;

import java.util.Map;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proper.enterprise.platform.push.common.model.PushMsg;

public class BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePushApp.class);
    /**
     * 判断是否为透传消息
     *
     * @param msg
     * @return
     */
    public boolean isCmdMessage(PushMsg msg) {
        Map<String, Object> customs = msg.getMcustomDatasMap();
        if (customs != null) {
            Object pushType = customs.get("_proper_pushtype");
            if (pushType != null && "cmd".equals(pushType)) {
                return true;
            }
        }
        return false;
    }

    public Integer getBadgeNumber(PushMsg msg) {
        Integer badgeNumber = null;
        Object badge = msg.getMcustomDatasMap().get("_proper_badge");
        if (badge != null) {
            try {
                badgeNumber = Integer.valueOf(badge.toString());
                if (badgeNumber < 0) {
                    badgeNumber = 0;
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return badgeNumber;
    }

    protected AbstractPushVendorService pushService;

    public AbstractPushVendorService getPushService() {
        return pushService;
    }

    public void setPushService(AbstractPushVendorService pushService) {
        this.pushService = pushService;
    }

    /**
     *  是否向第三方推送服务器推送消息，在测试环境下，将不请求真实推送服务器
     * @return
     */
    protected boolean isReallySendMsg(){
       return !StringUtil.equals("test", ConfCenter.get("push_env"));
    }
}
