package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.factory.PushConfiguratorFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("pushNoticeConfigurator")
public class PushNoticeConfigurator extends AbstractPushChannelSupport implements NoticeConfigurator {

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> request) {
        return PushConfiguratorFactory.product(getPushChannel(request)).post(appKey, config, request);
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        PushConfiguratorFactory.product(getPushChannel(params)).delete(appKey, params);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        return PushConfiguratorFactory.product(getPushChannel(params)).put(appKey, config, params);
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        return PushConfiguratorFactory.product(getPushChannel(params)).get(appKey, params);
    }


}
