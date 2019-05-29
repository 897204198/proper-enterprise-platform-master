package com.proper.enterprise.platform.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.mongodb.client.result.UpdateResult;
import com.proper.enterprise.platform.api.service.MongoDataBaseService;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MongoDataBaseServiceImpl implements MongoDataBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDataBaseServiceImpl.class);

    private static final String OBJECTID = "objectId";

    private static final String FILE_NAME_IN = "$in";

    private static final String ID = "_id";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoDAO mongoDAO;

    @Override
    public Document insertOne(JsonNode root, String collection) {
        return mongoDAO.insertOne(collection, root.toString());
    }

    @Override
    public int delete(String collection, String objectIds) throws Exception {
        // TODO Auto-generated method stub
        return mongoDAO.deleteByIds(collection, objectIds.split(",")).size();

    }

    @Override
    public Document deleteById(String collection, String objectIds) throws Exception {
        return mongoDAO.deleteById(collection, objectIds);
    }

    @Override
    public UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception {
        Iterator<String> iter = root.fieldNames();
        Map<String, Object> setMap = new HashMap<>(4);
        Map<String, Object> unsetMap = new HashMap<>(2);
        while (iter.hasNext()) {
            String field = iter.next();
            if (field.startsWith("_")) {
                continue;
            }
            if (root.get(field).isArray()) {
                setMap.put(field, root.get(field));
            }
            if (root.get(field).isNumber()) {
                setMap.put(field, root.get(field).numberValue());
            }
            if (root.get(field).isBoolean()) {
                setMap.put(field, root.get(field).booleanValue());
            }
            if (root.get(field).isObject()) {
                setMap.put(field, root.get(field));
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

    @Override
    public List<Document> query(JsonNode root, String collection) throws Exception {
        JsonNode whereNode = root.get("where");
        int limit = root.has("limit") ? root.get("limit").asInt() : -1;
        String order = root.has("order") ? root.get("order").asText() : "";
        LOGGER.debug("Where node: {}", whereNode);
        LOGGER.debug("limit is {}", limit);
        LOGGER.debug("orders are {}", order);
        int skip = root.has("skip") ? root.get("skip").asInt() : 0;
        whereNode = handleId(whereNode);
        Map<String, Integer> sort = new LinkedHashMap<>(2);
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
        return mongoDAO.query(collection, whereNode.toString(), skip, limit, JSONUtil.toJSON(sort));
    }

    @Override
    public long count(JsonNode root, String collection) throws Exception {
        JsonNode whereNode = root.get("where");
        String wherestr = whereNode.toString();
        if (wherestr.indexOf(OBJECTID) > 0) {
            String objectId = whereNode.get(OBJECTID).toString();
            wherestr = wherestr.replaceAll(OBJECTID, ID);
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

    private JsonNode handleId(JsonNode whereNode) {
        if (whereNode.path(ID).path(FILE_NAME_IN).isArray()) {
            ArrayNode idCondition = (ArrayNode) whereNode.path(ID).path(FILE_NAME_IN);
            ArrayNode idConditionRep = objectMapper.createArrayNode();
            for (JsonNode node : idCondition) {
                StringBuffer objectIdBuf = new StringBuffer();
                objectIdBuf.append("ObjectId('").append(node.textValue()).append("')");
                idConditionRep.add(new POJONode(objectIdBuf.toString()));
            }
            ObjectNode objectNode = (ObjectNode) whereNode.path(ID);
            objectNode.replace(FILE_NAME_IN, idConditionRep);
        }
        if (whereNode.has(OBJECTID)) {
            StringBuffer objectIdBuf = new StringBuffer();
            objectIdBuf.append("ObjectId('").append(whereNode.get(OBJECTID).textValue()).append("')");
            ObjectNode objectNode = (ObjectNode) whereNode;
            objectNode.remove(OBJECTID);
            objectNode.putPOJO(ID, new POJONode(objectIdBuf));
        }
        return whereNode;
    }

}
