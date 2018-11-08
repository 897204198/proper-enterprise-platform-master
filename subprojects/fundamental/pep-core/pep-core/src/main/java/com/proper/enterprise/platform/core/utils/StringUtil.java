package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * 字符串工具类
 * 直接继承自 apache commons-lang 中的字符串工具类，并扩展一些方法
 */
public class StringUtil extends StringUtils {
    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private StringUtil() {
    }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new StringUtil();
    }

    public static boolean isNull(String str) {
        return isBlank(str);
    }

    public static boolean isNotNull(String str) {
        return isNotBlank(str);
    }

    public static String camelToSnake(String camel) {
        String[] strs = StringUtil.splitByCharacterTypeCamelCase(camel);
        return StringUtil.join(strs, "_").toLowerCase();
    }

    /**
     * 按平台的默认编码，将byte[]转成String
     *
     * @param bytes 输入字节数组
     * @return 如果byte为null, 则返回空串 ""
     */
    public static String toEncodedString(byte[] bytes) {
        return toEncodedString(bytes, Charset.forName(PEPPropertiesLoader.load(CoreProperties.class).getCharset()));
    }

    /**
     * 按指定的charset将bytes转换成String
     *
     * @param bytes   输入字节数组
     * @param charset 字符集
     * @return 如果byte为null, 则返回空串 ""
     */
    public static String toEncodedString(final byte[] bytes, final Charset charset) {
        if (bytes != null) {
            return new String(bytes, charset);
        }
        return "";
    }

}
