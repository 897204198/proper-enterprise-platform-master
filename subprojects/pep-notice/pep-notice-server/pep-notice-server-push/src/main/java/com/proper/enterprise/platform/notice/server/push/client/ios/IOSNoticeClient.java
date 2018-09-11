package com.proper.enterprise.platform.notice.server.push.client.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.configurator.ios.IOSNoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class IOSNoticeClient implements IOSNoticeClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOSNoticeClient.class);

    private FileService fileService;

    @Autowired
    private IOSNoticeConfigurator iosNoticeConfigurator;

    @Autowired
    public IOSNoticeClient(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * ios客户端管理池
     */
    private Map<String, ApnsClient> apnsClientPool = new HashMap<>(16);

    @Override
    public ApnsClient get(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        ApnsClient client = apnsClientPool.get(appKey);
        if (null == client) {
            PushConfDocument pushConf = iosNoticeConfigurator.getConf(appKey, PushChannelEnum.IOS);
            if (null == pushConf) {
                throw new ErrMsgException("can't find conf by appKey:" + appKey);
            }
            try {
                apnsClientPool.put(appKey, initClient(pushConf));
            } catch (IOException e) {
                LOGGER.error("init ios client error,config:{}", pushConf.toString(), e);
                throw new ErrMsgException("init ios client error");
            }
        }
        return apnsClientPool.get(appKey);
    }

    private ApnsClient initClient(PushConfDocument pushDocument) throws IOException {
        String applePushUrl = ApnsClientBuilder.PRODUCTION_APNS_HOST;
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(applePushUrl);
        InputStream certInputStream = fileService.download(pushDocument.getCertificateId());
        builder = builder.setClientCredentials(certInputStream,
            pushDocument.getAppSecret());
        certInputStream.close();
        return builder.build();
    }

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        try {
            apnsClientPool.put(appKey, this.initClient(pushConf));
        } catch (IOException e) {
            LOGGER.error("init ios client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init ios client error");
        }
    }

    @Override
    public void delete(String appKey) {
        ApnsClient apnsClient = apnsClientPool.get(appKey);
        if (null == apnsClient) {
            return;
        }
        apnsClient.close();
    }

    @Override
    public void put(String appKey, PushConfDocument pushConf) {
        try {
            ApnsClient apnsClient = apnsClientPool.get(appKey);
            if (null != apnsClient) {
                apnsClient.close();
            }
            apnsClientPool.put(appKey, initClient(pushConf));
        } catch (IOException e) {
            LOGGER.error("init ios client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init ios client error");
        }
    }

}
