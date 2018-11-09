package com.proper.enterprise.platform.notice.server.email.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.email.document.EmailDocument;
import com.proper.enterprise.platform.notice.server.email.repository.EmailRepository;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service("emailNoticeConfigurator")
public class EmailNoticeConfigurator implements EmailNoticeExtConfigurator {

    private static Map<String, JavaMailSender> configMap = new HashMap<>(1);

    private EmailRepository emailRepository;

    private I18NService i18NService;

    @Autowired
    public EmailNoticeConfigurator(EmailRepository emailRepository, I18NService i18NService) {
        this.emailRepository = emailRepository;
        this.i18NService = i18NService;
        for (EmailDocument emailDocument : emailRepository.findAll()) {
            JavaMailSenderImpl javaMailSender = initJavaMailSender(emailDocument);
            configMap.put(emailDocument.getAppKey(), javaMailSender);
        }
    }

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        EmailDocument emailDocument = BeanUtil.convert(config, EmailDocument.class);
        if (null != emailRepository.findByAppKey(appKey)) {
            throw new ErrMsgException("The current configuration of the appKey already exists");
        }
        emailDocument.setAppKey(appKey);
        emailDocument = emailRepository.insert(emailDocument);
        JavaMailSenderImpl javaMailSender = initJavaMailSender(emailDocument);
        configMap.put(emailDocument.getAppKey(), javaMailSender);
        return JSONUtil.parseIgnoreException(emailDocument.toString(), Map.class);
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        if (emailRepository.deleteByAppKey(appKey) > 0) {
            configMap.remove(appKey);
        }
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        EmailDocument existDocument = emailRepository.findByAppKey(appKey);
        if (existDocument == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.email.notice.config.notExist"));
        }
        String emailDocumentId = existDocument.getId();
        EmailDocument emailDocument = BeanUtil.convert(config, EmailDocument.class);
        emailDocument.setAppKey(appKey);
        emailDocument.setId(emailDocumentId);
        emailDocument = emailRepository.save(emailDocument);
        JavaMailSenderImpl javaMailSender = initJavaMailSender(emailDocument);
        configMap.put(emailDocument.getAppKey(), javaMailSender);
        return JSONUtil.parseIgnoreException(emailDocument.toString(), Map.class);
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        EmailDocument emailDocument = emailRepository.findByAppKey(appKey);
        if (emailDocument != null) {
            Map result = JSONUtil.parseIgnoreException(emailDocument.toString(), Map.class);
            return result;
        }
        return null;
    }

    /**
     * 配置邮件发送服务器
     *
     * @param emailDocument 邮件配置信息
     * @return 邮件发送服务器
     */
    private JavaMailSenderImpl initJavaMailSender(EmailDocument emailDocument) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailDocument.getMailServerHost());
        javaMailSender.setUsername(emailDocument.getMailServerUsername());
        javaMailSender.setPassword(emailDocument.getMailServerPassword());
        javaMailSender.setPort(emailDocument.getMailServerPort());
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.from", emailDocument.getMailServerDefaultFrom());
        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }

    @Override
    public JavaMailSender getJavaMailSender(String appKey) {
        return configMap.get(appKey);
    }
}
