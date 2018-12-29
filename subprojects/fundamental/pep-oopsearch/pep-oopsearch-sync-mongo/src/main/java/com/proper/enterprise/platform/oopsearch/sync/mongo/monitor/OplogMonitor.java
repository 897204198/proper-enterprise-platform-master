package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.proper.enterprise.platform.oopsearch.sync.mongo.OopSearchSyncMongoProperties;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.executor.OplogExecutor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@DependsOn(value = "mongoSyncFullMongoManager")
public class OplogMonitor {

    @Autowired
    MongoMonitorClient mongoMonitorClient;

    @Autowired
    MongoProperties mongoProperties;

    @Autowired
    OplogExecutor oplogExecutor;

    @Autowired
    OopSearchSyncMongoProperties oopSearchSyncMongoProperties;

    @PostConstruct
    public void init() {
        for (Map.Entry<String, MongoClient> client : mongoMonitorClient.getShardSetClients().entrySet()) {
            MongoIterable<String> collectionNames = client.getValue().getDatabase(mongoProperties.getDatabase()).listCollectionNames();
            List<String> ignoreCollectionNames = Arrays.asList(oopSearchSyncMongoProperties.getIgnoreCollectionAttr());
            for (String collectionName : collectionNames) {
                if (ignoreCollectionNames.contains(collectionName)) {
                    continue;
                }
                start(client.getValue().getDatabase(mongoProperties.getDatabase()).getCollection(collectionName));
            }
        }
    }

    public void start(String collectionName) {
        for (Map.Entry<String, MongoClient> client : mongoMonitorClient.getShardSetClients().entrySet()) {
            MongoCollection<Document> collection = client.getValue()
                .getDatabase(mongoProperties.getDatabase()).getCollection(collectionName);
            if (null == collection) {
                continue;
            }
            start(collection);
        }
    }

    public void start(MongoCollection<Document> collection) {
        oplogExecutor.execute(collection);
    }
}
