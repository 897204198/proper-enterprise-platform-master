package com.proper.enterprise.platform.notice.email.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.email.document.EmailDocument;
import com.proper.enterprise.platform.notice.email.repository.EmailRepository;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("emailNoticeConfigurator")
public class EmailNoticeConfigurator implements NoticeConfigurator {

    private EmailRepository emailRepository;

    private I18NService i18NService;

    @Autowired
    public EmailNoticeConfigurator(EmailRepository emailRepository, I18NService i18NService) {
        this.emailRepository = emailRepository;
        this.i18NService = i18NService;
    }

    @Override
    public Map post(String appKey, Map config) {
        EmailDocument emailDocument = BeanUtil.convert(config, EmailDocument.class);
        emailDocument.setAppKey(appKey);
        return JSONUtil.parseIgnoreException(emailRepository.insert(emailDocument).toString(), Map.class);
    }

    @Override
    public void delete(String appKey) {
        emailRepository.deleteByAppKey(appKey);
    }

    @Override
    public Map put(String appKey, Map config) {
        EmailDocument existDocument = emailRepository.findByAppKey(appKey);
        if (existDocument == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.email.notice.config.notExist"));
        }
        String emailDocumentId = existDocument.getId();
        EmailDocument emailDocument = BeanUtil.convert(config, EmailDocument.class);
        emailDocument.setAppKey(appKey);
        emailDocument.setId(emailDocumentId);
        return JSONUtil.parseIgnoreException(emailRepository.save(emailDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey) {
        EmailDocument emailDocument = emailRepository.findByAppKey(appKey);
        if (emailDocument != null) {
            return JSONUtil.parseIgnoreException(emailDocument.toString(), Map.class);
        }
        return null;
    }
}
