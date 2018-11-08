package com.proper.enterprise.platform.core.mongo.client;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class PEPMongoClient extends MongoClient {
    @Value("${mongodb.database}")
    private String databaseName;

    public PEPMongoClient(final List<ServerAddress> seeds, final List<MongoCredential> credentialsList) {
        super(seeds, credentialsList, new MongoClientOptions.Builder().build());
    }

}
