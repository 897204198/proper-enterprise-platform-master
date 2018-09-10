package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushNoticeSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushNoticeSupport implements IOSNoticeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOSNoticeConfigurator.class);

    private FileService fileService;

    @Autowired
    public IOSNoticeConfigurator(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * ios客户端管理池
     */
    private Map<String, ApnsClient> apnsClientPool = new HashMap<>();

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get("certificateId")) {
            throw new ErrMsgException("certificateId can't be null");
        }
        Map postConf = super.post(appKey, config, request);
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        try {
            apnsClientPool.put(appKey, initClient(pushDocument));
        } catch (IOException e) {
            LOGGER.error("init ios client error,config:{}", pushDocument.toString(), e);
            throw new ErrMsgException("init ios client error");
        }
        return postConf;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
        ApnsClient apnsClient = apnsClientPool.get(appKey);
        if (null == apnsClient) {
            return;
        }
        apnsClient.close();
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map updateConf = super.put(appKey, config, request);
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        try {
            ApnsClient apnsClient = apnsClientPool.get(appKey);
            if (null != apnsClient) {
                apnsClient.close();
            }
            apnsClientPool.put(appKey, initClient(pushDocument));
        } catch (IOException e) {
            LOGGER.error("init ios client error,config:{}", pushDocument.toString(), e);
            throw new ErrMsgException("init ios client error");
        }
        return updateConf;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

    private ApnsClient initClient(PushConfDocument pushDocument) throws IOException {
        String applePushUrl = envProduct ? ApnsClientBuilder.PRODUCTION_APNS_HOST
            : ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(applePushUrl);
        InputStream certInputStream = fileService.download(pushDocument.getCertificateId());
        builder = builder.setClientCredentials(certInputStream,
            pushDocument.getAppSecret());
        certInputStream.close();
        return builder.build();
    }

    @Override
    public ApnsClient getClient(String appKey) {
        return apnsClientPool.get(appKey);
    }

    @Override
    public PushConfDocument getConf(String appKey) {
        return getConf(appKey, PushChannelEnum.IOS);
    }
}
