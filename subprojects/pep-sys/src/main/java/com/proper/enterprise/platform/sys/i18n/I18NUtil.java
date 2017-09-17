package com.proper.enterprise.platform.sys.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 * 用在无法注入 Spring Bean 的场合
 * 可注入 Bean 时，请使用 {@link I18NService}
 */
public class I18NUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18NUtil.class);

    private String baseName;

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private I18NUtil() { }

    public static I18NUtil getInstance(String baseName) {
        I18NUtil instance = new I18NUtil();
        instance.baseName = baseName;
        return instance;
    }

    public static String getString(String baseName, String key) {
        return transCode(ResourceBundle.getBundle(baseName).getString(key));
    }

    private static String transCode(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            LOGGER.warn("Should NOT throw this exception!", uee);
        }
        return result;
    }

    public String getString(String key) {
        return getString(this.baseName, key);
    }

}
