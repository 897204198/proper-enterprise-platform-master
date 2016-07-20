package com.proper.enterprise.platform.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.result.UpdateResult;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.service.IMongoDBService;
import com.proper.enterprise.platform.auth.common.mongodao.MongoDAO;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDBServiceImpl implements IMongoDBService {


    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    MongoDAO basicdmo;

    public MongoDBServiceImpl() {

    }

    // --------------------------------------增方法--------------------------------------
    @Override
    public Document insertOne(JsonNode root, String collection) {
        // TODO Auto-generated method stub
        return basicdmo.insertOne(root, collection);
    }

    // --------------------------------------删方法--------------------------------------

    @Override
    public int delete(String collection, String objectIds) throws Exception {
        // TODO Auto-generated method stub
        return basicdmo.deleteByIdAndDataRestrain(collection, objectIds);

    }

    public Document deleteById(String collection, String objectIds) throws Exception {
        // TODO Auto-generated method stub
        return basicdmo.deleteById(collection, objectIds);
    }

    // --------------------------------------改方法--------------------------------------

    @Override
    public UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception {
        return basicdmo.updateByIdAndDataRestrain(root, collection, objectId);
    }

    // --------------------------------------查方法--------------------------------------

    @Override
    public List<Document> query(JsonNode root, String collection) throws Exception {
        return basicdmo.queryByDataRestrain(root, collection);
    }

    @Override
    public Document queryById(String id, String collection) throws Exception {
        return basicdmo.queryById(id, collection);
    }

    @Override
    public void droptable(String collection) throws Exception {
        // TODO Auto-generated method stub
        basicdmo.dropTable(collection);
    }

}
