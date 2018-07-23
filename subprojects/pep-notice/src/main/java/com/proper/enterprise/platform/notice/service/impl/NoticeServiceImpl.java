package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.entity.NoticeEntity;
import com.proper.enterprise.platform.notice.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.service.NoticeService;
import com.proper.enterprise.platform.push.client.PusherApp;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    PusherApp pusherApp;

    @Autowired
    NoticeRepository noticeRepository;

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    private static final String NOTICE_CHANNEL_SMS = "SMS";
    private static final String NOTICE_CHANNEL_NORMAL = "NORMAL";


    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String title,
                           String content,
                           String toUserId,
                           Map<String, Object> custom,
                           String noticeChannelContent) {
        String noticeChannelCode = "";
        // 推送消息
        if (noticeChannelContent.contains(NOTICE_CHANNEL_PUSH)) {
            pushNotice(title, content, toUserId, custom);
            noticeChannelCode = noticeChannelCode + "1";
        } else {
            noticeChannelCode = noticeChannelCode + "0";
        }

        // 发送邮件
        if (noticeChannelContent.contains(NOTICE_CHANNEL_EMAIL)) {
            emailNotice();
            noticeChannelCode = noticeChannelCode + "1";
        } else {
            noticeChannelCode = noticeChannelCode + "0";
        }

        // 发送短信
        if (noticeChannelContent.contains(NOTICE_CHANNEL_SMS)) {
            smsNotice();
            noticeChannelCode = noticeChannelCode + "1";
        } else {
            noticeChannelCode = noticeChannelCode + "0";
        }

        NoticeEntity noticeDocument = new NoticeEntity();
        noticeDocument.setTitle(title);
        noticeDocument.setContent(content);
        noticeDocument.setBusinessId(businessId);
        noticeDocument.setBusinessName(businessName);
        noticeDocument.setReaded(false);
        noticeDocument.setToUserId(toUserId);
        noticeDocument.setNoticeChannelCode(noticeChannelCode);
        noticeRepository.save(noticeDocument);
    }

    @Override
    public void sendNotice(String businessId,
                           String businessName,
                           String title,
                           String content,
                           List<String> toUserIds,
                           Map<String, Object> custom,
                           String noticeChannelContent) {
        for (String userId : toUserIds) {
            sendNotice(businessId, businessName, title, content, userId, custom, noticeChannelContent);
        }
    }

    @Override
    public List<NoticeEntity> findByNoticeChannelName(String noticeChannelName) {
        String noticeChannelCode = null;
        if (NOTICE_CHANNEL_PUSH.equals(noticeChannelName)) {
            noticeChannelCode = "1__";
        } else if (NOTICE_CHANNEL_EMAIL.equals(noticeChannelName)) {
            noticeChannelCode = "_1_";
        } else if (NOTICE_CHANNEL_SMS.equals(noticeChannelName)) {
            noticeChannelCode = "__1";
        } else if (NOTICE_CHANNEL_NORMAL.equals(noticeChannelName)) {
            noticeChannelCode = "000";
        }
        if (StringUtil.isNull(noticeChannelCode)) {
            return noticeRepository.findAllByOrderByCreateTimeDesc();
        } else {
            return noticeRepository.findByNoticeChannelCodeLikeOrderByCreateTimeDesc(noticeChannelCode);
        }
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
