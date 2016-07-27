package com.proper.enterprise.platform.auth.common.repository.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.repository.MongoDAO;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.SpELParser;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Repository
public class MongoDAOImpl implements MongoDAO {

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    ResourceService resourceService;

    @Autowired
    SpELParser parser;

    @Override
    public Document insertOne(String collection, String document) {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Document doc = Document.parse(document);
        col.insertOne(doc);
        return doc;
    }

    @Override
    public Document deleteById(String collection, String id) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(collection);
        return col.findOneAndDelete(
            Filters.and(Filters.and(conditions),
                        Filters.eq("_id", new ObjectId(id))));
    }

    // TODO
    private List<Bson> getDataRestrainConditions(String collection) throws URISyntaxException {
        List<String> sqls = getDataRestrainSqls(getUrl(), getMethod(), collection);
        List<Bson> conditions = new ArrayList<>(sqls.size());
        for (String sql : sqls) {
            if (StringUtil.isNotNull(sql)) {
                conditions.add(Document.parse(sql));
            }
        }
        return conditions;
    }

    // TODO 可以考虑加个方法缓存
    private List<String> getDataRestrainSqls(String url, RequestMethod method, String collection) {
        Resource resource = resourceService.get(url, method);
        if (resource == null) {
            return Collections.emptyList();
        }
        Collection<DataRestrain> dataRestrains = resource.getDataRestrains(collection);
        List<String> sqls = new ArrayList<>(dataRestrains.size());
        String sql;
        for (DataRestrain dataRestrain : dataRestrains) {
            sql = dataRestrain.getSql();
            if (StringUtil.isNotNull(sql)) {
                sqls.add(parser.parse(sql));
            }
        }
        return sqls;
    }

    private String getUrl() throws URISyntaxException {
        URI uri = new URI(RequestUtil.getCurrentRequest().getRequestURI());
        return uri.getPath();
    }

    private RequestMethod getMethod() {
        return RequestMethod.valueOf(RequestUtil.getCurrentRequest().getMethod());
    }

    @Override
    public List<Document> deleteByIds(String collection, String[] ids) throws URISyntaxException {
        List<Document> result = new ArrayList<>(ids.length);
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(collection);
        Document document;
        for (String objectId : ids) {
            document = col.findOneAndDelete(
                Filters.and(Filters.and(conditions),
                            Filters.eq("_id", new ObjectId(objectId))));
            result.add(document);
        }
        return result;
    }

    @Override
    public Document updateById(String collection, String id, String update) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(collection);
        return col.findOneAndUpdate(
            Filters.and(Filters.and(conditions),
                        Filters.eq("_id", new ObjectId(id))),
            Document.parse(update));
    }

    @Override
    public Document queryById(String collection, String id) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(collection);
        return col.find(Filters.and(Filters.and(conditions),
                                    Filters.eq("_id", new ObjectId(id))))
                  .first();
    }

    @Override
    public List<Document> query(String collection, String query, int limit, String sort) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(collection);
        FindIterable<Document> findIter = col.find(Filters.and(Filters.and(conditions), Document.parse(query)));

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

}
