package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushNoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.document.PushDocument;
import com.proper.enterprise.platform.notice.server.push.repository.PushRepository;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushNoticeConfigurator implements IOSNoticeClient, NoticeConfigurator {

    private PushRepository pushRepository;

    private FileService fileService;

    @Autowired
    public IOSNoticeConfigurator(PushRepository pushRepository, FileService fileService) {
        this.pushRepository = pushRepository;
        this.fileService = fileService;
    }

    /**
     * ios客户端管理池
     */
    private Map<String, ApnsClient> apnsClientPool = new HashMap<>();

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushDocument pushDocument = BeanUtil.convert(config, PushDocument.class);
        pushDocument.setAppKey(appKey);
        pushDocument.setPushChannel(getPushChannel(request));
        apnsClientPool.put(appKey, initClient(pushDocument));
        return null;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        initClient(pushDocument).close()
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
        return null;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return null;
    }

    private ApnsClient initClient(PushDocument pushDocument) throws IOException {
        String applePushUrl = envProduct ? ApnsClientBuilder.PRODUCTION_APNS_HOST
            : ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(applePushUrl);

        builder = builder.setClientCredentials(fileService.download(pushDocument.getCertificateId()),
            pushDocument.getAppSecret());
        return builder.build();
    }

    @Override
    public ApnsClient getClient(String appKey) {
        return apnsClientPool.get(appKey);
    }
}
