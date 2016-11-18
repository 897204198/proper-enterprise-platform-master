package com.proper.enterprise.platform.core.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackages = "com.proper.**.repository")
public class MongoConfigs extends AbstractMongoConfiguration {

    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private int port;
    @Value("${mongodb.database}")
    private String databaseName;
    @Value("${mongodb.username}")
    private String username;
    @Value("${mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return mongoClient();
    }

    @Bean
    public MongoClient mongoClient() {
        List<MongoCredential> credentialList = Collections.emptyList();
        if (StringUtil.isNotNull(username)) {
            credentialList = singletonList(MongoCredential.createCredential(username, "admin", password.toCharArray()));
        }
        return new MongoClient(singletonList(new ServerAddress(host, port)), credentialList);
    }

    @Bean
    public MongoDatabase mongoDatabase() throws Exception {
        MongoDatabaseFactoryBean factoryBean = new MongoDatabaseFactoryBean();
        factoryBean.setDatabaseName(databaseName);
        factoryBean.setMongoClient(mongoClient());
        return factoryBean.createInstance();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
