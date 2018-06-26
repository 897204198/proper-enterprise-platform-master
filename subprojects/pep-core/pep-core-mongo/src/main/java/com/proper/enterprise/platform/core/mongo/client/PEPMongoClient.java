package com.proper.enterprise.platform.core.mongo.client;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.util.List;

public class PEPMongoClient extends MongoClient {
    @Value("${mongodb.database}")
    private String databaseName;
    @Value("${mongodb.destory}")
    private String destory;

    private static final String DROP = "drop";

    public PEPMongoClient(final List<ServerAddress> seeds, final List<MongoCredential> credentialsList) {
        super(seeds, credentialsList, new MongoClientOptions.Builder().build());
    }

    @PreDestroy
    public void preDestory() {
        if (StringUtil.isNotEmpty(destory) && DROP.equals(destory)) {
            this.dropDatabase(databaseName);
        }
    }
}
