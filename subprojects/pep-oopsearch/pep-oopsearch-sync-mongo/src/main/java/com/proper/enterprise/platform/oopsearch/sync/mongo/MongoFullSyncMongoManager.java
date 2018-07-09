package com.proper.enterprise.platform.oopsearch.sync.mongo;

import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.OplogMonitor;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.sync.MongoDataSyncMongo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component(value = "mongoSyncFullMongoManager")
@Lazy(false)
@DependsOn("liquibase")
public class MongoFullSyncMongoManager implements InitializingBean {

    @Autowired
    MongoDataSyncMongo mongoDataSyncMongo;

    @Autowired
    MongoMonitorClient mongoMonitorClient;

    @Autowired
    OplogMonitor oplogMonitor;

    @Override
    public void afterPropertiesSet() {
        mongoDataSyncMongo.fullSynchronization();
        mongoMonitorClient.resetSyncTime();
    }
}
