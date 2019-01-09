package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config;

import com.mongodb.*;
import com.proper.enterprise.platform.oopsearch.sync.mongo.OopSearchSyncMongoProperties;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.BSONTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MongoMonitorClient {

    private MongoMonitorClient() {
    }

    private static final String COMMA_SYMBOL = ",";
    public static final String TIME_DB_NAME = "monitor_time_d";


    private MongoClient hostMongo;
    private Map<String, MongoClient> shardSetClients;
    private DB timeDB;

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    @Qualifier(value = "mongoSyncMongoNotice")
    private Notice notice;

    @Autowired
    private OopSearchSyncMongoProperties oopSearchSyncMongoProperties;

    @PostConstruct
    void persist() {
        initConfig();
    }

    public void resetSyncTime() {
        Set<String> shardTimeNames = this.getTimeDB().getCollectionNames();
        BSONTimestamp lastTimeStamp = new BSONTimestamp(getSecondTimestampTwo(new Date()), 1);
        for (String shardTime : shardTimeNames) {
            this.getTimeDB().getCollection(shardTime).update(new BasicDBObject(), new BasicDBObject("$set", new BasicDBObject("ts",
                lastTimeStamp)), true, true, WriteConcern.ACKNOWLEDGED);
        }
    }

    private void initConfig() {
        initClient();
        initShardSetClients();
        initTimeDB();
    }

    private void initClient() {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : mongoProperties.getHost().split(COMMA_SYMBOL)) {
            serverAddresses.add(new ServerAddress(address + ":" + mongoProperties.getPort()));
        }
        MongoCredential mongoCredential = getMongoCredential(mongoProperties.getUsername(), mongoProperties.getPassword());
        MongoClientOptions options = new MongoClientOptions.Builder().build();
        this.hostMongo = mongoCredential == null ? new MongoClient(serverAddresses, options) :
                                                   new MongoClient(serverAddresses, mongoCredential, options);
    }

    private MongoCredential getMongoCredential(String userName, char[] password) {
        return StringUtils.isNotBlank(userName) ? MongoCredential.createCredential(userName, "admin", password) : null;
    }

    private void initShardSetClients() {
        shardSetClients = new ShardSetFinder().findShardSets(hostMongo);
    }

    private void initTimeDB() {
        timeDB = hostMongo.getDB(TIME_DB_NAME);
    }

    private class ShardSetFinder {

        public Map<String, MongoClient> findShardSets(MongoClient mongoS) {
            DBCursor find = mongoS.getDB("admin").getSisterDB("config")
                .getCollection("shards").find();
            Map<String, MongoClient> shardSets = new HashMap<>(16);
            while (find.hasNext()) {
                DBObject next = find.next();
                String key = (String) next.get("_id");
                shardSets.put(key, getMongoClient(buildServerAddressList(next)));
            }
            if (shardSets.size() == 0) {
                shardSets.put("single", hostMongo);
            }
            find.close();
            return shardSets;
        }

        private MongoClient getMongoClient(List<ServerAddress> shardSet) {
            MongoClientOptions opts = new MongoClientOptions.Builder().readPreference(ReadPreference.primary()).build();
            MongoCredential credential = getMongoCredential(mongoProperties.getUsername(), mongoProperties.getPassword());
            return credential == null ? new MongoClient(shardSet, opts) : new MongoClient(shardSet, credential, opts);
        }

        private List<ServerAddress> buildServerAddressList(DBObject next) {
            List<ServerAddress> hosts = new ArrayList<>();
            for (String host : Arrays
                .asList(((String) next.get("host")).split("/")[1].split(COMMA_SYMBOL))) {
                hosts.add(new ServerAddress(host));
            }
            return hosts;
        }
    }

    private int getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.parseInt(timestamp);
    }

    public MongoClient getHostMongo() {
        return hostMongo;
    }

    public Map<String, MongoClient> getShardSetClients() {
        return shardSetClients;
    }

    public DB getTimeDB() {
        return timeDB;
    }


    public Notice getNotice() {
        return notice;
    }

    public String getDatabase() {
        return mongoProperties.getDatabase();
    }

    public String[] getIgnoreCollections() {
        if (null == oopSearchSyncMongoProperties.getIgnoreCollectionAttr()) {
            return null;
        }
        return oopSearchSyncMongoProperties.getIgnoreCollectionAttr().clone();
    }
}
