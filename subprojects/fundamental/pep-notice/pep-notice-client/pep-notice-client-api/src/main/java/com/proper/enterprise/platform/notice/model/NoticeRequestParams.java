package com.proper.enterprise.platform.notice.model;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NoticeRequestParams {

    private String fromUserId;
    private Set<String> toUserIds;
    private Map<String, NoticeSetDocument> noticeSetMap;
    private String title;
    private String content;
    private Map<String, Object> custom;
    private NoticeType noticeType;
    private Collection<? extends User> users;
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Set<String> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(Set<String> toUserIds) {
        this.toUserIds = toUserIds;
    }

    public Map<String, NoticeSetDocument> getNoticeSetMap() {
        return noticeSetMap;
    }

    public void setNoticeSetMap(Map<String, NoticeSetDocument> noticeSetMap) {
        this.noticeSetMap = noticeSetMap;
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

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public Collection<? extends User> getUsers() {
        return users;
    }

    public void setUsers(Collection<? extends User> users) {
        this.users = users;
    }
}
