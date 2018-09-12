package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service("nspClientManager")
public class NSPClientManager implements HuaweiNoticeClientManagerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(NSPClientManager.class);

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository;

    @Autowired
    private FileService fileService;

    /**
     * Huawei client 池
     */
    private Map<String, NSPClient> nspClientPool = new HashMap<>(16);

    /**
     * Huawei AccessToken 池
     */
    private Map<String, AccessToken> accessTokenPool = new HashMap<>(16);

    public NSPClient getClient(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        NSPClient nspClient = nspClientPool.get(appKey);
        if (nspClient == null) {
            PushConfDocument pushConf1 = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            if (pushConf1 == null) {
                throw new ErrMsgException("Can't get Huawei push config");
            }
            try {
                nspClientPool.put(appKey, initClient(pushConf1));
            } catch (IOException e) {
                LOGGER.error("init huawei client error,config:{}", pushConf1.toString(), e);
                throw new ErrMsgException("init huawei client error");
            }
        }
        AccessToken accessToken = accessTokenPool.get(appKey);
        if (accessToken != null) {
            Long tokenExpiredTime = (Long) accessToken.getAttrs().get("token_expired_time");
            if (tokenExpiredTime <= System.currentTimeMillis()) {
                PushConfDocument pushConf2 = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
                try {
                    nspClientPool.put(appKey, initClient(pushConf2));
                } catch (IOException e) {
                    LOGGER.error("init huawei client error,config:{}", pushConf2.toString(), e);
                    throw new ErrMsgException("init huawei client error");
                }
            }
        }
        return nspClientPool.get(appKey);
    }

    @Override
    public HuaweiNoticeClient get(String appKey) {
        return null;
    }

    private NSPClient initClient(PushConfDocument pushDocument) throws IOException {
        AccessToken accessToken = initAccessToken(pushDocument);
        accessTokenPool.put(pushDocument.getAppKey(), accessToken);
        String pushUrl = "https://api.push.hicloud.com/pushsend.do";
        InputStream certInputStream = fileService.download(pushDocument.getCertificateId());
        NSPClient nspClient = new NSPClient(accessToken.getAccess_token());
        nspClient.setApiUrl(pushUrl);
        try {
            nspClient.initKeyStoreStream(certInputStream, pushDocument.getP12Password());
            certInputStream.close();
            return nspClient;
        } catch (NSPException e) {
            throw new ErrMsgException(e.getMessage());
        }
    }

    private AccessToken initAccessToken(PushConfDocument pushDocument) throws IOException {
        InputStream certInputStream = fileService.download(pushDocument.getCertificateId());
        OAuth2Client client = new OAuth2Client();
        try {
            client.initKeyStoreStream(certInputStream, pushDocument.getP12Password());
            AccessToken accessToken = client.getAccessToken("client_credentials",
                pushDocument.getAppId(), pushDocument.getAppSecret());
            Map<String, Object> attrs = new HashMap<>(1);
            Long tokenExpiredTime = System.currentTimeMillis() + accessToken.getExpires_in() - 5 * 60 * 1000;
            attrs.put("token_expired_time", tokenExpiredTime);
            accessToken.setAttrs(attrs);
            certInputStream.close();
            return accessToken;
        } catch (NSPException e) {
            throw new ErrMsgException(e.getMessage());
        }
    }

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        pushConf.setAppKey(appKey);
        try {
            nspClientPool.put(appKey, initClient(pushConf));
        } catch (ErrMsgException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("init huawei client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init huawei client error");
        }
    }

    @Override
    public void delete(String appKey) {
        nspClientPool.remove(appKey);
    }

    @Override
    public void put(String appKey, PushConfDocument pushConf) {
        pushConf.setAppKey(appKey);
        try {
            nspClientPool.put(appKey, initClient(pushConf));
        } catch (ErrMsgException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("init huawei client error,config:{}", pushConf.toString(), e);
            throw new ErrMsgException("init huawei client error");
        }
    }
}
