package com.proper.enterprise.platform.oopsearch.sync.mongo;

import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.OplogMonitor;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.sync.MongoDataSyncMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component(value = "mongoSyncFullMongoManager")
@Lazy(false)
@DependsOn("liquibase")
public class MongoFullSyncMongoManager implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoFullSyncMongoManager.class);

    @Autowired
    MongoDataSyncMongo mongoDataSyncMongo;

    @Autowired
    MongoMonitorClient mongoMonitorClient;

    @Autowired
    OplogMonitor oplogMonitor;

    @Override
    public void afterPropertiesSet() {
        try {
            mongoDataSyncMongo.fullSynchronization();
            mongoMonitorClient.resetSyncTime();
        } catch (Exception e) {
            LOGGER.warn("oopsearch sync error", e);
        }
    }
}
