/*
 * @author Hinex
 * @date 2015-4-2 14:40:04
 */

package com.proper.enterprise.platform.core.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Common Config Manager
 *
 * Config file should be placed like format below:
 *
 * /conf/a/a.properties
 * /conf/a/b/a-b.properties
 *
 * and when get it, pass in path like 'a', or 'a.b'
 *
 * @author Hinex
 */
public class ConfManager {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private ConfManager() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new ConfManager();
    }

    private Properties properties;

    private ConfManager(Properties properties) {
        this.properties = properties;
    }

    public static ConfManager getConf(String path) throws IOException {
        Properties properties = new Properties();
        String filename = "/conf/" + path.replace(".", "/") + "/" + path.replace(".", "-") + ".properties";
        InputStream is = ConfManager.class.getResourceAsStream(filename);
        properties.load(is);
        is.close();
        return new ConfManager(properties);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultVal) {
        String str = getString(key);
        if(str == null) {
            return defaultVal;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * Load resource, get key's string value or default value in one step.
     *
     * @param path         path of configuration resource file
     * @param key          key in resource file
     * @param defaultVal   default value when not setting
     * @return key's value or default value
     */
    public static String getString(String path, String key, String defaultVal) {
        try {
            ConfManager cp = ConfManager.getConf(path);
            return cp.getString(key, defaultVal);
        } catch (IOException e) {
            return defaultVal;
        }
    }

    /**
     * Load resource, get key's int value or default value in one step.
     *
     * @param path         path of configuration resource file
     * @param key          key in resource file
     * @param defaultVal   default value when not setting
     * @return key's value or default value
     */
    public static int getInt(String path, String key, int defaultVal) {
        return getInteger(path, key, defaultVal);
    }

    /**
     * Load resource, get key's Integer value or default value in one step.
     *
     * @param path         path of configuration resource file
     * @param key          key in resource file
     * @param defaultVal   default value when not setting
     * @return key's value or default value
     */
    public static Integer getInteger(String path, String key, Integer defaultVal) {
        try {
            ConfManager cp = ConfManager.getConf(path);
            return cp.getInt(key, defaultVal);
        } catch (IOException e) {
            return defaultVal;
        }
    }

}
