package com.proper.enterprise.platform.notice.server.email.handler;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.email.configurator.EmailNoticeExtConfigurator;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service("emailNoticeSender")
public class EmailNoticeSendHandler implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNoticeSendHandler.class);

    private EmailNoticeExtConfigurator noticeConfigurator;

    private I18NService i18NService;

    @Autowired
    public EmailNoticeSendHandler(EmailNoticeExtConfigurator noticeConfigurator, I18NService i18NService) {
        this.noticeConfigurator = noticeConfigurator;
        this.i18NService = i18NService;
    }

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        LOGGER.info("start email: " + JSONUtil.toJSONIgnoreException(notice));
        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) noticeConfigurator.getJavaMailSender(notice.getAppKey());
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            Map<String, Object> extMessage = notice.getNoticeExtMsgMap();
            String fromKey = "from";
            // 获取消息中的发件人, 不设置则获取邮件服务器中配置的默认发件人
            if (extMessage != null && StringUtil.isNotBlank((String) extMessage.get(fromKey))) {
                helper.setFrom((String) extMessage.get(fromKey));
            } else {
                helper.setFrom(javaMailSender.getJavaMailProperties().getProperty("mail.from"));
            }
            String[] to = notice.getTargetTo().split(",");
            helper.setTo(to);
            if (notice.getTargetExtMsgMap() != null) {
                // 获取回复地址
                String replyTo = (String) notice.getTargetExtMsgMap().get("replyTo");
                if (StringUtil.isNotBlank(replyTo)) {
                    helper.setReplyTo(replyTo);
                }
                // 获取抄送地址
                String cc = (String) notice.getTargetExtMsgMap().get("cc");
                if (StringUtil.isNotBlank(cc)) {
                    helper.setCc(cc.split(","));
                }
                // 获取密送地址
                String bcc = (String) notice.getTargetExtMsgMap().get("bcc");
                if (StringUtil.isNotBlank(bcc)) {
                    helper.setBcc(bcc.split(","));
                }
            }
            // 设置发送时间
            if (notice.getNoticeExtMsgMap() != null) {
                String sentDate = (String) notice.getNoticeExtMsgMap().get("sentDate");
                if (StringUtil.isNotBlank(sentDate) && DateUtil.isDate(sentDate)) {
                    helper.setSentDate(DateUtil.parseGMTSpecial(sentDate));
                }
            }
            helper.setSubject(notice.getTitle());
            helper.setText(notice.getContent(), true);
            javaMailSender.send(mailMessage);
        } catch (MessagingException me) {
            LOGGER.error("NoticeServiceImpl.emailNotice[MessagingException]:{}", me);
            throw new NoticeException(i18NService.getMessage("pep.email.notice.send.error"), me);
        } catch (Exception e) {
            LOGGER.error("NoticeServiceImpl.emailNotice[Exception]:{}", e);
            throw new NoticeException(i18NService.getMessage("pep.email.notice.send.error"), e);
        }
    }

    @Override
    public void beforeSend(BusinessNotice notice) {

    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) {
        return NoticeStatus.SUCCESS;
    }
}
