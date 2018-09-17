package com.proper.enterprise.platform.notice.model;

import com.proper.enterprise.platform.core.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TargetModel implements Serializable {

    /**
     * 目标ID
     */
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
