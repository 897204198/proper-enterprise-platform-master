package com.proper.enterprise.platform.notice.sms.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.sms.document.SMSDocument;
import com.proper.enterprise.platform.notice.sms.repository.SMSRepository;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("smsNoticeConfigurator")
public class SMSNoticeConfigurator implements NoticeConfigurator {

    @Autowired
    private SMSRepository smsRepository;

    @Autowired
    private I18NService i18NService;

    @Override
    public Map post(String appKey, Map config) {
        SMSDocument smsDocument = BeanUtil.convert(config, SMSDocument.class);
        smsDocument.setAppKey(appKey);
        return JSONUtil.parseIgnoreException(smsRepository.insert(smsDocument).toString(), Map.class);
    }

    @Override
    public void delete(String appKey) {
        smsRepository.deleteByAppKey(appKey);
    }

    @Override
    public Map put(String appKey, Map config) {
        SMSDocument existDocument = smsRepository.findByAppKey(appKey);
        if (existDocument == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.sms.notice.config.notExist"));
        }
        String smsDocumentId = existDocument.getId();
        SMSDocument smsDocument = BeanUtil.convert(config, SMSDocument.class);
        smsDocument.setAppKey(appKey);
        smsDocument.setId(smsDocumentId);
        return JSONUtil.parseIgnoreException(smsRepository.save(smsDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey) {
        SMSDocument smsDocument = smsRepository.findByAppKey(appKey);
        if (smsDocument != null) {
            return JSONUtil.parseIgnoreException(smsDocument.toString(), Map.class);
        }
        return null;
    }
}
