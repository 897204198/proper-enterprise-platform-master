package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.document.PushDocument;
import com.proper.enterprise.platform.notice.server.push.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class AbstractPushNoticeSupport extends AbstractPushChannelSupport implements NoticeConfigurator {

    @Autowired
    private PushConfigMongoRepository pushRepository;


    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get("appSecret")) {
            throw new ErrMsgException("appSecret can't be null");
        }
        if (null == config.get("pushPackage")) {
            throw new ErrMsgException("pushPackage can't be null");
        }
        pushRepository.save(buildPushDocument(appKey, config, request));
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
        if (null == config.get("appSecret")) {
            throw new ErrMsgException("appSecret can't be null");
        }
        if (null == config.get("pushPackage")) {
            throw new ErrMsgException("pushPackage can't be null");
        }
        String pushDocumentId = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request)).getId();
        PushDocument pushDocument = buildPushDocument(appKey, config, request);
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


    protected PushDocument buildPushDocument(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setPushChannel(getPushChannel(request));
        return pushDocument;
    }

}
