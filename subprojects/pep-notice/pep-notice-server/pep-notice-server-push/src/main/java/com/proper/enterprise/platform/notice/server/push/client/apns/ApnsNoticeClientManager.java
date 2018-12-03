package com.proper.enterprise.platform.notice.server.push.client.apns;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushProfileEnum;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApnsNoticeClientManager implements ApnsNoticeClientManagerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApnsNoticeClientManager.class);

    private FileService fileService;

    private PushConfigMongoRepository pushConfigMongoRepository;

    private static final String P12_PASSWORD_ERROR_MSG = "Given final block not properly padded";

    private static final String P12_PASSWORD_INCORRECT_MSG = "keystore password was incorrect";

    @Autowired
    public ApnsNoticeClientManager(FileService fileService, PushConfigMongoRepository pushConfigMongoRepository) {
        this.fileService = fileService;
        this.pushConfigMongoRepository = pushConfigMongoRepository;
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
            PushConfDocument pushConf = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.APNS);
            if (null == pushConf) {
                throw new ErrMsgException("can't find conf by appKey:" + appKey);
            }
            try {
                apnsClientPool.put(appKey, initClient(pushConf));
            } catch (IOException e) {
                LOGGER.error("init apns client error,config:{}", pushConf.toString(), e);
                throw new ErrMsgException("init apns client error");
            }
        }
        return apnsClientPool.get(appKey);
    }

    private ApnsClient initClient(PushConfDocument pushDocument) throws IOException {
        // 默认使用PRODUCTION_APNS_HOST发送
        String applePushUrl = ApnsClientBuilder.PRODUCTION_APNS_HOST;
        if (PushProfileEnum.DEV == pushDocument.getPushProfile()) {
            applePushUrl = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
        }
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(applePushUrl);
        InputStream certInputStream = fileService.download(pushDocument.getCertificateId());
        try {
            builder = builder.setClientCredentials(certInputStream, pushDocument.getCertPassword());
            certInputStream.close();
            return builder.build();
        } catch (IOException e) {
            if (null != e.getMessage() && e.getMessage().contains(P12_PASSWORD_ERROR_MSG)) {
                throw new ErrMsgException("Certificate and password do not match");
            }
            if (null != e.getMessage() && e.getMessage().contains(P12_PASSWORD_INCORRECT_MSG)) {
                throw new ErrMsgException("Certificate and password do not match");
            }
            throw e;
        }
    }

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        try {
            apnsClientPool.put(appKey, this.initClient(pushConf));
        } catch (ErrMsgException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("init apns client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init apns client error");
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
        } catch (ErrMsgException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("init apns client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init apns client error");
        }
    }

}
