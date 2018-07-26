package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeEntity;
import com.proper.enterprise.platform.notice.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.service.NoticeService;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.push.client.PusherApp;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    PusherApp pusherApp;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeSetService noticeSetService;

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    private static final String NOTICE_CHANNEL_SMS = "SMS";
    private static final String NOTICE_CHANNEL_NORMAL = "NORMAL";

    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           String toUserId,
                           Map<String, Object> custom,
                           String noticeChannelContent) {
        NoticeEntity noticeEntity = new NoticeEntity();
        if (noticeChannelContent.contains(NOTICE_CHANNEL_PUSH)) {
            pushNotice(title, content, toUserId, custom);
            noticeEntity.setPush(true);
        }
        if (noticeChannelContent.contains(NOTICE_CHANNEL_EMAIL)) {
            emailNotice();
            noticeEntity.setEmail(true);
        }
        if (noticeChannelContent.contains(NOTICE_CHANNEL_SMS)) {
            smsNotice();
            noticeEntity.setSms(true);
        }

        noticeEntity.setTitle(title);
        noticeEntity.setContent(content);
        noticeEntity.setBusinessId(businessId);
        noticeEntity.setBusinessName(businessName);
        noticeEntity.setToUserId(toUserId);
        noticeRepository.save(noticeEntity);
    }

    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           Set<String> toUserIds,
                           Map<String, Object> custom,
                           String noticeChannelContent) {
        for (String userId : toUserIds) {
            sendNotice(businessId, businessName, noticeType, title, content, userId, custom, noticeChannelContent);
        }
    }


    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           String toUserId,
                           Map<String, Object> custom) {
        String noticeChannelContent = noticeSetService.findNoticeSetsByNoticeTypeAndUserId(noticeType, toUserId);
        sendNotice(businessId, businessName, noticeType, title, content, toUserId, custom, noticeChannelContent);
    }

    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           Set<String> toUserIds,
                           Map<String, Object> custom) {
        for (String userId : toUserIds) {
            sendNotice(businessId, businessName, noticeType, title, content, userId, custom);
        }
    }

    @Override
    public List<NoticeEntity> findByNoticeChannelName(String noticeChannelName) {
        if (NOTICE_CHANNEL_NORMAL.equals(noticeChannelName)) {
            return noticeRepository.findByIsPushAndIsSmsAndIsEmail(false, false, false);
        }
        if (NOTICE_CHANNEL_EMAIL.equals(noticeChannelName)) {
            return noticeRepository.findByIsEmail(true);
        }
        if (NOTICE_CHANNEL_PUSH.equals(noticeChannelName)) {
            return noticeRepository.findByIsPush(true);
        }
        if (NOTICE_CHANNEL_SMS.equals(noticeChannelName)) {
            return noticeRepository.findByIsSms(true);
        }
        return noticeRepository.findAllByOrderByCreateTimeDesc();
    }

    private void pushNotice(String title,
                            String content,
                            String toUserId,
                            Map<String, Object> custom) {
        PushMessage msg = new PushMessage();
        msg.setTitle(title);
        msg.setContent(content);
        msg.setCustoms(custom);
        pusherApp.pushMessageToOneUser(msg, toUserId);
    }

    private void emailNotice() {

    }

    private void smsNotice() {

    }

}
