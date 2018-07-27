package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeEntity;
import com.proper.enterprise.platform.notice.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.service.NoticeService;
import com.proper.enterprise.platform.push.client.PusherApp;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
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

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    //private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    //private static final String NOTICE_CHANNEL_SMS = "SMS";

    @Override
    public boolean sendNotice(String systemId,
                              String businessId,
                              String businessName,
                              String noticeType,
                              String target,
                              String title,
                              String content,
                              Map<String, Object> custom,
                              String noticeChannel) {
        //TODO auto add business (datadic)
        return sendNotice(systemId, businessId, target, title, content, custom, noticeChannel);
    }

    @Override
    public boolean sendNotice(String systemId,
                              String businessId,
                              String businessName,
                              String noticeType,
                              Set<String> targets,
                              String title,
                              String content,
                              Map<String, Object> custom,
                              String noticeChannel) {
        //TODO auto add business (datadic)
        boolean result = true;
        for (String target : targets) {
            boolean single = sendNotice(systemId, businessId, target, title, content, custom, noticeChannel);
            if (!single) {
                result = false;
            }
        }
        return result;
    }

    private boolean sendNotice(String systemId,
                               String businessId,
                               String target,
                               String title,
                               String content,
                               Map<String, Object> custom,
                               String noticeChannel) {
        DataDicLiteBean noticeChannelData = new DataDicLiteBean("NOTICE_CHANNEL", noticeChannel);
        if (NOTICE_CHANNEL_PUSH.equals(noticeChannel)) {
            return pushNotice(systemId, businessId, target, title, content, custom, noticeChannelData);
        }
        return false;
    }

    private boolean pushNotice(String systemId,
                               String businessId,
                               String target,
                               String title,
                               String content,
                               Map<String, Object> custom,
                               DataDicLiteBean noticeChannel) {
        PushMessage msg = new PushMessage();
        msg.setTitle(title);
        msg.setContent(content);
        msg.setCustoms(custom);
        pusherApp.pushMessageToOneUser(msg, target);

        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle(title);
        noticeEntity.setContent(content);
        noticeEntity.setSystemId(systemId);
        noticeEntity.setBusinessId(businessId);
        noticeEntity.setTarget(target);
        noticeEntity.setNoticeChannel(noticeChannel);
        noticeRepository.save(noticeEntity);
        return true;
    }

    @Override
    public List<NoticeEntity> findByNoticeChannel(String noticeChannel) {
        DataDicLiteBean noticeChannelData = new DataDicLiteBean("NOTICE_CHANNEL", noticeChannel);
        return noticeRepository.findByNoticeChannel(noticeChannelData);
    }

}
