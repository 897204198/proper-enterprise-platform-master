package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor;

import com.mongodb.MongoClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.executor.OplogExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@DependsOn(value = "mongoSyncFullMongoManager")
public class OplogMonitor {

    @Autowired
    MongoMonitorClient mongoMonitorClient;

    @Scheduled(fixedDelay = 1000L)
    public void syncMongoOplog() {
        for (Map.Entry<String, MongoClient> client : mongoMonitorClient.getShardSetClients().entrySet()) {
            new OplogExecutor(client, mongoMonitorClient.getTimeDB(), mongoMonitorClient).execute();
        }
    }
}
