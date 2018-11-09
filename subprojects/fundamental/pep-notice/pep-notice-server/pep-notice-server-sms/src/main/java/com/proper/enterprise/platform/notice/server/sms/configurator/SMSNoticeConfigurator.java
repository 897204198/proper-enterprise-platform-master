package com.proper.enterprise.platform.notice.server.sms.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NService;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sms.document.SMSDocument;
import com.proper.enterprise.platform.notice.server.sms.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("smsNoticeConfigurator")
public class SMSNoticeConfigurator implements SMSConfigurator {

    @Autowired
    private SMSRepository smsRepository;

    @Autowired
    private I18NService i18NService;

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        if (null != smsRepository.findByAppKey(appKey)) {
            throw new ErrMsgException("The current configuration of the appKey already exists");
        }
        SMSDocument smsDocument = BeanUtil.convert(config, SMSDocument.class);
        smsDocument.setAppKey(appKey);
        smsDocument.setSmsTemplate(buildTemplate(smsDocument));
        return JSONUtil.parseIgnoreException(smsRepository.insert(smsDocument).toString(), Map.class);
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        smsRepository.deleteByAppKey(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        SMSDocument existDocument = smsRepository.findByAppKey(appKey);
        if (existDocument == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.sms.notice.config.notExist"));
        }
        String smsDocumentId = existDocument.getId();
        SMSDocument smsDocument = BeanUtil.convert(config, SMSDocument.class);
        smsDocument.setAppKey(appKey);
        smsDocument.setId(smsDocumentId);
        smsDocument.setSmsTemplate(buildTemplate(smsDocument));
        return JSONUtil.parseIgnoreException(smsRepository.save(smsDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        return get(appKey);
    }

    @Override
    public Map get(String appKey) {
        SMSDocument smsDocument = smsRepository.findByAppKey(appKey);
        if (smsDocument != null) {
            return JSONUtil.parseIgnoreException(smsDocument.toString(), Map.class);
        }
        return null;
    }

    private String buildTemplate(SMSDocument smsDocument) {
        String template = "UserId={userId}&Password={password}&Mobiles={0}&Content={1}";
        return template.replaceAll("\\{userId\\}", smsDocument.getUserId()).replaceAll("\\{password\\}", smsDocument.getPassword());
    }
}
