package com.proper.enterprise.platform.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.result.UpdateResult;
import com.proper.enterprise.platform.api.service.IMongoDBService;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class MongoDBServiceImpl implements IMongoDBService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBServiceImpl.class);

    @Autowired
    MongoDAO mongoDAO;

    // --------------------------------------增方法--------------------------------------
    @Override
    public Document insertOne(JsonNode root, String collection) {
        return mongoDAO.insertOne(collection, root.toString());
    }

    // --------------------------------------删方法--------------------------------------

    @Override
    public int delete(String collection, String objectIds) throws Exception {
        // TODO Auto-generated method stub
        return mongoDAO.deleteByIds(collection, objectIds.split(",")).size();

    }

    public Document deleteById(String collection, String objectIds) throws Exception {
        return mongoDAO.deleteById(collection, objectIds);
    }

    // --------------------------------------改方法--------------------------------------

    @Override
    public UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception {
        Iterator<String> iter = root.fieldNames();
        Map<String, Object> setMap = new HashMap<>();
        Map<String, Object> unsetMap = new HashMap<>();
        while (iter.hasNext()) {
            String field = iter.next();
            if (field.startsWith("_")) {
                continue;
            }
            if (root.get(field).isNumber()) {
                setMap.put(field, root.get(field).numberValue());
            }
            if (root.get(field).isBoolean()) {
                setMap.put(field, root.get(field).booleanValue());
            }
            if (root.get(field).isTextual()) {
                LOGGER.debug("Set {} to {}", field, root.get(field).textValue());
                setMap.put(field, root.get(field).textValue());
            } else if (root.get(field).has("__op") && "Delete".equals(root.get(field).get("__op").textValue())) {
                LOGGER.debug("Unset {}", field);
                unsetMap.put(field, "");
            }
        }
        String[] query = new String[2];
        if (!setMap.isEmpty()) {
            query[0] = "$set:" + JSONUtil.toJSONIgnoreException(setMap);
        }
        if (!unsetMap.isEmpty()) {
            query[1] = "$unset:" + JSONUtil.toJSONIgnoreException(unsetMap);
        }
        // TODO
        mongoDAO.updateById(collection, objectId, "{" + StringUtil.join(query, ",") + "}");
        return null;
    }

    // --------------------------------------查方法--------------------------------------

    @Override
    public List<Document> query(JsonNode root, String collection) throws Exception {
        JsonNode whereNode = root.get("where");
        int limit = root.has("limit") ? root.get("limit").asInt() : -1;
        String order = root.has("order") ? root.get("order").asText() : "";
        LOGGER.debug("Where node: {}", whereNode);
        LOGGER.debug("limit is {}", limit);
        LOGGER.debug("orders are {}", order);
        int skip = root.has("skip") ? root.get("skip").asInt() : 0;
        String wherestr = whereNode.toString();
        if (wherestr.indexOf("objectId") > 0) {
            String objectId = whereNode.get("objectId").toString();
            wherestr = wherestr.replaceAll("objectId", "_id");
            wherestr = wherestr.replaceAll(objectId, "ObjectId(" + objectId + ")");
        }
        Map<String, Integer> sort = new HashMap<>();
        if (StringUtil.isNotNull(order)) {
            String[] orders = order.split(",");
            for (String o : orders) {
                if (o.startsWith("-")) {
                    sort.put(o.substring(1), -1);
                } else {
                    sort.put(o, 1);
                }
            }
        }

        return mongoDAO.query(collection, wherestr, skip, limit, JSONUtil.toJSON(sort));
    }

    @Override
    public long count(JsonNode root, String collection) throws Exception {
        JsonNode whereNode = root.get("where");
        String wherestr = whereNode.toString();
        if (wherestr.indexOf("objectId") > 0) {
            String objectId = whereNode.get("objectId").toString();
            wherestr = wherestr.replaceAll("objectId", "_id");
            wherestr = wherestr.replaceAll(objectId, "ObjectId(" + objectId + ")");
        }
        return mongoDAO.count(collection, wherestr);
    }

    @Override
    public Document queryById(String id, String collection) throws Exception {
        return mongoDAO.queryById(collection, id);
    }

    @Override
    public void droptable(String collection) throws Exception {
        mongoDAO.drop(collection);
    }

}
