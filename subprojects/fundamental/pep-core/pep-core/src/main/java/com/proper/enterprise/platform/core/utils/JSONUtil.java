package com.proper.enterprise.platform.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JSON 操作工具类
 * 操作类型侧重于两类：
 * 1. Object 转为 JSON String
 * 2. JSON String 和 Java Bean 容器的数据绑定
 */
public class JSONUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private JSONUtil() {
    }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new JSONUtil();

        // to allow (non-standard) unquoted field names in JSON:
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
            PEPPropertiesLoader.load(CoreProperties.class).isAllowUnquotedFieldNames());
        // to allow use of apostrophes (single quotes), non standard
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,
            PEPPropertiesLoader.load(CoreProperties.class).isAllowSingleQuotes());
    }

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param obj 容器对象
     * @return JSON 字符串
     * @throws IOException 异常
     */
    public static String toJSON(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将对象转换为 JSON 字符串
     * 忽略转换过程中的异常
     *
     * @param object 容器对象
     * @return JSON 字符串
     */
    public static String toJSONIgnoreException(Object object) {
        String str = "";
        try {
            str = toJSON(object);
        } catch (IOException e) {
            LOGGER.debug("Error occurs when parsing object to JSON!", e);
        }
        return str;
    }

    /**
     * 将 JSON 字符串转换为相应容器类型的对象
     *
     * @param str JSON 字符串
     * @param clz 容器对象类型
     * @return 容器对象
     * @throws IOException 异常
     */
    public static <T> T parse(String str, Class<T> clz) throws IOException {
        return mapper.readValue(str, clz);
    }

    /**
     * 将 JSON 字节数组转换为相应容器类型的对象
     *
     * @param bytes JSON 字节数组
     * @param clz   容器对象类型
     * @return 容器对象
     * @throws IOException 异常
     */
    public static <T> T parse(byte[] bytes, Class<T> clz) throws IOException {
        return mapper.readValue(bytes, clz);
    }

    /**
     * 将 JSONATTR 字符串转换为相应容器类型的对象集合
     *
     * @param str  JSON 字符串
     * @param type valueType
     * @return 容器对象集合
     * @throws IOException 异常
     */
    public static <T> T parse(String str, TypeReference type) throws IOException {
        return mapper.readValue(str, type);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

}
