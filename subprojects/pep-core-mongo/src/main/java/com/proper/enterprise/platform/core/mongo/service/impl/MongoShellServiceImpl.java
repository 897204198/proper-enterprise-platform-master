package com.proper.enterprise.platform.core.mongo.service.impl;

import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import org.bson.Document;

import java.util.List;

public class MongoShellServiceImpl implements MongoShellService {

    private MongoDAO mongoDAO;

    public void setMongoDAO(MongoDAO mongoDAO) {
        this.mongoDAO = mongoDAO;
    }

    @Override
    public Document insertOne(String collection, String document) {
        return mongoDAO.insertOne(collection, document);
    }

    @Override
    public Document deleteById(String collection, String id) throws Exception {
        return mongoDAO.deleteById(collection, id);
    }

    @Override
    public List<Document> deleteByIds(String collection, String[] ids) throws Exception {
        return mongoDAO.deleteByIds(collection, ids);
    }

    @Override
    public Document updateById(String collection, String id, String update) throws Exception {
        return mongoDAO.updateById(collection, id, update);
    }

    @Override
    public Document queryById(String collection, String id) throws Exception {
        return mongoDAO.queryById(collection, id);
    }

    @Override
    public List<Document> query(String collection, String query) throws Exception {
        return mongoDAO.query(collection, query);
    }

    @Override
    public List<Document> query(String collection, String query, int limit) throws Exception {
        return mongoDAO.query(collection, query, limit);
    }

    @Override
    public List<Document> query(String collection, String query, String sort) throws Exception {
        return mongoDAO.query(collection, query, sort);
    }

    @Override
    public List<Document> query(String collection, String query, int limit, String sort) throws Exception {
        return mongoDAO.query(collection, query, limit, sort);
    }

    @Override
    public void drop(String collection) {
        mongoDAO.drop(collection);
    }

}
