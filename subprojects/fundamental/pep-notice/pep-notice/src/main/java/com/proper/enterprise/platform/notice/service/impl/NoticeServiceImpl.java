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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    AppServerRequestService service;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${pep.mail.mailDefaultFrom}")
    String emailFrom;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    //private static final String NOTICE_CHANNEL_SMS = "SMS";

    @Override
    public boolean saveNoticeAndCallNoticeChannel(NoticeModel noticeModel) {
        LOGGER.info("Notice Channel ...");
        saveNotice(noticeModel);
        if (NOTICE_CHANNEL_PUSH.equals(noticeModel.getNoticeChannel())) {
            return pushNotice(noticeModel);
        }
        if (NOTICE_CHANNEL_EMAIL.equals(noticeModel.getNoticeChannel())) {
            return emailNotice(noticeModel);
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
        noticeDocument.setFrom(noticeModel.getFrom());
        noticeDocument.setTargets(noticeModel.getTarget());
        return noticeRepository.save(noticeDocument);
    }

    private boolean pushNotice(NoticeModel noticeModel) {
        LOGGER.info("start push: " + JSONUtil.toJSONIgnoreException(noticeModel));
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTitle(noticeModel.getTitle());
        pushMessage.setContent(noticeModel.getContent());
        pushMessage.setCustoms(noticeModel.getCustom());
        List<String> lstUids = new ArrayList<>(noticeModel.getTarget());
        service.savePushMessageToUsers(noticeModel.getSystemId(), lstUids, pushMessage);
        return true;
    }

    private boolean emailNotice(NoticeModel noticeModel) {
        LOGGER.info("start email: " + JSONUtil.toJSONIgnoreException(noticeModel));
        List<String> targets = new ArrayList<>(noticeModel.getTarget());
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mailMessage, true, "GBK");
            String from = noticeModel.getFrom() == null ? emailFrom : noticeModel.getFrom();
            helper.setFrom(from);
            helper.setTo(targets.get(0));
            helper.setSubject(noticeModel.getTitle());
            helper.setText(noticeModel.getContent(), true);
            javaMailSender.send(mailMessage);
            return true;
        } catch (MessagingException e) {
            LOGGER.error("NoticeServiceImpl.emailNotice[Exception]:{}", e);
            return false;
        }
    }

    @Override
    public List<NoticeDocument> findByNoticeChannel(String noticeChannel) {
        DataDicLiteBean noticeChannelData = new DataDicLiteBean("NOTICE_CHANNEL", noticeChannel);
        return noticeRepository.findByNoticeChannel(noticeChannelData);
    }

}
