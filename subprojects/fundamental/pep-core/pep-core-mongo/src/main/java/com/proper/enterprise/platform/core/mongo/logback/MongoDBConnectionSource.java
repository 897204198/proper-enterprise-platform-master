package com.proper.enterprise.platform.core.mongo.logback;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proper.enterprise.platform.core.mongo.CoreMongoProperties;
import com.proper.enterprise.platform.core.mongo.CoreMongoConfiguration;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

public class MongoDBConnectionSource {

    private volatile String host;

    private volatile int port;

    private volatile String database;

    private volatile String username;

    private volatile String password;

    private volatile String collection;

    private volatile String replica;

    private volatile MongoCollection<Document> mongoCollection;

    public MongoCollection<Document> getMongoCollection() {
        if (mongoCollection == null) {
            synchronized (this) {
                // DCL
                if (mongoCollection == null) {
                    MongoProperties mongoProperties = new MongoProperties();
                    CoreMongoProperties coreMongoProperties = new CoreMongoProperties();
                    if (StringUtil.isNotNull(replica)) {
                        coreMongoProperties.setReplicaSet(replica);
                    } else {
                        mongoProperties.setHost(host);
                        mongoProperties.setPort(port);
                    }
                    mongoProperties.setDatabase(database);
                    mongoProperties.setUsername(username);
                    mongoProperties.setPassword(password.toCharArray());
                    CoreMongoConfiguration coreMongoConfiguration = new CoreMongoConfiguration(mongoProperties, coreMongoProperties);
                    final MongoClient mongoClient = coreMongoConfiguration.mongoClient();
                    MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
                    mongoCollection = mongoDatabase.getCollection(collection);
                    Runtime.getRuntime().addShutdownHook(new Thread(mongoClient::close, "MongoDBConnectionSource shutdown"));
                }
            }
        }
        return mongoCollection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

}
