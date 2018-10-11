package com.proper.enterprise.platform.core.utils.encrypt;

import com.proper.enterprise.platform.core.utils.valid.ValidUtil;

/**
 * 加密工具类
 */
public class EncryptUtil {

    private EncryptUtil() {

    }

    /**
     * 加密email 默认从第三位开始加密
     *
     * @param email email
     * @return 加密后的email
     */
    public static String encryptEmail(String email) {
        return encryptEmail(email, 3);
    }

    /**
     * 对emaul加密
     *
     * @param email  email
     * @param digits 从第几位开始加密
     * @return 加密后的email
     */
    public static String encryptEmail(String email, int digits) {
        if (!ValidUtil.isEmail(email)) {
            return email;
        }
        String[] emailAttr = email.split("\\@");
        String prefix = emailAttr[0];
        String suffix = emailAttr[1];
        if (prefix.length() <= digits || digits <= 0) {
            return prefix + "@" + suffix;
        }
        return prefix.substring(0, digits) + "***@" + suffix;
    }
}
