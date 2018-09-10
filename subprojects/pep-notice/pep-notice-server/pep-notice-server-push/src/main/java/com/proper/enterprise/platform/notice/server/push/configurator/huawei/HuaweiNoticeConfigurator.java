package com.proper.enterprise.platform.notice.server.push.configurator.huawei;

import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeConfigurator")
public class HuaweiNoticeConfigurator extends AbstractPushConfigSupport implements HuaweiNoticeClient, NoticeConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeConfigurator.class);

    public static final String APP_ID = "appId";

    /**
     * accessToken的过期时间
     */
    private Map<String, Map<String, Object>> accessTokenPool = new HashMap<>();

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(APP_ID)) {
            throw new ErrMsgException("appId can't be null");
        }
        Map result = super.post(appKey, config, request);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        refreshAccessTokenAndExpiredTime(pushDocument);
        return result;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
        accessTokenPool.remove(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map result = super.put(appKey, config, request);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        refreshAccessTokenAndExpiredTime(pushDocument);
        return result;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

    @Override
    public String getAccessToken(String appKey) {
        Map<String, Object> accessTokenDetail = accessTokenPool.get(appKey);
        long tokenExpiredTime = Long.parseLong(accessTokenDetail.get("token_expired_time").toString());
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            PushConfDocument pushDocument = getConf(appKey, PushChannelEnum.HUAWEI);
            refreshAccessTokenAndExpiredTime(pushDocument);
        }
        return (String) accessTokenPool.get(appKey).get("access_token");
    }

    @Override
    public PushConfDocument getConf(String appKey) {
        return getConf(appKey, PushChannelEnum.HUAWEI);
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
            response = post(tokenUrl, msgBody);
            obj = JSONObject.parseObject(response);

            Long tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
            String accessToken = obj.getString("access_token");
            Map<String, Object> accessTokenDetail = new HashMap<>(2);
            accessTokenDetail.put("access_token", accessToken);
            accessTokenDetail.put("token_expired_time", tokenExpiredTime);
            accessTokenPool.put(pushDocument.getAppKey(), accessTokenDetail);
        } catch (Exception e) {
            LOGGER.error("get accessToken failed with Exception {}", e);
        }
    }

    @Override
    public String post(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        return new String(post.getBody(), "UTF-8");
    }
}
