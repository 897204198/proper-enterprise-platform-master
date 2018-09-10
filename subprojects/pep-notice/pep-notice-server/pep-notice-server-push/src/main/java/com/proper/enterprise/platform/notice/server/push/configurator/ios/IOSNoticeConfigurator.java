package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushNoticeSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushDocument;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushNoticeSupport implements IOSNoticeClient {

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
        super.post(appKey, config, request);
        apnsClientPool.put(appKey, initClient(pushDocument));
        return null;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map updateConf = super.put(appKey, config, request);
        return updateConf;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
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
