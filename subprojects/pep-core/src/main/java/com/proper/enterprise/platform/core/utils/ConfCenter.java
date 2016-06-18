package com.proper.enterprise.platform.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
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
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new ConfCenter();
        properties = new Properties();

        loadProperties();
        loadEnvVariables();
        loadSysProperties();
    }

    private static void loadProperties() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:conf/**/*.properties");
            for (Resource res : resources) {
                properties.load(res.getInputStream());
            }
        } catch (IOException e) {
            LOGGER.error("Load properties defined in 'classpath*:conf/**/*.properties' error!", e);
        }
    }

    private static void loadEnvVariables() {
        properties.putAll(System.getProperties());
    }

    private static void loadSysProperties() {
        properties.putAll(System.getenv());
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
