package com.proper.enterprise.platform.core.mongo.dao.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MongoDAOImpl implements MongoDAO {

    @Autowired
    MongoDatabase mongoDatabase;

    @Override
    public Document insertOne(String collection, String document) {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Document doc = Document.parse(document);
        col.insertOne(doc);
        return doc;
    }

    @Override
    public Document deleteById(String collection, String id) throws Exception {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        return col.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
    }

    @Override
    public List<Document> deleteByIds(String collection, String[] ids) throws Exception {
        List<Document> result = new ArrayList<>();
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Document document;
        for (String objectId : ids) {
            document = col.findOneAndDelete(Filters.eq("_id", new ObjectId(objectId)));
            if (document != null) {
                result.add(document);
            }
        }
        return result;
    }

    @Override
    public Document updateById(String collection, String id, String update) throws Exception {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        return col.findOneAndUpdate(Filters.eq("_id", new ObjectId(id)),
            Document.parse(update));
    }

    @Override
    public Document queryById(String collection, String id) throws Exception {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        return col.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    @Override
    public List<Document> query(String collection, String query) throws Exception {
        return query(collection, query, 0, null);
    }

    @Override
    public List<Document> query(String collection, String query, int limit) throws Exception {
        return query(collection, query, limit, null);
    }

    @Override
    public List<Document> query(String collection, String query, String sort) throws Exception {
        return query(collection, query, 0, sort);
    }

    @Override
    public List<Document> query(String collection, String query, int limit, String sort) throws Exception {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        FindIterable<Document> findIter = col.find();

        if (StringUtil.isNotNull(query)) {
            findIter.filter(Document.parse(query));
        }
        if (limit > 0) {
            findIter.limit(limit);
        }
        if (StringUtil.isNotNull(sort)) {
            findIter.sort(Document.parse(sort));
        }

        List<Document> result = new ArrayList<>();
        for (Document document : findIter) {
            result.add(document);
        }
        return result;
    }

    @Override
    public void drop(String collection) {
        mongoDatabase.getCollection(collection).drop();
    }

}
