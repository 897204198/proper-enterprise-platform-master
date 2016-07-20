package com.proper.enterprise.platform.auth.common.mongodao;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Repository
public class MongoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDAO.class);

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    WebApplicationContext wac;

    public MongoDAO() {

    }

    private String[] getDataRestrainSql(String collection, RequestMethod method, String url) throws Exception {
        ArrayList<String> sqllist = new ArrayList<String>();
        String sql = null;
        String[] sqls = null;
        Resource resource = resourceService.get(url, method);
        if (resource != null) {
            Collection<DataRestrain> dataRestrains = resource.getDataRestrains(collection);
            Iterator<DataRestrain> it = dataRestrains.iterator();
            while (it.hasNext()) {
                DataRestrain dataRestrain = it.next();
                if (dataRestrain != null) {
                    sql = dataRestrain.getSql();
                    if (sql != null) {
                        ExpressionParser parser = new SpelExpressionParser();
                        StandardEvaluationContext context = new StandardEvaluationContext();
                        context.setBeanResolver(new BeanFactoryResolver(wac));
                        Object val = parser.parseExpression(sql, new TemplateParserContext()).getValue(context);
                        sqllist.add(val.toString());
                    }
                }
            }
        }
        if (sqllist != null && sqllist.size() > 0) {
            sqls = new String[sqllist.size()];
            sqls = sqllist.toArray(sqls);
        }
        return sqls;
    }

    private String getUrl() throws URISyntaxException {
        URI uri = new URI(RequestUtil.getCurrentRequest().getRequestURI());
        return uri.getPath();
    }

    public Document insertOne(JsonNode root, String collection) {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Document doc = Document.parse(root.toString());
        col.insertOne(doc);
        return doc;
    }

    public int deleteByIdAndDataRestrain(String collection, String objectIds) throws Exception {
        ArrayList<Bson> list = new ArrayList<Bson>();
        String url = getUrl();
        if (url != null) {
            String[] sql = getDataRestrainSql(collection, RequestMethod.DELETE, url);
            if (sql != null && sql.length > 0) {
                for (int i = 0; i < sql.length; i++) {
                    list.add(Document.parse(sql[i]));
                }
            }
        }
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        int count = 0;

        for (String objectId : objectIds.split(",")) {
            Bson bson = Filters.eq("_id", new ObjectId(objectId));
            ArrayList<Bson> deletelist = (ArrayList<Bson>) list.clone();
            deletelist.add(bson);
            col.findOneAndDelete(Filters.and(deletelist));
        }
        return objectIds.split(",").length;
    }

    public Document deleteById(String collection, String objectIds) throws Exception {
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Document doc = null;
        for (String objectId : objectIds.split(",")) {
            doc = col.findOneAndDelete(Filters.eq("_id", new ObjectId(objectId)));
        }
        return doc;
    }

    public UpdateResult updateByIdAndDataRestrain(JsonNode root, String collection, String objectId) throws Exception {
        List<Bson> querylist = new ArrayList<Bson>();
        String url = getUrl();
        if (url != null) {
            String[] sql = getDataRestrainSql(collection, RequestMethod.PUT, url);
            if (sql != null && sql.length > 0) {
                for (int i = 0; i < sql.length; i++) {
                    querylist.add(Document.parse(sql[i]));
                }
            }
        }
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        Iterator<String> iter = root.fieldNames();
        List<Bson> updatelist = new ArrayList<Bson>();
        while (iter.hasNext()) {
            String field = iter.next();
            if (field.startsWith("_")) {
                continue;
            }
            if (root.get(field).isTextual()) {
                LOGGER.debug("Set {} to {}", field, root.get(field).textValue());
                updatelist.add(Updates.set(field, root.get(field).textValue()));
            } else if (root.get(field).has("__op") && "Delete".equals(root.get(field).get("__op").textValue())) {
                LOGGER.debug("Unset {}", field);
                updatelist.add(Updates.unset(field));
            }
        }
        Bson bson = Filters.eq("_id", new ObjectId(objectId));
        querylist.add(bson);
        return col.updateOne(Filters.and(querylist), Updates.combine(updatelist));
    }

    public List<Document> queryByDataRestrain(JsonNode root, String collection) throws Exception {
        JsonNode whereNode = root.get("where");
        int limit = root.has("limit") ? root.get("limit").asInt() : -1;
        String order = root.has("order") ? root.get("order").asText() : "";
        LOGGER.debug("Where node: {}", whereNode);
        LOGGER.debug("limit is {}", limit);
        LOGGER.debug("orders are {}", order);
        String wherestr = whereNode.toString();
        List<Bson> list = new ArrayList<Bson>();
        String url = getUrl();
        if (url != null) {
            String[] sql = getDataRestrainSql(collection, RequestMethod.GET, url);
            if (sql != null && sql.length > 0) {
                for (int i = 0; i < sql.length; i++) {
                    list.add(Document.parse(sql[i]));
                }
            }
        }
        MongoCollection<Document> col = mongoDatabase.getCollection(collection);

        if (wherestr.indexOf("objectId") > 0) {
            String objectId = whereNode.get("objectId").toString();
            wherestr = wherestr.replaceAll("objectId", "_id");
            wherestr = wherestr.replaceAll(objectId, "ObjectId(" + objectId + ")");
        }
        Document bson = Document.parse(wherestr);
        list.add(bson);
        FindIterable<Document> findIter = col.find(Filters.and(list));

        if (limit > 0) {
            findIter.limit(limit);
        }
        if (!StringUtils.isEmpty(order)) {
            String[] orders = order.split(",");
            for (String o : orders) {
                if (o.startsWith("-")) {
                    findIter.sort(Sorts.descending(o.substring(1)));
                } else {
                    findIter.sort(Sorts.ascending(o));
                }
            }
        }
        Iterator<Document> docsIter = findIter.iterator();
        List<Document> docs = new ArrayList<Document>();
        Document doc;
        while (docsIter.hasNext()) {
            doc = docsIter.next();
            doc.put("objectId", doc.getObjectId("_id").toHexString());
            doc.remove("_id");
            docs.add(doc);
        }
        return docs;
    }

    public Document queryById(String id, String collection) throws Exception {

        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
        FindIterable<Document> iterable = col.find(query);
        Iterator<Document> docsIter = iterable.iterator();
        Document doc = null;
        while (docsIter.hasNext()) {
            doc = docsIter.next();
        }
        return doc;
    }

    public void dropTable(String collection) throws Exception {

        MongoCollection<Document> col = mongoDatabase.getCollection(collection);
        col.drop();
    }

}
