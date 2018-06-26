package com.proper.enterprise.platform.core.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.proper.enterprise.platform.core.mongo.client.PEPMongoClient;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.mongo.service.impl.MongoShellServiceImpl;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackages = "com.proper.**.repository")
public class MongoConfigs extends AbstractMongoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfigs.class);

    private static final String COMMA =  ",";

    private static final String MATCH =  "^.+:\\d+(,.+:\\d+)*";

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
    @Value("${mongodb.replicaSet}")
    private String replicaSet;

    @Autowired
    @Qualifier("mongoDAOImpl")
    private MongoDAO mongoDAO;

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
    public PEPMongoClient mongoClient() {
        List<MongoCredential> credentialList = Collections.emptyList();
        if (StringUtil.isNotNull(username)) {
            credentialList = singletonList(MongoCredential.createCredential(username, "admin", password.toCharArray()));
        }
        List<ServerAddress> serverAddresses = new ArrayList<>();
        if (StringUtil.isNotNull(replicaSet) && replicaSet.matches(MATCH)) {
            String[] address;
            for (String pair : replicaSet.split(COMMA)) {
                address = pair.split(":");
                serverAddresses.add(new ServerAddress(address[0], Integer.parseInt(address[1])));
            }
        } else {
            LOGGER.debug("Replica set not setting or invalid, use direct connection.");
            serverAddresses = singletonList(new ServerAddress(host, port));
        }
        return new PEPMongoClient(serverAddresses, credentialList);
    }

    @Bean
    public MongoDatabase mongoDatabase() throws Exception {
        MongoDatabaseFactoryBean factoryBean = new MongoDatabaseFactoryBean();
        factoryBean.setDatabaseName(databaseName);
        factoryBean.setMongoClient(mongoClient());
        return factoryBean.createInstance();
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoDbFactory mongoDbFactory = mongoDbFactory();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        //设置mongodb不生成_class 字段
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory, converter);
    }

    @Bean
    public MongoShellService mongoShellService() {
        MongoShellServiceImpl instance = new MongoShellServiceImpl();
        instance.setMongoDAO(mongoDAO);
        return instance;
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

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }
}
