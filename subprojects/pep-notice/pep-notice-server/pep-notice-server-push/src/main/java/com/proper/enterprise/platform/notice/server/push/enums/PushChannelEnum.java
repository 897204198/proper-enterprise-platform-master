package com.proper.enterprise.platform.notice.server.push.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 推送渠道
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PushChannelEnum {
    /**
     * 华为
     */
    HUAWEI("华为"),
    /**
     * 小米
     */
    XIAOMI("小米"),
    /**
     * IOS
     */
    IOS("IOS");

    private PushChannelEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
