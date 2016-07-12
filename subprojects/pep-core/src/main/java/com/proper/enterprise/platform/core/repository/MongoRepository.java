package com.proper.enterprise.platform.core.repository;

import com.mongodb.*;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoRepository implements MongoDatabase {

    @Autowired
    Mongo mongo;

    private MongoDatabase getMongoDatabase() {
        return ((MongoClient)mongo).getDatabase(ConfCenter.get("mongodb.database"));
    }


    @Override
    public String getName() {
        return getMongoDatabase().getName();
    }

    @Override
    public CodecRegistry getCodecRegistry() {
        return getMongoDatabase().getCodecRegistry();
    }

    @Override
    public ReadPreference getReadPreference() {
        return getMongoDatabase().getReadPreference();
    }

    @Override
    public WriteConcern getWriteConcern() {
        return getMongoDatabase().getWriteConcern();
    }

    @Override
    public ReadConcern getReadConcern() {
        return getMongoDatabase().getReadConcern();
    }

    @Override
    public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
        return getMongoDatabase().withCodecRegistry(codecRegistry);
    }

    @Override
    public MongoDatabase withReadPreference(ReadPreference readPreference) {
        return getMongoDatabase().withReadPreference(readPreference);
    }

    @Override
    public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
        return getMongoDatabase().withWriteConcern(writeConcern);
    }

    @Override
    public MongoDatabase withReadConcern(ReadConcern readConcern) {
        return getMongoDatabase().withReadConcern(readConcern);
    }

    @Override
    public MongoCollection<Document> getCollection(String collectionName) {
        return getMongoDatabase().getCollection(collectionName);
    }

    @Override
    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> tDocumentClass) {
        return getMongoDatabase().getCollection(collectionName, tDocumentClass);
    }

    @Override
    public Document runCommand(Bson command) {
        return getMongoDatabase().runCommand(command);
    }

    @Override
    public Document runCommand(Bson command, ReadPreference readPreference) {
        return getMongoDatabase().runCommand(command, readPreference);
    }

    @Override
    public <T> T runCommand(Bson command, Class<T> tResultClass) {
        return getMongoDatabase().runCommand(command, tResultClass);
    }

    @Override
    public <T> T runCommand(Bson command, ReadPreference readPreference, Class<T> tResultClass) {
        return getMongoDatabase().runCommand(command, readPreference, tResultClass);
    }

    @Override
    public void drop() {
        getMongoDatabase().drop();
    }

    @Override
    public MongoIterable<String> listCollectionNames() {
        return getMongoDatabase().listCollectionNames();
    }

    @Override
    public ListCollectionsIterable<Document> listCollections() {
        return getMongoDatabase().listCollections();
    }

    @Override
    public <T> ListCollectionsIterable<T> listCollections(Class<T> tResultClass) {
        return getMongoDatabase().listCollections(tResultClass);
    }

    @Override
    public void createCollection(String collectionName) {
        getMongoDatabase().createCollection(collectionName);
    }

    @Override
    public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
        getMongoDatabase().createCollection(collectionName, createCollectionOptions);
    }

}
