package com.proper.enterprise.platform.test.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * JSON 操作工具类
 * 操作类型侧重于两类：
 * 1. Object 转为 JSON String
 * 2. JSON String 和 Java Bean 容器的数据绑定
 */
public class JSONUtil {

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
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // to allow use of apostrophes (single quotes), non standard
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
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
     * 将 JSON 字符串转换为相应容器类型的对象
     *
     * @param str JSON 字符串
     * @param clz 容器对象类型
     * @return 容器对象
     * @throws IOException
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
     * @throws IOException
     */
    public static <T> T parse(byte[] bytes, Class<T> clz) throws IOException {
        return mapper.readValue(bytes, clz);
    }

}
