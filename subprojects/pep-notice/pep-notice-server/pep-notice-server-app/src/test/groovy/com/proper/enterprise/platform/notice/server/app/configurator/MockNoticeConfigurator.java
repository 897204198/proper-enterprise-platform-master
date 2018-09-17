package com.proper.enterprise.platform.notice.server.app.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.app.global.SingletonMap;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service("mockNoticeConfigurator")
public class MockNoticeConfigurator implements NoticeConfigurator {
    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        config.put(appKey, appKey);
        SingletonMap.getSingletMap().put(appKey, config);
        return config;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        SingletonMap.getSingletMap().remove(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        config.put(appKey, appKey + "put");
        SingletonMap.getSingletMap().put(appKey, config);
        return config;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return (Map) SingletonMap.getSingletMap().get(appKey);
    }
}
