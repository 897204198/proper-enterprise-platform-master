package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.proper.enterprise.platform.notice.server.push.client.xiaomi.XiaomiNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 小米推送配置类
 */
@Service("xiaomiNoticeConfigurator")
public class XiaomiNoticeConfigurator extends AbstractPushConfigSupport {

    private XiaomiNoticeClientManagerApi xiaomiNoticeClient;

    @Autowired
    public XiaomiNoticeConfigurator(XiaomiNoticeClientManagerApi xiaomiNoticeClient) {
        this.xiaomiNoticeClient = xiaomiNoticeClient;
    }

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        PushConfDocument pushDocument = buildPushDocument(appKey, config, params);
        xiaomiNoticeClient.post(appKey, pushDocument);
        return super.post(appKey, config, params);
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        xiaomiNoticeClient.delete(appKey);
        super.delete(appKey, params);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        PushConfDocument pushDocument = buildPushDocument(appKey, config, params);
        xiaomiNoticeClient.put(appKey, pushDocument);
        return super.put(appKey, config, params);
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        return super.get(appKey, params);
    }

}
