package com.proper.enterprise.platform.oopsearch.sync.mongo.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;

public class ConfigUtil {

    private ConfigUtil() {
    }

    public static MongoMonitorClient getMongoMonitorConfig() {
        return PEPApplicationContext.getBean(MongoMonitorClient.class);
    }
}
