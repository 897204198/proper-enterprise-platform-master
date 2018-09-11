package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.proper.enterprise.platform.notice.server.push.client.xiaomi.XiaomiNoticeClientApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 小米推送配置类
 */
@Service("xiaomiNoticeConfigurator")
public class XiaomiNoticeConfigurator extends AbstractPushConfigSupport {

    private XiaomiNoticeClientApi xiaomiNoticeClient;

    @Autowired
    public XiaomiNoticeConfigurator(XiaomiNoticeClientApi xiaomiNoticeClient) {
        this.xiaomiNoticeClient = xiaomiNoticeClient;
    }

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        xiaomiNoticeClient.post(appKey, pushDocument);
        return super.post(appKey, config, request);
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        xiaomiNoticeClient.delete(appKey);
        super.delete(appKey, request);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        xiaomiNoticeClient.put(appKey, pushDocument);
        return super.put(appKey, config, request);
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

}
