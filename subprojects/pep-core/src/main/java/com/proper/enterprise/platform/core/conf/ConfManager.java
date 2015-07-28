/* 
 * @author Hinex
 * @date 2015-4-2 14:40:04
 */

package com.proper.enterprise.platform.core.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfManager.class);
	
	private ConfManager() { }
	
	private Properties properties;
	
	private ConfManager(Properties properties) {
		this.properties = properties;
	}
	
	public static ConfManager getConf(String path) throws IOException {
		Properties properties = new Properties();
		String filename = "/conf/" + path.replace(".", "/") + "/" + path.replace(".", "-") + ".properties";
		InputStream is = ConfManager.class.getResourceAsStream(filename);
		try {
			properties.load(is);
		} catch(IOException e) {
			LOGGER.error("Load properties:{} error.", filename, e);
		} finally {
			is.close();
		}
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
	 * @author Hinex
	 * @date 2015-6-21 22:44:43
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
     * @author Hinex
     * @date 2015-6-21 22:53:18
     * @param path         path of configuration resource file
     * @param key          key in resource file
     * @param defaultVal   default value when not setting
     * @return key's value or default value
     */
    public static int getInt(String path, String key, int defaultVal) {
        return getInteger(path, key, defaultVal).intValue();
    }
	
	/**
	 * Load resource, get key's Integer value or default value in one step.
	 * 
	 * @author Hinex
	 * @date 2015-6-21 22:52:28
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
