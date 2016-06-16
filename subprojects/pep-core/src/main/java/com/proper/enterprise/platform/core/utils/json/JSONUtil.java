package com.proper.enterprise.platform.core.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * JSON 操作工具类
 * 操作类型侧重于两类：
 * 1. Object 转为 JSON String
 * 2. JSON String 和 Java Bean 的数据绑定
 */
public class JSONUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private JSONUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new JSONUtil();

        // to allow (non-standard) unquoted field names in JSON:
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, PEPConstants.ALLOW_UNQUOTED_FIELD_NAMES);
        // to allow use of apostrophes (single quotes), non standard
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, PEPConstants.ALLOW_SINGLE_QUOTES);
    }

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param obj 容器对象
     * @return JSON 字符串
     * @throws IOException
     */
    public static String toJSON(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }



    /**
     * 解析 JSON 字符串为 JSONObject
     *
     * @param str JSON 字符串
     * @return 通用的 JSON 对象
     * @throws IOException
     */
    public static JSONObject parseObject(String str) throws IOException {
        ObjectNode root = (ObjectNode)mapper.readTree(str);
        return new JSONObject(root);
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



    public static <T> T parseObject(String str, Class<T> clz) {
        if (StringUtil.isNull(str)) {
            return null;
        }

        T result = null;
        try {
            result = mapper.readValue(str, clz);
        } catch (IOException ioe) {
            LOGGER.error("Parse json to {} error!", clz.getName(), ioe);
        }
        return result;
    }

    public static <T> T parseObject(byte[] bytes, Class<T> clz) {
        if (bytes == null) {
            return null;
        }

        T result = null;
        try {
            result = mapper.readValue(bytes, clz);
        } catch (IOException ioe) {
            LOGGER.error("Parse json to {} error!", clz.getName(), ioe);
        }
        return result;
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
