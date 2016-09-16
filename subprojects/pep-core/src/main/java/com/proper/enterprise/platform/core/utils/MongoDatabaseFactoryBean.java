package com.proper.enterprise.platform.core.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * MongoDatabase 工厂
 *
 * 根据注入的 MongoClient 和配置文件中定义的 mongodb.database 创建 MongoDatabase 实例
 */
public class MongoDatabaseFactoryBean extends AbstractFactoryBean<MongoDatabase> {

    private MongoClient mongoClient;

    private String databaseName;

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public Class<? extends MongoDatabase> getObjectType() {
        return MongoDatabase.class;
    }

    @Override
    protected MongoDatabase createInstance() throws Exception {
        return mongoClient.getDatabase(databaseName);
    }

}
