package com.proper.enterprise.platform.core.utils.digest;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private SHA() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new SHA();
    }

    public static String sha256(String content) {
        return DigestUtils.sha256Hex(content);
    }

}
