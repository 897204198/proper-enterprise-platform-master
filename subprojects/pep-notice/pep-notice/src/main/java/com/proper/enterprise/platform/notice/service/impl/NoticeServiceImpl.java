package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.service.NoticeService;
import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.api.openapi.service.AppServerRequestService;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    AppServerRequestService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    //private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    //private static final String NOTICE_CHANNEL_SMS = "SMS";

    @Override
    public boolean saveNoticeAndCallNoticeChannel(NoticeModel noticeModel) {
        LOGGER.info("Notice Channel ...");
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
        noticeDocument.setCustom(noticeModel.getCustom());
        noticeDocument.setContent(noticeModel.getContent());
        noticeDocument.setTargets(noticeModel.getTarget());
        return noticeRepository.save(noticeDocument);
    }

    private boolean pushNotice(NoticeModel noticeModel) {
        LOGGER.info("start push: ");
        try {
            LOGGER.info(JSONUtil.toJSON(noticeModel));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        PushMessage thePushmsg = getPushMessage(noticeModel);
        List<String> lstUids = new ArrayList<>(noticeModel.getTarget());
        service.savePushMessageToUsers(noticeModel.getSystemId(), lstUids, thePushmsg);
        return true;
    }

    private PushMessage getPushMessage(NoticeModel noticeModel) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTitle(noticeModel.getTitle());
        pushMessage.setContent(noticeModel.getContent());
        pushMessage.setCustoms(noticeModel.getCustom());
        return pushMessage;
    }

    @Override
    public List<NoticeDocument> findByNoticeChannel(String noticeChannel) {
        DataDicLiteBean noticeChannelData = new DataDicLiteBean("NOTICE_CHANNEL", noticeChannel);
        return noticeRepository.findByNoticeChannel(noticeChannelData);
    }

}
