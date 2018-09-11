package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class AbstractPushConfigSupport extends AbstractPushChannelSupport implements BasePushConfigApi {

    @Autowired
    private PushConfigMongoRepository pushRepository;

    public static final String APP_SECRET = "appSecret";

    public static final String PUSH_PACKAGE = "pushPackage";

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(APP_SECRET)) {
            throw new ErrMsgException("appSecret can't be null");
        }
        if (null == config.get(PUSH_PACKAGE)) {
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
        PushConfDocument existDocument = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request));
        if (existDocument == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.notice.config.notExist"));
        }
        if (null == config.get(APP_SECRET)) {
            throw new ErrMsgException("appSecret can't be null");
        }
        if (null == config.get(PUSH_PACKAGE)) {
            throw new ErrMsgException("pushPackage can't be null");
        }
        String pushDocumentId = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request)).getId();
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        pushDocument.setId(pushDocumentId);
        return JSONUtil.parseIgnoreException(pushRepository.save(pushDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        PushConfDocument pushDocument = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(request));
        if (pushDocument != null) {
            Map result = JSONUtil.parseIgnoreException(pushDocument.toString(), Map.class);
            return result;
        }
        return null;
    }

    @Override
    public PushConfDocument getConf(String appKey, PushChannelEnum pushChannel) {
        return pushRepository.findByAppKeyAndPushChannel(appKey, pushChannel);
    }

    @Override
    public String getPushPackage(String appKey, PushChannelEnum pushChannel) {
        PushConfDocument pushConf = pushRepository.findByAppKeyAndPushChannel(appKey, pushChannel);
        if (null == pushConf) {
            throw new ErrMsgException("can't find conf by appKey:" + appKey);
        }
        return pushConf.getPushPackage();
    }

    protected PushConfDocument buildPushDocument(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setPushChannel(getPushChannel(request));
        return pushDocument;
    }

}
