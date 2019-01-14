package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeSenderImpl implements NoticeSender {

    @Autowired
    private NoticeSendServiceImpl noticeSendService;

    @Override
    public void sendNotice(String toUserId, String code, Map<String, Object> custom) {
        Map<String, Object> templateParams = new HashMap<>(0);
        sendNotice(toUserId, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String toUserId, String code, Map<String, Object> templateParams, Map<String, Object>
        custom) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(userIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String toUserId, String title, String content, Map<String, Object> custom, String catalog, NoticeType
        noticeType) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(userIds, title, content, custom, catalog, noticeType);
    }

    @Override
    public void sendNotice(Set<String> toUserIds, String code, Map<String, Object> custom) {
        Map<String, Object> templateParams = new HashMap<>(0);
        sendNotice(toUserIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(Set<String> toUserIds, String code, Map<String, Object> templateParams,
                           Map<String, Object> custom) {
        sendNotice(null, toUserIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(Set<String> toUserIds, String title, String content, Map<String, Object> custom,
                           String catalog, NoticeType noticeType) {
        sendNotice(null, toUserIds, title, content, custom, catalog, noticeType);
    }

    @Override
    public void sendNotice(String fromUserId, String toUserId, String code, Map<String, Object> custom) {
        Map<String, Object> templateParams = new HashMap<>(0);
        sendNotice(fromUserId, toUserId, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String fromUserId, String toUserId, String code, Map<String, Object> templateParams,
                           Map<String, Object> custom) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(fromUserId, userIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String fromUserId, String toUserId, String title, String content, Map<String, Object> custom,
                           String catalog, NoticeType noticeType) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(fromUserId, userIds, title, content, custom, catalog, noticeType);
    }

    @Override
    public void sendNotice(String fromUserId, Set<String> toUserIds, String code, Map<String, Object> custom) {
        Map<String, Object> templateParams = new HashMap<>(0);
        sendNotice(fromUserId, toUserIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String fromUserId, Set<String> toUserIds, String code, Map<String, Object> templateParams,
                           Map<String, Object> custom) {
        noticeSendService.sendNoticeChannel(fromUserId, toUserIds, code, templateParams, custom);
    }

    @Override
    public void sendNotice(String fromUserId, Set<String> toUserIds, String title, String content, Map<String, Object> custom,
                           String catalog, NoticeType noticeType) {
        noticeSendService.sendNoticeChannel(fromUserId, toUserIds, title, content, custom, catalog, noticeType);
    }

    @Override
    public void sendNoticeSMS(String phone, String content, Map<String, Object> custom) {
        noticeSendService.sendNoticeSMS(phone, content, custom);
    }

}
