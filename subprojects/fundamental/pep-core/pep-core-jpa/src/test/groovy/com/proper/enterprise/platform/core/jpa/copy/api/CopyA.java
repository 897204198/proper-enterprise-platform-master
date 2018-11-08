package com.proper.enterprise.platform.core.jpa.copy.api;

public interface CopyA {

    /**
     * 获取name
     * @return 字符串
     */
    String getName();

    /**
     * 赋值name
     * @param name name
     */
    void setName(String name);

    /**
     * 获取Cb
     * @return CopyB
     */
    CopyB getCb();

    /**
     * 赋值Cb
     * @param b b
     */
    void setCb(CopyB b);
}
