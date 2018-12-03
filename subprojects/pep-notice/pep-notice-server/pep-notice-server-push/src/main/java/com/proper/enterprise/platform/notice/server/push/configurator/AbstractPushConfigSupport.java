package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushProfileEnum;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class AbstractPushConfigSupport extends AbstractPushChannelSupport implements BasePushConfigApi {

    @Autowired
    private PushConfigMongoRepository pushRepository;

    private static final String PUSH_PACKAGE = "pushPackage";

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        if (null == config.get(PUSH_PACKAGE)) {
            throw new ErrMsgException("pushPackage can't be null");
        }
        PushConfDocument pushConf = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(params));
        if (null != pushConf) {
            throw new ErrMsgException("The current configuration of the appKey and push channels already exists");
        }
        pushRepository.save(buildPushDocument(appKey, config, params));
        return config;
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        pushRepository.deleteByAppKeyAndPushChannel(appKey, getPushChannel(params));
    }


    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        PushConfDocument existDocument = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(params));
        if (existDocument == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.notice.config.notExist"));
        }
        if (null == config.get(PUSH_PACKAGE)) {
            throw new ErrMsgException("pushPackage can't be null");
        }
        String pushDocumentId = pushRepository.findByAppKeyAndPushChannel(appKey, getPushChannel(params)).getId();
        PushConfDocument pushDocument = buildPushDocument(appKey, config, params);
        pushDocument.setId(pushDocumentId);
        return JSONUtil.parseIgnoreException(pushRepository.save(pushDocument).toString(), Map.class);
    }

    @Override
    public Map get(String appKey, Map<String, Object> request) {
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

    protected PushConfDocument buildPushDocument(String appKey, Map<String, Object> config, Map<String, Object> params) {
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setPushChannel(getPushChannel(params));
        if (pushDocument.getPushProfile() == null) {
            pushDocument.setPushProfile(PushProfileEnum.PRODUCTION);
        }
        return pushDocument;
    }

}
