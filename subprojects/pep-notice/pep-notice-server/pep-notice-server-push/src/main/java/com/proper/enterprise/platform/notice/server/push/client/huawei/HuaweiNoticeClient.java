package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class HuaweiNoticeClient implements HuaweiNoticeClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeClient.class);

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository;

    /**
     * Huawei access token 池
     */
    private Map<String, Map<String, Object>> accessTokenPool = new HashMap<>();

    @Override
    public String getAccessToken(String appKey) {
        Map<String, Object> accessTokenDetail = accessTokenPool.get(appKey);
        if (accessTokenDetail == null) {
            PushConfDocument pushExistDocument = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            if (pushExistDocument == null) {
                throw new ErrMsgException("Can't get Huawei push config");
            }
            refreshAccessTokenAndExpiredTime(pushExistDocument);
            accessTokenDetail = accessTokenPool.get(appKey);
        }
        long tokenExpiredTime = Long.parseLong(accessTokenDetail.get("token_expired_time").toString());
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            PushConfDocument pushDocument = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            refreshAccessTokenAndExpiredTime(pushDocument);
            accessTokenDetail = accessTokenPool.get(appKey);
        }
        return (String) accessTokenDetail.get("access_token");
    }

    @Override
    public PushConfDocument getConf(String appKey) {
        return pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
    }

    @Override
    public String handlePost(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        return new String(post.getBody(), "UTF-8");
    }

    /**
     * 获取并刷新下发通知消息的认证 Token 及 Token 过期时间
     *
     * @param pushDocument 推送配置信息
     */
    private void refreshAccessTokenAndExpiredTime(PushConfDocument pushDocument) {
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        String msgBody = null;
        String response = null;
        JSONObject obj = null;
        try {
            msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(pushDocument.getAppSecret(), "UTF-8"), pushDocument.getAppId());
            response = handlePost(tokenUrl, msgBody);
            LOGGER.debug("Get huawei access token response: {}", response);
            obj = JSONObject.parseObject(response);

            Long tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
            String accessToken = obj.getString("access_token");
            Map<String, Object> accessTokenDetail = new HashMap<>(2);
            accessTokenDetail.put("access_token", accessToken);
            accessTokenDetail.put("token_expired_time", tokenExpiredTime);
            accessTokenPool.put(pushDocument.getAppKey(), accessTokenDetail);
        } catch (Exception e) {
            LOGGER.error("get accessToken failed with Exception {}", e);
            throw new ErrMsgException("Please check Huawei push config");
        }
    }

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        pushConf.setAppKey(appKey);
        refreshAccessTokenAndExpiredTime(pushConf);
    }

    @Override
    public void delete(String appKey) {
        accessTokenPool.remove(appKey);
    }

    @Override
    public void put(String appKey, PushConfDocument pushConf) {
        pushConf.setAppKey(appKey);
        refreshAccessTokenAndExpiredTime(pushConf);
    }
}
