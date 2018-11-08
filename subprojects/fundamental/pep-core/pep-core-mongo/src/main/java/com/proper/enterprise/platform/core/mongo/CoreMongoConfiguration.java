package com.proper.enterprise.platform.core.mongo;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.mongo.jackson.DocumentJsonSerializer;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.mongo.service.impl.MongoShellServiceImpl;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackages = "com.proper.**.repository")
public class CoreMongoConfiguration extends AbstractMongoConfiguration {

    private MongoProperties mongoProperties;

    private CoreMongoProperties coreMongoProperties;

    @Autowired
    public CoreMongoConfiguration(MongoProperties mongoProperties, CoreMongoProperties coreMongoProperties) {
        this.mongoProperties = mongoProperties;
        this.coreMongoProperties = coreMongoProperties;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreMongoConfiguration.class);

    private static final String COMMA = ",";

    /**
     * pattern of 'host1:port1,host2:port2,host3:port3'
     */
    private static final String MATCH = "^.+:\\d+(,.+:\\d+)*";

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        if (StringUtil.isNotNull(coreMongoProperties.getReplicaSet()) && coreMongoProperties.getReplicaSet().matches(MATCH)) {
            String[] address;
            for (String pair : coreMongoProperties.getReplicaSet().split(COMMA)) {
                address = pair.split(":");
                serverAddresses.add(new ServerAddress(address[0], Integer.parseInt(address[1])));
            }
        } else {
            LOGGER.debug("Replica set not setting or invalid, use direct connection.");
            serverAddresses = singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()));
        }
        if (StringUtil.isNotNull(mongoProperties.getUsername())) {
            return new MongoClient(serverAddresses, MongoCredential.createCredential(mongoProperties.getUsername(),
                "admin", mongoProperties.getPassword()), new MongoClientOptions.Builder().build());
        }
        return new MongoClient(serverAddresses, new MongoClientOptions.Builder().build());
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(mongoProperties.getDatabase());
    }

    @Bean
    @Override
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }

    @Bean
    @Override
    public MongoTemplate mongoTemplate() {
        MongoDbFactory mongoDbFactory = mongoDbFactory();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        //设置mongodb不生成_class 字段
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory, converter);
    }

    @Bean
    public MongoShellService mongoShellService(@Qualifier("mongoDAOImpl") MongoDAO mongoDAO) {
        MongoShellServiceImpl instance = new MongoShellServiceImpl();
        instance.setMongoDAO(mongoDAO);
        return instance;
    }

    /**
     * 重写ObjecgtMapper  用于序列化ObjectId
     * @return 覆盖 objectMapper
     */
    @Primary
    @Bean
    public ObjectMapper documentObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        Map<Class<?>, JsonSerializer<?>> jsonSerializerMap = new LinkedHashMap<>();
        jsonSerializerMap.put(Document.class, new DocumentJsonSerializer());
        builder.serializersByType(jsonSerializerMap);
        return builder.build();
    }

}
