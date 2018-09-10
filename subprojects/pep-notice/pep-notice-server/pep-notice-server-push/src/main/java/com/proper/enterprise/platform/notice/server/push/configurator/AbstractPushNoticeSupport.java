package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.document.PushDocument;
import com.proper.enterprise.platform.notice.server.push.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class AbstractPushNoticeSupport extends AbstractPushChannelSupport implements NoticeConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPushNoticeSupport.class);

    @Autowired
    private PushConfigMongoRepository pushRepository;


    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setPushChannel(getPushChannel(request));
        pushRepository.save(pushDocument);
        return config;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        pushRepository.deleteByAppKeyAndPushChannel(appKey, getPushChannel(request));
    }


    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushDocument existDocument = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request));
        if (existDocument == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.notice.config.notExist"));
        }
        String pushDocumentId = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request)).getId();
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setId(pushDocumentId);
        return JSONUtil.parseIgnoreException(pushRepository.save(pushDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        PushDocument pushDocument = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request));
        if (pushDocument != null) {
            Map result = JSONUtil.parseIgnoreException(pushDocument.toString(), Map.class);
            return result;
        }
        return null;
    }


}
