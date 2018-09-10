package com.proper.enterprise.platform.notice.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.push.document.PushDocument;
import com.proper.enterprise.platform.notice.server.push.repository.PushRepository;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("pushNoticeConfigurator")
public class PushNoticeConfigurator implements NoticeConfigurator {

    @Autowired
    private PushRepository pushRepository;

    @Autowired
    private I18NService i18NService;

    @Override
    public Map post(String appKey, Map config) {
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        return JSONUtil.parseIgnoreException(pushRepository.insert(pushDocument).toString(), Map.class);
    }

    @Override
    public void delete(String appKey) {
        pushRepository.deleteByAppKey(appKey);
    }

    @Override
    public Map put(String appKey, Map config) {
        PushDocument existDocument = pushRepository.findByAppKey(appKey);
        if (existDocument == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.push.notice.config.notExist"));
        }
        String pushDocumnetId = pushRepository.findByAppKey(appKey).getId();
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setId(pushDocumnetId);
        return JSONUtil.parseIgnoreException(pushRepository.save(pushDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey) {
        PushDocument pushDocument = pushRepository.findByAppKey(appKey);
        if (pushDocument != null) {
            return JSONUtil.parseIgnoreException(pushDocument.toString(), Map.class);
        }
        return null;
    }
}
