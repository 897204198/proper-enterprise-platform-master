package com.proper.enterprise.platform.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.List;

public interface IMongoDBService {


    Document insertOne(JsonNode root, String collection) throws Exception;

    int delete(String collection, String objectIds) throws Exception;

    Document deleteById(String collection, String objectIds) throws Exception;

    UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception;

    List<Document> query(JsonNode root, String collection) throws Exception;

    Document queryById(String id, String collection) throws Exception;

    void droptable(String collection) throws Exception;

}
