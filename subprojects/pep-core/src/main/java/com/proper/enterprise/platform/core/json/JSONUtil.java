package com.proper.enterprise.platform.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

public class JSONUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();
    
    private JSONUtil() { }
    
    public static String toJSONString(Object obj) {
        String result = "";
        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            LOGGER.error("Write obj to json error!", jpe);
        }
        return result;
    }
    
    public static Object parse(String str) {
        JsonNode root = null;
        try {
            root = mapper.readTree(str);
        } catch (IOException ioe) {
            LOGGER.error("Parse json to object error!", ioe);
        }
        if (root == null) {
            return null;
        } else if (root instanceof ObjectNode) {
            return new JSONObject((ObjectNode)root);
        } else if (root instanceof ArrayNode) {
            return toJSONObjectArray((ArrayNode)root);
        } else {
            return null;
        }
    }
    
    private static JSONObject[] toJSONObjectArray(ArrayNode array) {
        JSONObject[] objs = new JSONObject[array.size()];
        Iterator<JsonNode> iter = array.elements();
        int i = 0;
        ObjectNode node;
        while (iter.hasNext()) {
            node = (ObjectNode) iter.next();
            objs[i++] = new JSONObject(node);
        }
        return objs;
    }
    
    public static JSONObject parseObject(String str) {
        if (StringUtil.isNull(str)) {
            return null;
        }
        ObjectNode root = null;
        try {
            root = (ObjectNode)mapper.readTree(str);
        } catch (IOException ioe) {
            LOGGER.error("Parse json to object error!", ioe);
        }
        return root == null ? null : new JSONObject(root);
    }
    
    public static JSONObject[] parseArray(String str) {
        if (StringUtil.isNull(str)) {
            return null;
        }

        JsonNode root = null;
        try {
            root = mapper.readTree(str);
        } catch (IOException ioe) {
            LOGGER.error("Parse json to object error!", ioe);
        }
        return root == null ? null : toJSONObjectArray((ArrayNode) root);
    }

}
