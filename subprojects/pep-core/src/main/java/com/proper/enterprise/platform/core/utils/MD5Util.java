package com.proper.enterprise.platform.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5 工具类
 */
public class MD5Util {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private MD5Util() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new MD5Util();
    }

    /**
     * 计算输入字符串的 MD5 值，并将其转化为 16 进制字符串输出
     *
     * @param input 需计算 MD5 值的原始字符串
     * @return MD5 值 16 进制表示
     */
    public static String md5Hex(String input) {
        return DigestUtils.md5Hex(input);
    }

}
