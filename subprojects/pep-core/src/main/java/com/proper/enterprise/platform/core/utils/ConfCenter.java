package com.proper.enterprise.platform.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;

/**
 * 配置中心
 *
 * 配置中心在初始化时按如下优先级从下列位置读取配置参数：
 *
 * 1. Java 系统属性（System.getProperties()）
 * 2. OS 环境变量
 * 3. conf 路径下任意子路径中的 *.properties 配置文件
 */
public class ConfCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfCenter.class);

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private ConfCenter() { }

    private static Properties properties;

    /**
     * 默认配置文件加载路径
     */
    private static final String DEFAULT_CONFIG_PATH = "classpath*:conf/**/*.properties";

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new ConfCenter();
        properties = new Properties();
        init();
    }

    /**
     * 初始化配置中心，按优先级倒序读取配置文件
     */
    private static void init() {
        loadProperties();
        loadEnvVariables();
        loadSysProperties();
    }

    /**
     * 从默认配置文件加载路径加载配置文件中的参数至配置中心
     */
    private static void loadProperties() {
        _loadProperties(DEFAULT_CONFIG_PATH);
    }

    /**
     * 从指定路径样式加载配置文件
     * 支持 Ant 风格的路径样式，由 Spring 负责实际的加载工作
     *
     * @param locationPattern Ant 风格的路径样式
     */
    private static void _loadProperties(String locationPattern) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(locationPattern);
            for (Resource res : resources) {
                LOGGER.trace("Load config file from {}", res.getDescription());
                properties.load(res.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Load properties defined in '{}' error!", DEFAULT_CONFIG_PATH, e);
        }
    }

    /**
     * 加载操作系统环境变量至配置中心
     */
    private static void loadEnvVariables() {
        properties.putAll(System.getProperties());
    }

    /**
     * 加载 JVM 系统参数至配置中心
     */
    private static void loadSysProperties() {
        properties.putAll(System.getenv());
    }

    /**
     * 从配置中心按照参数名获得参数值
     * 当参数不存在时，返回 null
     *
     * @param key 参数名
     * @return 参数值或 null
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 从配置中心按照参数名获得参数值
     * 当参数不存在时，返回默认值
     *
     * @param key           参数名
     * @param defaultValue  参数值
     * @return 参数值或默认值
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, defaultValue + ""));
    }

    /**
     * 从非默认路径加载额外的配置文件
     *
     * @param locationPattern Ant 风格路径样式
     */
    public static void loadProperties(String locationPattern) {
        _loadProperties(locationPattern);
    }

    /**
     * 重新加载配置中心所有配置
     * 注意：从非默认路径加载的额外配置文件，重新加载后将丢失！
     */
    public static void reload() {
        properties.clear();
        init();
    }

}
