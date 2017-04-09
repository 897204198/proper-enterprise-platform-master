package com.proper.enterprise.platform.push.client.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class PushMessage {
    String title = "";
    String content = "";
    Map<String, Object> customs = new LinkedHashMap<String, Object>();
    /**
     * 透传消息
     */
    public static final String PUSHTYPE_CMD = "cmd";
    /**
     * 通知栏消息
     */
    public static final String PUSHTYPE_NOTIFICATION = "notification";

    public static final String DEVICETYPE_ANDROID = "android";
    public static final String DEVICETYPE_IOS = "ios";

    public int getBadgeNumber() {
        int badgeNumber = 0;
        Object badge = customs.get("_proper_badge");
        if (badge != null) {
            badgeNumber = Integer.parseInt(badge.toString());
        }
        return badgeNumber;
    }

    /**
     * 设置应用角标
     * 
     * @param badgeNumber
     *            >=0 合法的角标数;=0时，清空手机端对应的角标；>0时，设置对应的角标数
     */
    public void setBadgeNumber(int badgeNumber) {
        if (badgeNumber < 0) {
            customs.remove("_proper_badge");
        } else {
            customs.put("_proper_badge", badgeNumber);
        }

    }

    public String getPushType() {
        String pushType = (String) customs.get("_proper_pushtype");
        if (pushType == null || "".equals(pushType)) {
            pushType = PUSHTYPE_NOTIFICATION;
        }
        return pushType;
    }

    public void setPushType(String pushType) {
        customs.put("_proper_pushtype", pushType);
    }

    /**
     * 创建一条要推送的消息
     * 
     * @param title
     *            消息标题
     * @param content
     *            消息内容
     * @param customs
     *            自定义的json键值对
     */
    public PushMessage(String title, String content, Map<String, Object> customs) {
        super();
        this.title = title;
        this.content = content;
        this.customs = customs;
    }

    public PushMessage(String title, String content) {
        super();
        this.title = title;
        this.content = content;
    }

    public PushMessage() {
        super();
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

    public Map<String, Object> getCustoms() {
        return customs;
    }

    public void setCustoms(Map<String, Object> customs) {
        this.customs = customs;
    }

    public void addCustomData(String key, Object value) {
        this.customs.put(key, value);
    }

}
