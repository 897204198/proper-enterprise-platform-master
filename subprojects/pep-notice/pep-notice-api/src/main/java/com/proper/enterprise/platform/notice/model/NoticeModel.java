package com.proper.enterprise.platform.notice.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NoticeModel {

    /**
     * 系统ID
     */
    private String systemId;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 通知类型
     */
    private String noticeType;

    /**
     * 通知渠道
     */
    private String noticeChannel;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知正文
     */
    private String content;

    /**
     * 通知接收人ID集合 为空时代表广播通知
     */
    private Set<String> target;

    /**
     * 通知自定义字段
     */
    private Map<String, Object> custom;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeChannel() {
        return noticeChannel;
    }

    public void setNoticeChannel(String noticeChannel) {
        this.noticeChannel = noticeChannel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }

    public Set<String> getTarget() {
        return target;
    }

    public void setTarget(Set<String> target) {
        this.target = target;
    }

    public void addCustom(String key, String value) {
        if (custom == null) {
            custom = new HashMap<>(1);
        }
        custom.put(key, value);
    }
}
