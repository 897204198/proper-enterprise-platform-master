package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.factory.PushConfiguratorFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service("pushNoticeConfigurator")
public class PushNoticeConfigurator extends AbstractPushNoticeConfigurator implements NoticeConfigurator {

    @Override
    public Map post(String appKey, Map config, HttpServletRequest request) {
        return PushConfiguratorFactory.product(getPushChannel(request)).post(appKey, config, request);
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        PushConfiguratorFactory.product(getPushChannel(request)).delete(appKey, request);
    }

    @Override
    public Map put(String appKey, Map config, HttpServletRequest request) {
        return PushConfiguratorFactory.product(getPushChannel(request)).post(appKey, config, request);
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return PushConfiguratorFactory.product(getPushChannel(request)).get(appKey, request);
    }


}
