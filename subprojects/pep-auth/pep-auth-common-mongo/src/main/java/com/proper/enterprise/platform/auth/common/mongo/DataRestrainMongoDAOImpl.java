package com.proper.enterprise.platform.auth.common.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.SpELParser;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 提供一个按照 mongo shell 语法操作 MongoDB 数据库的实现
 * 本实现中的方法受数据约束限制
 */
@Repository
public class DataRestrainMongoDAOImpl implements MongoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRestrainMongoDAOImpl.class);

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
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.DELETE, collection);
        return col.findOneAndDelete(
            Filters.and(Filters.and(conditions),
                        Filters.eq("_id", new ObjectId(id))));
    }

    /**
     * 根据当前请求的 URL 及操作对应的 HTTP 方法获得对某个集合操作的数据约束
     * 并将数据约束语句解析为 MongoDB 的查询条件
     *
     * @param method        RESTFul 操作对应的 HTTP 方法
     * @param collection    集合名称
     * @return 查询条件集合
     * @throws URISyntaxException 解析请求中的 URL 发生异常
     */
    private List<Bson> getDataRestrainConditions(RequestMethod method, String collection) throws URISyntaxException {
        List<String> sqls = getDataRestrainSqls(method, collection);
        List<Bson> conditions = new ArrayList<>(sqls.size());
        for (String sql : sqls) {
            if (StringUtil.isNotNull(sql)) {
                conditions.add(Document.parse(sql));
            }
        }
        return conditions;
    }

    // TODO 可以考虑加个方法缓存
    private List<String> getDataRestrainSqls(RequestMethod method, String collection) throws URISyntaxException {
        Resource resource = resourceService.get(getUrl(), method);
        if (resource == null) {
            return Collections.emptyList();
        }
        Collection<DataRestrain> dataRestrains = resource.getDataRestrains(collection);
        List<String> sqls = new ArrayList<>(dataRestrains.size());
        String sql;
        for (DataRestrain dataRestrain : dataRestrains) {
            sql = dataRestrain.getSqlStr();
            LOGGER.debug("{} restrain on {}:{} with {}",
                dataRestrain.getName(), resource.getMethod(), resource.getURL(), sql);
            if (StringUtil.isNotNull(sql)) {
                // 数据约束 sql 中可以使用 SpEL 表达式
                // 在真正使用前需要将其解析成具体的值
                sqls.add(parser.parse(sql));
            }
        }
        return sqls;
    }

    private String getUrl() throws URISyntaxException {
        URI uri = new URI(RequestUtil.getCurrentRequest().getRequestURI());
        return uri.getPath();
    }

    @Override
    public List<Document> deleteByIds(String collection, String[] ids) throws URISyntaxException {
        List<Document> result = new ArrayList<>();
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.DELETE, collection);
        Document document;
        for (String objectId : ids) {
            document = col.findOneAndDelete(
                Filters.and(Filters.and(conditions),
                            Filters.eq("_id", new ObjectId(objectId))));
            if (document != null) {
                result.add(document);
            }
        }
        return result;
    }

    @Override
    public Document updateById(String collection, String id, String update) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.PUT, collection);
        return col.findOneAndUpdate(
            Filters.and(Filters.and(conditions),
                        Filters.eq("_id", new ObjectId(id))),
            Document.parse(update));
    }

    @Override
    public Document queryById(String collection, String id) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.GET, collection);
        return col.find(Filters.and(Filters.and(conditions),
                                    Filters.eq("_id", new ObjectId(id))))
                  .first();
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
    public List<Document> query(String collection, String query, int limit, String sort) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.GET, collection);
        FindIterable<Document> findIter = col.find();

        if (!conditions.isEmpty()) {
            findIter.filter(Filters.and(conditions));
        }
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
    public long count(String collection, String query) throws URISyntaxException {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        List<Bson> conditions = getDataRestrainConditions(RequestMethod.GET, collection);
        if (StringUtil.isNotNull(query)) {
            conditions.add(Document.parse(query));
        }
        return col.count(Filters.and(conditions));
    }

    @Override
    public void drop(String collection) {
        mongoDatabase.getCollection(collection).drop();
    }

}
