package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.service.NoticeService;
import com.proper.enterprise.platform.push.client.PusherApp;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public boolean sendNotice(NoticeModel noticeModel) {
        //TODO auto add business (datadic)
        saveNotice(noticeModel);
        if (NOTICE_CHANNEL_PUSH.equals(noticeModel.getNoticeChannel())) {
            return pushNotice(noticeModel);
        }
        return false;
    }

    private NoticeDocument saveNotice(NoticeModel noticeModel) {
        DataDicLiteBean noticeChannel = new DataDicLiteBean("NOTICE_CHANNEL", noticeModel.getNoticeChannel());
        NoticeDocument noticeDocument = new NoticeDocument();
        noticeDocument.setSystemId(noticeModel.getSystemId());
        noticeDocument.setBusinessId(noticeModel.getBusinessId());
        noticeDocument.setNoticeChannel(noticeChannel);
        noticeDocument.setTitle(noticeModel.getTitle());
        noticeDocument.setContent(noticeModel.getContent());
        noticeDocument.setTarget(noticeModel.getTarget());
        return noticeRepository.save(noticeDocument);
    }

    private boolean pushNotice(NoticeModel noticeModel) {
        PushMessage msg = new PushMessage();
        msg.setTitle(noticeModel.getTitle());
        msg.setContent(noticeModel.getContent());
        msg.setCustoms(noticeModel.getCustom());
        pusherApp.pushMessageToOneUser(msg, noticeModel.getTarget());
        return true;
    }

    @Override
    public List<NoticeDocument> findByNoticeChannel(String noticeChannel) {
        DataDicLiteBean noticeChannelData = new DataDicLiteBean("NOTICE_CHANNEL", noticeChannel);
        return noticeRepository.findByNoticeChannel(noticeChannelData);
    }

}
