package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushNoticeSupport;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service("xiaomiNoticeConfigurator")
public class XiaomiNoticeConfigurator extends AbstractPushNoticeSupport implements XiaomiNoticeClient {


    @Override
    public Sender getClient(String appKey) {
        return xiaomiSenderMap.get(appKey);
    }

    /**
     * 小米消息发送类管理池
     */
    private Map<String, Sender> xiaomiSenderMap = new HashMap<>();

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map configMap = super.post(appKey, config, request);
        Sender sender = new Sender((String) config.get("appSecret"));
        xiaomiSenderMap.put(appKey, sender);
        return configMap;
    }


    @Override
    public void delete(String appKey, HttpServletRequest request) {
        xiaomiSenderMap.remove(appKey);
        super.delete(appKey, request);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map configMap = super.put(appKey, config, request);
        Sender sender = new Sender((String) config.get("appSecret"));
        xiaomiSenderMap.put(appKey, sender);
        return configMap;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }
}
