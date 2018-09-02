package com.proper.enterprise.platform.notice.server.api.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

/**
 * 发送目标对象
 */
public class NoticeTarget {

    /**
     * 发送目标
     */
    @NotEmpty(message = "notice.server.param.target.cantBeEmpty")
    @Length(message = "notice.server.param.target.isTooLong", max = 255)
    private String to;

    /**
     * 发送扩展信息
     */
    @Length(message = "notice.server.param.targetExtMsg.isTooLong", max = 2048)
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
}
