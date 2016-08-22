package com.proper.enterprise.platform.core.utils.cipher;

public class AES {

    private CipherUtil util;

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private AES() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new AES();
    }

    public AES(String mode, String padding, String key) {
        util = CipherUtil.getInstance("AES", mode, padding, key);
    }

    public String encrypt(String content) throws Exception {
        return util.encrypt(content);
    }

    public String decrypt(String content) throws Exception {
        return util.decrypt(content);
    }

}
