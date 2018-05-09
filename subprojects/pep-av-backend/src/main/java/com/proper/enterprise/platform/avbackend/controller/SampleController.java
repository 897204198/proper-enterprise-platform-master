package com.proper.enterprise.platform.avbackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.proper.enterprise.platform.api.service.IMongoDBService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/avdemo")
public class SampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private IMongoDBService mongoDBService;

    public SampleController() {
        LOGGER.info("------------load SampleController-----------------");

    }

    @RequestMapping(value = "/classes/{collection}", method = RequestMethod.POST)
    @ResponseBody
    // TODO RESTFul 响应处理 response body 外还要有不同的响应码表示状态，参考 BaseController 和 UsersController
    Map<String, Object> createOrQuery(@PathVariable String collection, @RequestBody String objectStr,
            HttpServletRequest request, HttpServletResponse res) {
        String url = request.getRequestURI();
        return handler(collection, null, objectStr, url);
    }

    @RequestMapping(value = "/classes/{collection}/{objectIds}", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> delOrUpdate(@PathVariable String collection, @PathVariable String objectIds,
            @RequestBody String objectStr, HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();
        return handler(collection, objectIds, objectStr, url);
    }

    private Map<String, Object> handler(String collection, String objectId, String objectStr, String url) {
        LOGGER.info("Received payload: {}", objectStr);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(objectStr);
            if (innerMethod(root, "DELETE")) {
                return doDelete(collection, objectId, url);
            } else if (innerMethod(root, "PUT")) {
                return doPut(root, collection, objectId, url);
            } else if (root.has("where") && innerMethod(root, "GET")) {
                return doQuery(root, collection, url);
            } else {
                return doCreate(root, collection);
            }
        } catch (Exception ioe) {
            LOGGER.error("Parse json to object error!", ioe);
        }

        return null;
    }

    private boolean innerMethod(JsonNode node, String method) {
        return node.has("_method") && method.equals(node.get("_method").textValue());
    }

    private Map<String, Object> doDelete(String collection, String objectIds, String url) throws Exception {

        mongoDBService.delete(collection, objectIds);
        return new HashMap<String, Object>();

    }

    private Map<String, Object> doPut(JsonNode root, String collection, String objectId, String url) throws Exception {
        // TODO delete 返回个空 map 能说通，put 也返回空 map？
        mongoDBService.updateById(root, collection, objectId);
        return new HashMap<String, Object>();
    }

    private Map<String, Object> doCreate(JsonNode root, String collection) throws Exception {
        Document doc = mongoDBService.insertOne(root, collection);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("objectId", doc.getObjectId("_id").toHexString());
        result.put("createdAt", ISO8601Utils.format(new Date(), true));
        return result;
    }

    private Map<String, Object> doQuery(JsonNode root, String collection, String url) throws Exception {
        List<Document> docs = mongoDBService.query(root, collection);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("results", docs);
        return result;
    }

}
