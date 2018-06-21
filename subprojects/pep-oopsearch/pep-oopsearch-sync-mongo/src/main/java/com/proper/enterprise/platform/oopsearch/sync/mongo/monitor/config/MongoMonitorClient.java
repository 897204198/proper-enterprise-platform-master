package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config;

import com.mongodb.*;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.BSONTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.Collections.singletonList;

@Component
public class MongoMonitorClient {

    private MongoMonitorClient() {
    }

    public static final String TIME_DB_NAME = "monitor_time_d";

    private MongoClient hostMongo;
    private Map<String, MongoClient> shardSetClients;
    private DB timeDB;
    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private String port;
    @Value("${mongodb.database}")
    private String database;
    @Value("${mongodb.username}")
    private String userName;
    @Value("${mongodb.password}")
    private String password;

    @Autowired
    @Qualifier(value = "mongoSyncMongoNotice")
    private Notice notice;

    @Value("${mongodb.sync.ignoreCollections}")
    private String[] ignoreCollections;

    @PostConstruct
    void persist() {
        initConfig();
    }

    public void resetSyncTime() {
        Set<String> shardTimeNames = this.getTimeDB().getCollectionNames();
        BSONTimestamp lastTimeStamp = new BSONTimestamp(getSecondTimestampTwo(new Date()), 1);
        for (String shardTime : shardTimeNames) {
            this.getTimeDB().getCollection(shardTime).update(new BasicDBObject(), new BasicDBObject("$set", new BasicDBObject("ts",
                lastTimeStamp)), true, true, WriteConcern.SAFE);
        }
    }

    private void initConfig() {
        initClient();
        initShardSetClients();
        initTimeDB();
    }

    private void initClient() {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : host.split(",")) {
            serverAddresses.add(new ServerAddress(address + ":" + port));
        }
        this.hostMongo = new MongoClient(serverAddresses, getMongoCredential(userName, password));
    }

    private List<MongoCredential> getMongoCredential(String userName, String password) {
        List<MongoCredential> credentialList = Collections.emptyList();
        if (StringUtils.isNotBlank(userName)) {
            credentialList = singletonList(MongoCredential.createCredential(userName,
                "admin", password.toCharArray()));
        }
        return credentialList;
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
            Map<String, MongoClient> shardSets = new HashMap<>();
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
            return new MongoClient(shardSet, getMongoCredential(userName, password), opts);
        }

        private List<ServerAddress> buildServerAddressList(DBObject next) {
            List<ServerAddress> hosts = new ArrayList<>();
            for (String host : Arrays
                .asList(((String) next.get("host")).split("/")[1].split(","))) {
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
        return database;
    }

    public String[] getIgnoreCollections() {
        if (null == ignoreCollections) {
            return null;
        }
        return ignoreCollections.clone();
    }
}
