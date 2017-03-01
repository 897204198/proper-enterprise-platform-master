package com.proper.enterprise.platform.core.mongo.logback;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proper.enterprise.platform.core.mongo.MongoConfigs;
import org.bson.Document;

public class MongoDBConnectionSource {

    private volatile String host;

    private volatile int port;

    private volatile String database;

    private volatile String username;

    private volatile String password;

    private volatile String collection;

    private volatile MongoCollection<Document> mongoCollection;

    public MongoCollection<Document> getMongoCollection() {
        if (mongoCollection == null) {
            synchronized (this) {
                // DCL
                if (mongoCollection == null) {
                    MongoConfigs configs = new MongoConfigs();
                    configs.setHost(host);
                    configs.setPort(port);
                    configs.setDatabaseName(database);
                    configs.setUsername(username);
                    configs.setPassword(password);

                    final MongoClient mongoClient = configs.mongoClient();
                    MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
                    mongoCollection = mongoDatabase.getCollection(collection);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mongoClient.close();
                        }
                    }, "MongoDBConnectionSource shutdown"));
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
}
