package com.proper.enterprise.platform.notice.server.sdk.request;

import com.proper.enterprise.platform.core.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送目标对象
 */

public class NoticeTarget implements Serializable {

    /**
     * 发送目标
     */
    private String to;

    /**
     * 发送扩展信息
     */
    private Map<String, Object> targetExtMsg;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, Object> getTargetExtMsg() {
        return targetExtMsg;
    }

    public void setTargetExtMsg(Map<String, Object> targetExtMsg) {
        this.targetExtMsg = targetExtMsg;
    }


    public void setTargetExtMsg(String key, Object msg) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (null == this.targetExtMsg) {
            targetExtMsg = new HashMap<>(16);
        }
        targetExtMsg.put(key, msg);
    }
}
