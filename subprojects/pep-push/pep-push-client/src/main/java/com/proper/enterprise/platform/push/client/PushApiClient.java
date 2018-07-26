package com.proper.enterprise.platform.push.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class PushApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushApiClient.class);

    @Async
    public String taskRun(PusherApp app, LinkedHashMap<String, Object> params) {
        try {
            return app.getPushApiRequest().requestServiceServer(app.getPushUrl(), params,
                app.getConnTimeout());
        } catch (Exception ex) {
            LOGGER.error("Exception:" + ex.getMessage(), ex);
        }
        return "";
    }
}
