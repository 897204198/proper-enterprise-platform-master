package hello;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Controller

@RequestMapping("/avdemo")
public class SampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    private MongoDatabase database;

    private String userTable = "user";

    // @Autowired
    public SampleController() {
    }

    @RequestMapping(value = "/classes/{collection}", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> createOrQuery(@PathVariable String collection, @RequestBody String objectStr,
            HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        return handler(collection, null, objectStr);
    }

    @RequestMapping(value = "/classes/{collection}/{objectIds}", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> delOrUpdate(@PathVariable String collection, @PathVariable String objectIds,
            @RequestBody String objectStr, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        return handler(collection, objectIds, objectStr);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> user(@RequestBody String userStr, HttpServletRequest request, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        return userHandler("users", userStr);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> userLogin(@RequestBody String userStr, HttpServletRequest request, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        userHandler("login", userStr);
        return null;
    }

    private Map<String, Object> handler(String collection, String objectId, String objectStr) {
        // LOGGER.info("Collection name is {}", collection);
        LOGGER.info("Received payload: {}", objectStr);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(objectStr);
            if (innerMethod(root, "DELETE")) {
                return doDelete(collection, objectId);
            } else if (innerMethod(root, "PUT")) {
                return doPut(root, collection, objectId);
            } else if (root.has("where") && innerMethod(root, "GET")) {
                return doQuery(root, collection);
            } else {
                return doCreate(root, collection);
            }
        } catch (IOException ioe) {
            LOGGER.error("Parse json to object error!", ioe);
        }

        return null;
    }

    private Map<String, Object> userHandler(String collection, String objectStr) {
        // LOGGER.info("Collection name is {}", collection);
        if (collection.equals("users")) {
            return users(objectStr);

        } else if (collection.equals("login")) {
            return userLogin(objectStr);
        }

        return null;
    }

    private boolean innerMethod(JsonNode node, String method) {
        return node.has("_method") && method.equals(node.get("_method").textValue());
    }

    private Map<String, Object> doDelete(String collection, String objectIds) {
        LOGGER.info("In do DELETE method");
        MongoCollection<Document> col = getDatabase().getCollection(collection);
        for (String objectId : objectIds.split(",")) {
            col.findOneAndDelete(eq("_id", new ObjectId(objectId)));
        }
        return new HashMap<String, Object>();
    }

    private Map<String, Object> doPut(JsonNode root, String collection, String objectId) {
        LOGGER.info("In do PUT method");
        MongoCollection<Document> col = getDatabase().getCollection(collection);
        Iterator<String> iter = root.fieldNames();
        List<Bson> list = new ArrayList<Bson>();
        while (iter.hasNext()) {
            String field = iter.next();
            if (field.startsWith("_")) {
                continue;
            }
            if (root.get(field).isTextual()) {
                LOGGER.info("Set {} to {}", field, root.get(field).textValue());
                list.add(set(field, root.get(field).textValue()));
            } else if (root.get(field).has("__op") && "Delete".equals(root.get(field).get("__op").textValue())) {
                LOGGER.info("Unset {}", field);
                list.add(unset(field));
            }
        }
        col.updateOne(eq("_id", new ObjectId(objectId)), combine(list));
        return new HashMap<String, Object>();
    }

    private Map<String, Object> doCreate(JsonNode root, String collection) {
        Map<String, Object> result = new HashMap<String, Object>();
        MongoCollection<Document> col = getDatabase().getCollection(collection);

        Document doc = new Document();
        doc = doc.parse(root.toString());
        col.insertOne(doc);

        result.put("objectId", doc.getObjectId("_id").toHexString());
        result.put("createdAt", ISO8601Utils.format(new Date(), true));
        return result;
    }

    private Map<String, Object> doQuery(JsonNode root, String collection) {
        Map<String, Object> result = new HashMap<String, Object>();
        JsonNode whereNode = root.get("where");
        int limit = root.has("limit") ? root.get("limit").asInt() : -1;
        String order = root.has("order") ? root.get("order").asText() : "";
        LOGGER.info("Where node: {}", whereNode);
        LOGGER.info("limit is {}", limit);
        LOGGER.info("orders are {}", order);

        MongoCollection<Document> col = getDatabase().getCollection(collection);
        String wherestr = whereNode.toString();
        if (wherestr.indexOf("objectId") > 0) {
            String objectId = whereNode.get("objectId").toString();
            wherestr = wherestr.replaceAll("objectId", "_id");
            wherestr = wherestr.replaceAll(objectId, "ObjectId(" + objectId + ")");
        }
        FindIterable<Document> findIter = col.find(Document.parse(wherestr));

        if (limit > 0) {
            findIter.limit(limit);
        }
        if (!StringUtils.isEmpty(order)) {
            String[] orders = order.split(",");
            for (String o : orders) {
                if (o.startsWith("-")) {
                    findIter.sort(descending(o.substring(1)));
                } else {
                    findIter.sort(ascending(o));
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
        result.put("results", docs);
        return result;
    }

    private Map<String, Object> users(String userstr) {
        LOGGER.info("In do users method");
        Map<String, Object> result = new HashMap<String, Object>();
        MongoCollection<Document> col = getDatabase().getCollection(userTable);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(userstr);
            Document doc = new Document();
            doc.put("username", root.get("username").textValue());
            doc.put("password", root.get("password").textValue());
            doc.put("email", root.get("email").textValue());
            col.insertOne(doc);
            result.put("results", doc);
            result.put("objectId", doc.getObjectId("_id").toHexString());
            result.put("createdAt", ISO8601Utils.format(new Date(), true));
            return result;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("Parse json to object error!", e);
        }
        return new HashMap<String, Object>();
    }

    private Map<String, Object> userLogin(String userstr) {
        LOGGER.info("In do userLogin method");
        return new HashMap<String, Object>();
    }

    private MongoDatabase getDatabase() {
        try {
            if (database != null) {
                return database;
            }

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            Properties p = new Properties();
            p.load(in);
            MongoClient mongoClient = new MongoClient(p.getProperty("mongodb.host"),
                    Integer.parseInt(p.getProperty("mongodb.port")));
            database = mongoClient.getDatabase(p.getProperty("mongodb.database"));

        } catch (Exception e) {
            // TODO: handle exception
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }

        return database;

    }
}